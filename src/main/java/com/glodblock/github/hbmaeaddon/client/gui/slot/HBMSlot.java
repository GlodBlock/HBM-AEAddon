package com.glodblock.github.hbmaeaddon.client.gui.slot;

import java.util.Collections;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import com.glodblock.github.hbmaeaddon.HBMAEAddon;
import com.glodblock.github.hbmaeaddon.api.HBMFluidInventory;
import com.glodblock.github.hbmaeaddon.client.icon.IconFluidHBM;
import com.glodblock.github.hbmaeaddon.network.packet.CSHBMSlotUpdate;
import com.glodblock.github.hbmaeaddon.util.HBMUtil;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.Fluids;

public class HBMSlot extends SpecialSlot {

    private final HBMFluidInventory fluids;
    private final int slot;

    public HBMSlot(HBMFluidInventory inv, int slot, int id, int x, int y) {
        super(id, x, y);
        this.fluids = inv;
        this.slot = slot;
    }

    @Override
    public void drawContent(Minecraft mc, int mouseX, int mouseY, float ticks) {
        var fluid = this.fluids.getFluidType(this.slot);
        if (fluid != Fluids.NONE) {
            GL11.glEnable(GL11.GL_BLEND);
            var color = fluid.getColor();
            var r = (color >> 16) & 0xff;
            var g = (color >> 8) & 0xff;
            var b = color & 0xff;
            GL11.glColor3f(r / 255F, g / 255F, b / 255F);
            mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            var icon = IconFluidHBM.getIcon(fluid);
            if (icon != null) {
                this.drawTexturedModelRectFromIcon(this.x, this.y, icon, this.getWidth(), this.getHeight());
            }
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    @Override
    public void slotClicked(final ItemStack clickStack, int mouseButton) {
        if (clickStack == null || mouseButton == 1) {
            this.setFluidStack(null);
        } else if (mouseButton == 0) {
            var type = HBMUtil.getFluidType(clickStack);
            if (type != Fluids.NONE) {
                this.setFluidStack(new FluidStack(type, 1000));
            }
        }
    }

    @Override
    public String getMessage() {
        var fluid = this.getFluidStack();
        if (fluid != null) {
            return fluid.type.getLocalizedName();
        }
        return null;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    public void setFluidStack(FluidStack stack) {
        this.fluids.setFluidStack(this.slot, stack);
        HBMAEAddon.proxy.netHandler.sendToServer(new CSHBMSlotUpdate(Collections.singletonMap(this.getId(), stack)));
    }

    public FluidStack getFluidStack() {
        return this.fluids.getFluidStack(this.slot);
    }

}
