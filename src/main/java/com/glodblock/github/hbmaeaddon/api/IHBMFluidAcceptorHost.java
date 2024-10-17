package com.glodblock.github.hbmaeaddon.api;

import net.minecraft.tileentity.TileEntity;

import api.hbm.tile.ILoadedTile;
import appeng.api.networking.security.IActionHost;

public interface IHBMFluidAcceptorHost extends IActionHost, ILoadedTile {

    TileEntity getTileEntity();

}
