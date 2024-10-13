package com.glodblock.github.hbmaeaddon.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import com.glodblock.github.hbmaeaddon.HBMAEAddon;
import com.glodblock.github.hbmaeaddon.api.IHBMFluidExposerHost;
import com.glodblock.github.hbmaeaddon.client.gui.slot.HBMSlot;
import com.glodblock.github.hbmaeaddon.client.gui.slot.HBMTankSlot;
import com.glodblock.github.hbmaeaddon.common.container.ContainerHBMFluidExposer;
import com.glodblock.github.hbmaeaddon.common.me.HBMFluidExposer;

import appeng.core.localization.GuiText;

public class GuiHBMFluidExposer extends GuiWithSpecialSlot {

    private final IHBMFluidExposerHost host;

    public GuiHBMFluidExposer(InventoryPlayer ip, IHBMFluidExposerHost host) {
        super(new ContainerHBMFluidExposer(ip, host));
        this.host = host;
        this.ySize = 231;
    }

    @Override
    public void initGui() {
        super.initGui();
        var config = this.host.getExposer().getConfig();
        var tank = this.host.getExposer().getTanks();
        for (int i = 0; i < HBMFluidExposer.TANK_SLOT; ++i) {
            this.addSlot(new HBMTankSlot(tank, i, HBMFluidExposer.TANK_SLOT + i, 35 + 18 * i, 53, 16, 68));
            this.addSlot(new HBMSlot(config, i, i, 35 + 18 * i, 35));
        }
    }

    @Override
    public void drawFG(int offsetX, int offsetY, int mouseX, int mouseY) {
        this.fontRendererObj.drawString(this.getGuiDisplayName(this.getGuiName()), 8, 6, 4210752);
        this.fontRendererObj.drawString(GuiText.Config.getLocal(), 8, 6 + 11 + 7, 4210752);
        this.fontRendererObj.drawString(I18n.format("gui.hbmaeaddon.stored_fluid"), 8, 6 + 112 + 7, 4210752);
        this.fontRendererObj.drawString(GuiText.inventory.getLocal(), 8, this.ySize - 96 + 3, 4210752);
    }

    @Override
    protected String getGuiName() {
        return I18n.format("gui.hbmaeaddon.hbm_fluid_exposer");
    }

    @Override
    protected ResourceLocation getBackgroundTexture() {
        return HBMAEAddon.resource("textures/guis/hbm_fluid_exposer.png");
    }
}
