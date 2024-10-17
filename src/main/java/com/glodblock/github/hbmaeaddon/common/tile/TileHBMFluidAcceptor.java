package com.glodblock.github.hbmaeaddon.common.tile;

import java.util.EnumSet;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.glodblock.github.hbmaeaddon.api.IHBMFluidAcceptorHost;
import com.glodblock.github.hbmaeaddon.common.me.HBMFluidAcceptor;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.tank.FluidTank;

import api.hbm.fluid.IFluidStandardReceiver;
import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.tile.grid.AENetworkTile;

public class TileHBMFluidAcceptor extends AENetworkTile
        implements IHBMFluidAcceptorHost, IFluidStandardReceiver, IGridTickable {

    private final HBMFluidAcceptor acceptor = new HBMFluidAcceptor(this.getProxy(), this);
    private boolean isLoaded = true;

    @Override
    public void onReady() {
        this.getProxy().setValidSides(EnumSet.complementOf(EnumSet.of(ForgeDirection.UNKNOWN)));
        this.isLoaded = true;
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

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

    @Override
    public TickingRequest getTickingRequest(IGridNode node) {
        return new TickingRequest(5, 5, false, false);
    }

    @Override
    public TickRateModulation tickingRequest(IGridNode node, int TicksSinceLastCall) {
        this.acceptor.subscribe();
        return TickRateModulation.SAME;
    }
}
