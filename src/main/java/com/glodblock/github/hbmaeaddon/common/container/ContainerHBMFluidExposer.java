package com.glodblock.github.hbmaeaddon.common.container;

import java.util.Collections;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

import com.glodblock.github.hbmaeaddon.api.HBMFluidInventory;
import com.glodblock.github.hbmaeaddon.api.IHBMFluidExposerHost;
import com.glodblock.github.hbmaeaddon.common.me.HBMFluidExposer;
import com.glodblock.github.hbmaeaddon.util.HBMFluidSyncHelper;
import com.hbm.inventory.FluidStack;

import appeng.api.config.SecurityPermissions;
import appeng.util.Platform;

public class ContainerHBMFluidExposer extends ContainerWithHBMSlot<IHBMFluidExposerHost> {

    private final HBMFluidSyncHelper tankSync;

    public ContainerHBMFluidExposer(InventoryPlayer ip, IHBMFluidExposerHost host) {
        super(ip, host);
        this.tankSync = HBMFluidSyncHelper.create(host.getExposer().getTanks(), HBMFluidExposer.TANK_SLOT);
        bindPlayerInventory(ip, 0, 149);
    }

    @Override
    public void detectAndSendChanges() {
        this.verifyPermissions(SecurityPermissions.BUILD, false);
        if (Platform.isServer()) {
            this.tankSync.sendDiff(this.crafters);
        }
        super.detectAndSendChanges();
    }

    @Override
    public void addCraftingToCrafters(@Nonnull ICrafting listener) {
        super.addCraftingToCrafters(listener);
        this.tankSync.sendFull(Collections.singleton(listener));
    }

    @Override
    public void receiveHBMSlots(final Map<Integer, FluidStack> fluids) {
        super.receiveHBMSlots(fluids);
        // Prevent cheat packet.
        // System.out.print(fluids + "\n");
        if (Platform.isClient()) {
            this.tankSync.readPacket(fluids);
        }
    }

    @Override
    public HBMFluidInventory getHBMConfigInventory() {
        return this.host.getExposer().getConfig();
    }

}
