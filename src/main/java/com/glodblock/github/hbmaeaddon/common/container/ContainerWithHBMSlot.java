package com.glodblock.github.hbmaeaddon.common.container;

import java.util.Collections;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

import com.glodblock.github.hbmaeaddon.api.HBMFluidInventory;
import com.glodblock.github.hbmaeaddon.api.IHBMFluidSyncContainer;
import com.glodblock.github.hbmaeaddon.util.HBMFluidSyncHelper;
import com.hbm.inventory.FluidStack;

import appeng.container.AEBaseContainer;
import appeng.util.Platform;

public abstract class ContainerWithHBMSlot<T> extends AEBaseContainer implements IHBMFluidSyncContainer {

    protected final HBMFluidSyncHelper sync;
    protected final T host;

    public ContainerWithHBMSlot(InventoryPlayer ip, T obj) {
        super(ip, obj);
        this.host = obj;
        this.sync = HBMFluidSyncHelper.create(this.getHBMConfigInventory(), 0);
    }

    public abstract HBMFluidInventory getHBMConfigInventory();

    @Override
    public void detectAndSendChanges() {
        if (Platform.isServer()) {
            this.sync.sendDiff(this.crafters);
        }
        super.detectAndSendChanges();
    }

    @Override
    public void addCraftingToCrafters(@Nonnull ICrafting listener) {
        super.addCraftingToCrafters(listener);
        this.sync.sendFull(Collections.singleton(listener));
    }

    @Override
    public void receiveHBMSlots(final Map<Integer, FluidStack> fluids) {
        this.sync.readPacket(fluids);
    }

}
