package com.glodblock.github.hbmaeaddon.common;

import com.glodblock.github.hbmaeaddon.common.block.BlockHBMFluidAcceptor;
import com.glodblock.github.hbmaeaddon.common.block.BlockHBMFluidExposer;
import com.glodblock.github.hbmaeaddon.common.tile.TileHBMFluidAcceptor;
import com.glodblock.github.hbmaeaddon.common.tile.TileHBMFluidExposer;

public class BlockAndItems {

    public static final BlockHBMFluidExposer HBM_EXPOSER = new BlockHBMFluidExposer();
    public static final BlockHBMFluidAcceptor HBM_ACCEPTOR = new BlockHBMFluidAcceptor();

    public static void init(RegistryHandler handler) {
        handler.block(HBM_EXPOSER, TileHBMFluidExposer.class, "hbm_fluid_exposer");
        handler.block(HBM_ACCEPTOR, TileHBMFluidAcceptor.class, "hbm_fluid_acceptor");
    }

}
