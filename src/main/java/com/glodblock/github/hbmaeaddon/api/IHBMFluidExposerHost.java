package com.glodblock.github.hbmaeaddon.api;

import java.util.EnumSet;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.glodblock.github.hbmaeaddon.common.me.HBMFluidExposer;

import api.hbm.tile.ILoadedTile;
import appeng.api.networking.security.IActionHost;

public interface IHBMFluidExposerHost extends IActionHost, ILoadedTile {

    HBMFluidExposer getExposer();

    EnumSet<ForgeDirection> getTargets();

    TileEntity getTileEntity();

    void saveChanges();

}
