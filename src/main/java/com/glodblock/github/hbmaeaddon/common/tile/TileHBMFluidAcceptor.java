package com.glodblock.github.hbmaeaddon.common.tile;

import java.util.EnumSet;

import net.minecraftforge.common.util.ForgeDirection;

import com.glodblock.github.hbmaeaddon.api.IHBMFluidAcceptorHost;
import com.glodblock.github.hbmaeaddon.common.me.HBMFluidAcceptor;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.tank.FluidTank;

import api.hbm.fluid.IFluidStandardReceiver;
import appeng.tile.grid.AENetworkTile;

public class TileHBMFluidAcceptor extends AENetworkTile implements IHBMFluidAcceptorHost, IFluidStandardReceiver {

    private final HBMFluidAcceptor acceptor = new HBMFluidAcceptor(this.getProxy(), this);
    private boolean isLoaded = true;

    @Override
    public void onReady() {
        this.getProxy().setValidSides(EnumSet.complementOf(EnumSet.of(ForgeDirection.UNKNOWN)));
        super.onReady();
    }

    @Override
    public long getDemand(FluidType type, int pressure) {
        return this.acceptor.getDemand(type, pressure);
    }

    @Override
    public long transferFluid(FluidType type, int pressure, long amount) {
        return this.acceptor.transferFluid(type, pressure, amount);
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return this.acceptor.getReceivingTanks();
    }

    @Override
    public FluidTank[] getAllTanks() {
        return this.acceptor.getAllTanks();
    }

    @Override
    public boolean isLoaded() {
        return this.isLoaded;
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        this.isLoaded = false;
    }

}
