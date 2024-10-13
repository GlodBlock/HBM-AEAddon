package com.glodblock.github.hbmaeaddon.network.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.glodblock.github.hbmaeaddon.HBMAEAddon;
import com.glodblock.github.hbmaeaddon.api.GuiMode;

import appeng.util.Platform;
import cpw.mods.fml.common.network.IGuiHandler;

public class HBMGuiHandler implements IGuiHandler {

    public static final HBMGuiHandler INSTANCE = new HBMGuiHandler();

    private HBMGuiHandler() {
        // NO-OP
    }

    public void openTile(EntityPlayer player, World world, GuiCreator<?> gui, int x, int y, int z) {
        if (Platform.isClient()) {
            return;
        }
        player.openGui(HBMAEAddon.INSTANCE, (gui.getID() << 5) | GuiMode.TILE.ordinal(), world, x, y, z);
    }

    public void openPart(EntityPlayer player, World world, GuiCreator<?> gui, int x, int y, int z,
            ForgeDirection face) {
        if (Platform.isClient()) {
            return;
        }
        player.openGui(
                HBMAEAddon.INSTANCE,
                (gui.getID() << 5) | (face.ordinal() << 2) | GuiMode.TILE.ordinal(),
                world,
                x,
                y,
                z);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        var mode = GuiMode.decode(ID);
        var gui = GuiCreator.getGui(ID >> 5);
        var face = ForgeDirection.getOrientation(ID >> 2);
        return gui.createServerGui(mode, player, world, x, y, z, face);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        var mode = GuiMode.decode(ID);
        var gui = GuiCreator.getGui(ID >> 5);
        var face = ForgeDirection.getOrientation(ID >> 2);
        return gui.createClientGui(mode, player, world, x, y, z, face);
    }

}
