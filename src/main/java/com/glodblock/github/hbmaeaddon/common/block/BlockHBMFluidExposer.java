package com.glodblock.github.hbmaeaddon.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.glodblock.github.hbmaeaddon.HBMAEAddon;
import com.glodblock.github.hbmaeaddon.common.tile.TileHBMFluidExposer;
import com.glodblock.github.hbmaeaddon.network.Guis;
import com.glodblock.github.hbmaeaddon.network.gui.HBMGuiHandler;
import com.glodblock.github.util.Util;

import appeng.api.config.SecurityPermissions;
import appeng.block.AEBaseTileBlock;
import appeng.core.features.ActivityState;
import appeng.core.features.BlockStackSrc;
import appeng.tile.AEBaseTile;
import appeng.util.Platform;

public class BlockHBMFluidExposer extends AEBaseTileBlock {

    public BlockHBMFluidExposer() {
        super(Material.iron);
        this.setBlockName(HBMAEAddon.MODID + ".hbm_fluid_exposer");
        this.setBlockTextureName(HBMAEAddon.MODID + ":" + "hbm_fluid_exposer");
        this.setTileEntity(TileHBMFluidExposer.class);
    }

    @Override
    public boolean onActivated(World world, int x, int y, int z, EntityPlayer player, int facing, float hitX,
            float hitY, float hitZ) {
        if (player.isSneaking()) {
            return false;
        }
        TileHBMFluidExposer tile = getTileEntity(world, x, y, z);
        if (tile != null) {
            if (Platform.isServer()) {
                if (Util.hasPermission(player, SecurityPermissions.INJECT, tile)) {
                    HBMGuiHandler.INSTANCE.openTile(player, world, Guis.HBM_EXPOSER, x, y, z);
                } else {
                    player.addChatComponentMessage(new ChatComponentText("You don't have permission to view."));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void setTileEntity(final Class<? extends TileEntity> clazz) {
        AEBaseTile.registerTileItem(clazz, new BlockStackSrc(this, 0, ActivityState.Enabled));
        super.setTileEntity(clazz);
    }

}
