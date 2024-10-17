package com.glodblock.github.hbmaeaddon.network;

import net.minecraft.entity.player.EntityPlayer;

import com.glodblock.github.hbmaeaddon.api.IHBMFluidExposerHost;
import com.glodblock.github.hbmaeaddon.client.gui.GuiHBMFluidExposer;
import com.glodblock.github.hbmaeaddon.common.container.ContainerHBMFluidExposer;
import com.glodblock.github.hbmaeaddon.network.gui.GuiCreator;

public class Guis {

    public static void ensureLoad() {
        // NO-OP
    }

    public static final GuiCreator<IHBMFluidExposerHost> HBM_EXPOSER = new GuiCreator<>(IHBMFluidExposerHost.class) {

        @Override
        public Object createServerGui(EntityPlayer player, IHBMFluidExposerHost inventory) {
            return new ContainerHBMFluidExposer(player.inventory, inventory);
        }

        @Override
        public Object createClientGui(EntityPlayer player, IHBMFluidExposerHost inventory) {
            return new GuiHBMFluidExposer(player.inventory, inventory);
        }
    };

}
