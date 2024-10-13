package com.glodblock.github.hbmaeaddon.client.gui.slot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;

import org.lwjgl.opengl.GL11;

import com.glodblock.github.hbmaeaddon.api.HBMFluidInventory;
import com.glodblock.github.hbmaeaddon.client.icon.IconFluidHBM;
import com.hbm.inventory.FluidStack;

public class HBMTankSlot extends SpecialSlot {

    private final HBMFluidInventory fluids;
    private final int slot;
    private final int width;
    private final int height;

    public HBMTankSlot(HBMFluidInventory tank, int slot, int id, int x, int y, int w, int h) {
        super(id, x, y);
        this.fluids = tank;
        this.slot = slot;
        this.width = w;
        this.height = h;
    }

    @Override
    public void drawContent(Minecraft mc, int mouseX, int mouseY, float ticks) {
        var fluid = this.fluids.getFluidStack(this.slot);
        if (fluid != null) {
            GL11.glEnable(GL11.GL_BLEND);
            var color = fluid.type.getColor();
            var r = (color >> 16) & 0xff;
            var g = (color >> 8) & 0xff;
            var b = color & 0xff;
            GL11.glColor3f(r / 255F, g / 255F, b / 255F);
            mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            var icon = IconFluidHBM.getIcon(fluid.type);
            if (icon != null) {
                int scaledHeight = (int) (this.height
                        * ((float) fluid.fill / this.fluids.getTank(this.slot).getMaxFill()));
                scaledHeight = Math.min(this.height, scaledHeight);
                int iconHeightRemainder = scaledHeight % 16;
                if (iconHeightRemainder > 0) {
                    this.drawTexturedModelRectFromIcon(
                            this.xPos(),
                            this.yPos() + this.getHeight() - iconHeightRemainder,
                            icon,
                            16,
                            iconHeightRemainder);
                }
                for (int i = 0; i < scaledHeight / 16; i++) {
                    this.drawTexturedModelRectFromIcon(
                            this.xPos(),
                            this.yPos() + this.getHeight() - iconHeightRemainder - (i + 1) * 16,
                            icon,
                            16,
                            16);
                }
            }
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    @Override
    public String getMessage() {
        var fluid = this.fluids.getFluidStack(this.slot);
        if (fluid != null) {
            String desc = fluid.type.getLocalizedName();
            return desc + "\n" + fluid.fill + "/" + this.fluids.getTank(this.slot).getMaxFill() + "mB";
        }
        return null;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    public FluidStack getFluidStack() {
        return this.fluids.getFluidStack(this.slot);
    }

}
