package com.glodblock.github.hbmaeaddon.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

import com.glodblock.github.hbmaeaddon.HBMAEAddon;
import com.glodblock.github.hbmaeaddon.common.tile.TileHBMFluidAcceptor;

import appeng.block.AEBaseTileBlock;
import appeng.core.features.ActivityState;
import appeng.core.features.BlockStackSrc;
import appeng.tile.AEBaseTile;

public class BlockHBMFluidAcceptor extends AEBaseTileBlock {

    public BlockHBMFluidAcceptor() {
        super(Material.iron);
        this.setBlockName(HBMAEAddon.MODID + ".hbm_fluid_acceptor");
        this.setBlockTextureName(HBMAEAddon.MODID + ":" + "hbm_fluid_acceptor");
        this.setTileEntity(TileHBMFluidAcceptor.class);
    }

    @Override
    public void setTileEntity(final Class<? extends TileEntity> clazz) {
        AEBaseTile.registerTileItem(clazz, new BlockStackSrc(this, 0, ActivityState.Enabled));
        super.setTileEntity(clazz);
    }

}
