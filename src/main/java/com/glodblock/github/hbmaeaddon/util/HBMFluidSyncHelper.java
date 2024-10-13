package com.glodblock.github.hbmaeaddon.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;

import com.glodblock.github.hbmaeaddon.HBMAEAddon;
import com.glodblock.github.hbmaeaddon.api.HBMFluidInventory;
import com.glodblock.github.hbmaeaddon.api.IHBMFluidSyncContainer;
import com.glodblock.github.hbmaeaddon.network.packet.CSHBMSlotUpdate;
import com.hbm.inventory.FluidStack;

public class HBMFluidSyncHelper {

    private final HBMFluidInventory inv;
    private final HBMFluidInventory cache;
    private final int idOffset;

    public static HBMFluidSyncHelper create(final HBMFluidInventory inv, final int idOffset) {
        return new HBMFluidSyncHelper(inv, idOffset);
    }

    private HBMFluidSyncHelper(final HBMFluidInventory inv, final int idOffset) {
        this.inv = inv;
        this.cache = new HBMFluidInventory(inv.size());
        this.idOffset = idOffset;
    }

    public void sendFull(final Iterable<?> listeners) {
        this.sendDiffMap(this.createDiffMap(true), listeners);
    }

    public void sendDiff(final Iterable<?> listeners) {
        this.sendDiffMap(this.createDiffMap(false), listeners);
    }

    public void readPacket(final Map<Integer, FluidStack> data) {
        for (int i = 0; i < this.inv.size(); ++i) {
            if (data.containsKey(i + this.idOffset)) {
                this.inv.setFluidStack(i, data.get(i + this.idOffset) == null ? null : data.get(i + this.idOffset));
            }
        }
    }

    private void sendDiffMap(final Map<Integer, FluidStack> data, final Iterable<?> listeners) {
        if (data.isEmpty()) {
            return;
        }
        for (final Object l : listeners) {
            if (l instanceof EntityPlayerMP player) {
                Container c = player.openContainer;
                if (c instanceof IHBMFluidSyncContainer sync) {
                    sync.receiveHBMSlots(data);
                }
                HBMAEAddon.proxy.netHandler.sendTo(new CSHBMSlotUpdate(data), player);
            }
        }
    }

    private Map<Integer, FluidStack> createDiffMap(final boolean full) {
        final Map<Integer, FluidStack> ret = new HashMap<>();
        for (int i = 0; i < this.inv.size(); ++i) {
            if (full || !this.equalsSlot(i)) {
                ret.put(i + this.idOffset, HBMUtil.copy(this.inv.getFluidStack(i)));
            }
            if (!full) {
                this.cache.setFluidStack(i, this.inv.getFluidStack(i));
            }
        }
        return ret;
    }

    private boolean equalsSlot(int slot) {
        var stackA = this.inv.getFluidStack(slot);
        var stackB = this.cache.getFluidStack(slot);

        return HBMUtil.equals(stackA, stackB);
    }

}
