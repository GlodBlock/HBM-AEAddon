package com.glodblock.github.hbmaeaddon.common.block;

import appeng.block.AEBaseTileBlock;
import appeng.core.features.ActivityState;
import appeng.core.features.BlockStackSrc;
import appeng.tile.AEBaseTile;
import com.glodblock.github.hbmaeaddon.HBMAEAddon;
import com.glodblock.github.hbmaeaddon.common.tile.TileHELiquefier;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

public class BlockHELiquefier extends AEBaseTileBlock {

    public BlockHELiquefier() {
        super(Material.iron);
        this.setBlockName(HBMAEAddon.MODID + ".energy_liquefier");
        this.setBlockTextureName(HBMAEAddon.MODID + ":" + "energy_liquefier");
        this.setTileEntity(TileHELiquefier.class);
    }

    @Override
    public void setTileEntity(final Class<? extends TileEntity> clazz) {
        AEBaseTile.registerTileItem(clazz, new BlockStackSrc(this, 0, ActivityState.Enabled));
        super.setTileEntity(clazz);
    }

}
