package com.glodblock.github.hbmaeaddon.common.tile;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.glodblock.github.hbmaeaddon.common.fluid.FluidHE;

import api.hbm.energymk2.IEnergyReceiverMK2;
import appeng.api.config.Actionable;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.MachineSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEFluidStack;
import appeng.me.GridAccessException;
import appeng.tile.grid.AENetworkTile;
import appeng.util.item.AEFluidStack;

public class TileHELiquefier extends AENetworkTile implements IGridTickable, IEnergyReceiverMK2 {

    private final BaseActionSource mySource = new MachineSource(this);
    private boolean isLoaded = true;

    // Don't use it.
    @Override
    public long getPower() {
        return 0;
    }

    // Don't use it.
    @Override
    public void setPower(long l) {
        // NO-OP
    }

    // Don't use it.
    @Override
    public long getMaxPower() {
        var storage = getFluidGrid();
        if (storage != null) {
            var left = storage.injectItems(getHEFluid(Integer.MAX_VALUE), Actionable.SIMULATE, this.mySource);
            if (left == null) {
                return Integer.MAX_VALUE;
            }
            return Integer.MAX_VALUE - left.getStackSize();
        }
        return 0;
    }

    @Override
    public boolean isLoaded() {
        return this.isLoaded;
    }

    private IMEMonitor<IAEFluidStack> getFluidGrid() {
        try {
            return this.getProxy().getGrid().<IStorageGrid>getCache(IStorageGrid.class).getFluidInventory();
        } catch (GridAccessException e) {
            return null;
        }
    }

    @Override
    public long transferPower(long power) {
        var energy = getHEFluid(power);
        var storage = this.getFluidGrid();
        if (storage != null) {
            var left = storage.injectItems(energy, Actionable.MODULATE, this.mySource);
            if (left == null) {
                return 0;
            } else {
                return left.getStackSize();
            }
        }
        return power;
    }

    @Override
    public void onReady() {
        this.isLoaded = true;
        super.onReady();
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        this.isLoaded = false;
    }

    @Override
    public TickingRequest getTickingRequest(IGridNode node) {
        return new TickingRequest(5, 5, false, false);
    }

    @Override
    public TickRateModulation tickingRequest(IGridNode node, int TicksSinceLastCall) {
        for (var dir : ForgeDirection.VALID_DIRECTIONS) {
            this.trySubscribe(
                    this.worldObj,
                    this.xCoord + dir.offsetX,
                    this.yCoord + dir.offsetY,
                    this.zCoord + dir.offsetZ,
                    dir);
        }
        return TickRateModulation.SAME;
    }

    private static IAEFluidStack getHEFluid(long amount) {
        return AEFluidStack.create(new FluidStack(FluidHE.HE, 1)).setStackSize(amount);
    }

}
