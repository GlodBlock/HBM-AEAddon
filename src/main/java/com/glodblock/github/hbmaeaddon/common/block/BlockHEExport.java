package com.glodblock.github.hbmaeaddon.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

import com.glodblock.github.hbmaeaddon.HBMAEAddon;
import com.glodblock.github.hbmaeaddon.common.tile.TileHEExport;

import appeng.block.AEBaseTileBlock;
import appeng.core.features.ActivityState;
import appeng.core.features.BlockStackSrc;
import appeng.tile.AEBaseTile;

public class BlockHEExport extends AEBaseTileBlock {

    public BlockHEExport() {
        super(Material.iron);
        this.setBlockName(HBMAEAddon.MODID + ".energy_export");
        this.setBlockTextureName(HBMAEAddon.MODID + ":" + "energy_export");
        this.setTileEntity(TileHEExport.class);
    }

    @Override
    public void setTileEntity(final Class<? extends TileEntity> clazz) {
        AEBaseTile.registerTileItem(clazz, new BlockStackSrc(this, 0, ActivityState.Enabled));
        super.setTileEntity(clazz);
    }

}
