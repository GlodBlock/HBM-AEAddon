package com.glodblock.github.hbmaeaddon.client.gui.slot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import appeng.client.gui.widgets.ITooltip;

public abstract class SpecialSlot extends Gui implements ITooltip {

    protected final int x;
    protected final int y;
    protected final int id;

    public SpecialSlot(int id, int x, int y) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public boolean canClick(EntityPlayer player) {
        return true;
    }

    public void slotClicked(ItemStack clickStack, int mouseButton) {}

    public abstract void drawContent(Minecraft mc, int mouseX, int mouseY, float ticks);

    public String getMessage() {
        return null;
    }

    public int xPos() {
        return this.x;
    }

    public int yPos() {
        return this.y;
    }

    public int getWidth() {
        return 16;
    }

    public int getHeight() {
        return 16;
    }

    public boolean isVisible() {
        return false;
    }

    public boolean isSlotEnabled() {
        return true;
    }

}
