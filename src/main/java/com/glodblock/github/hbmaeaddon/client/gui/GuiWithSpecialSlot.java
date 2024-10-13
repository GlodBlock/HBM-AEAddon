package com.glodblock.github.hbmaeaddon.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.glodblock.github.hbmaeaddon.client.gui.slot.SpecialSlot;

import appeng.client.gui.AEBaseGui;

public abstract class GuiWithSpecialSlot extends AEBaseGui {

    private final List<SpecialSlot> specialSlots = new ArrayList<>();

    public GuiWithSpecialSlot(Container container) {
        super(container);
    }

    protected void addSlot(SpecialSlot slot) {
        this.specialSlots.add(slot);
    }

    protected List<SpecialSlot> getSpecialSlots() {
        return this.specialSlots;
    }

    protected abstract String getGuiName();

    protected abstract ResourceLocation getBackgroundTexture();

    @Override
    public void drawBG(int offsetX, int offsetY, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(this.getBackgroundTexture());
        drawTexturedModalRect(offsetX, offsetY, 0, 0, 176, ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glPushMatrix();
        GL11.glTranslated(this.guiLeft, this.guiTop, 0.0F);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        for (var slot : this.getSpecialSlots()) {
            this.drawSpecialSlot(slot, mouseX, mouseY, partialTicks);
        }
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        for (var slot : this.getSpecialSlots()) {
            this.handleTooltip(mouseX - this.guiLeft, mouseY - this.guiTop, slot);
        }
        GL11.glPopMatrix();
    }

    @SuppressWarnings("unchecked")
    public <T extends Container> T getContainer() {
        return (T) this.inventorySlots;
    }

    @Override
    protected void mouseClicked(final int xCoord, final int yCoord, final int btn) {
        for (var slot : this.getSpecialSlots()) {
            if (this.isMouseOverRect(slot.xPos(), slot.yPos(), slot.getWidth(), slot.getHeight(), xCoord, yCoord)
                    && slot.canClick(this.mc.thePlayer)) {
                slot.slotClicked(this.mc.thePlayer.inventory.getItemStack(), btn);
            }
        }
        super.mouseClicked(xCoord, yCoord, btn);
    }

    protected void drawSpecialSlot(SpecialSlot slot, int mouseX, int mouseY, float partialTicks) {
        if (slot.isSlotEnabled()) {
            int left = slot.xPos();
            int top = slot.yPos();
            int right = left + slot.getWidth();
            int bottom = top + slot.getHeight();
            slot.drawContent(this.mc, mouseX, mouseY, partialTicks);
            if (this.isMouseOverRect(left, top, slot.getWidth(), slot.getHeight(), mouseX, mouseY)
                    && slot.canClick(this.mc.thePlayer)) {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glColorMask(true, true, true, false);
                this.drawGradientRect(left, top, right, bottom, -2130706433, -2130706433);
                GL11.glColorMask(true, true, true, true);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
    }

}
