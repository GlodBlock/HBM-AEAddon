package com.glodblock.github.hbmaeaddon.common;

import com.glodblock.github.hbmaeaddon.common.block.BlockHBMFluidAcceptor;
import com.glodblock.github.hbmaeaddon.common.block.BlockHBMFluidExposer;
import com.glodblock.github.hbmaeaddon.common.block.BlockHEExport;
import com.glodblock.github.hbmaeaddon.common.block.BlockHELiquefier;
import com.glodblock.github.hbmaeaddon.common.tile.TileHBMFluidAcceptor;
import com.glodblock.github.hbmaeaddon.common.tile.TileHBMFluidExposer;
import com.glodblock.github.hbmaeaddon.common.tile.TileHEExport;
import com.glodblock.github.hbmaeaddon.common.tile.TileHELiquefier;

public class BlockAndItems {

    public static final BlockHBMFluidExposer HBM_EXPOSER = new BlockHBMFluidExposer();
    public static final BlockHBMFluidAcceptor HBM_ACCEPTOR = new BlockHBMFluidAcceptor();
    public static final BlockHELiquefier HE_LIQUEFIER = new BlockHELiquefier();
    public static final BlockHEExport HE_EXPORT = new BlockHEExport();

    public static void init(RegistryHandler handler) {
        handler.block(HBM_EXPOSER, TileHBMFluidExposer.class, "hbm_fluid_exposer");
        handler.block(HBM_ACCEPTOR, TileHBMFluidAcceptor.class, "hbm_fluid_acceptor");
        handler.block(HE_LIQUEFIER, TileHELiquefier.class, "energy_liquefier");
        handler.block(HE_EXPORT, TileHEExport.class, "energy_export");
    }

}
