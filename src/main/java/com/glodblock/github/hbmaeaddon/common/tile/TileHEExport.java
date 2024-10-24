package com.glodblock.github.hbmaeaddon.common.tile;

import api.hbm.energymk2.IEnergyConductorMK2;
import api.hbm.energymk2.IEnergyProviderMK2;
import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.energymk2.Nodespace;
import appeng.api.config.Actionable;
import appeng.api.networking.IGridHost;
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
import com.glodblock.github.hbmaeaddon.common.fluid.FluidHE;
import com.hbm.util.Compat;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class TileHEExport extends AENetworkTile implements IGridTickable, IEnergyProviderMK2 {

    private final BaseActionSource mySource = new MachineSource(this);
    private boolean isLoaded = true;

    // Don't use it.
    @Override
    public long getPower() {
        return this.getProviderSpeed();
    }

    // Don't use it.
    @Override
    public void setPower(long l) {
        // NO-OP
    }

    // Don't use it.
    @Override
    public long getMaxPower() {
        return this.getProviderSpeed();
    }

    @Override
    public boolean isLoaded() {
        return this.isLoaded;
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
    public void usePower(long power) {
        var storage = this.getFluidGrid();
        if (storage != null) {
            storage.extractItems(getHEFluid(power), Actionable.MODULATE, this.mySource);
        }
    }

    @Override
    public long getProviderSpeed() {
        var storage = this.getFluidGrid();
        if (storage != null) {
            var ext = storage.extractItems(getHEFluid(Integer.MAX_VALUE), Actionable.SIMULATE, this.mySource);
            if (ext == null) {
                return 0;
            }
            return ext.getStackSize();
        }
        return 0;
    }

    @Override
    public void tryProvide(World world, int x, int y, int z, ForgeDirection dir) {
        var te = Compat.getTileStandard(world, x, y, z);
        if (te instanceof IGridHost gh) {
            try {
                if (this.getProxy().getGrid() == gh.getGridNode(dir.getOpposite()).getGrid()) {
                    return;
                }
            } catch (Exception e) {
                return;
            }
        }
        if (te instanceof IEnergyConductorMK2 con) {
            if (con.canConnect(dir.getOpposite())) {
                Nodespace.PowerNode node = Nodespace.getNode(world, x, y, z);
                if (node != null && node.net != null) {
                    node.net.addProvider(this);
                }
            }
        }
        var storage = this.getFluidGrid();
        if (storage == null) {
            return;
        }
        if (te instanceof IEnergyReceiverMK2 rec && te != this) {
            if (rec.canConnect(dir.getOpposite())) {
                long toSend = Math.max(rec.getMaxPower() - rec.getPower(), 0);
                if (toSend <= 0) {
                    return;
                }
                var ext = storage.extractItems(getHEFluid(toSend), Actionable.MODULATE, this.mySource);
                if (ext == null) {
                    return;
                }
                toSend = ext.getStackSize();
                var left = rec.transferPower(toSend);
                if (left > 0) {
                    storage.injectItems(getHEFluid(left), Actionable.MODULATE, this.mySource);
                }
            }
        }
    }

    private IMEMonitor<IAEFluidStack> getFluidGrid() {
        try {
            return this.getProxy().getGrid().<IStorageGrid>getCache(IStorageGrid.class).getFluidInventory();
        } catch (GridAccessException e) {
            return null;
        }
    }

    @Override
    public TickingRequest getTickingRequest(IGridNode node) {
        return new TickingRequest(5, 5, false, false);
    }

    @Override
    public TickRateModulation tickingRequest(IGridNode node, int TicksSinceLastCall) {
        for (var dir : ForgeDirection.VALID_DIRECTIONS) {
            this.tryProvide(this.worldObj, this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ, dir);
        }
        return TickRateModulation.SAME;
    }

    private static IAEFluidStack getHEFluid(long amount) {
        return AEFluidStack.create(new FluidStack(FluidHE.HE, 1)).setStackSize(amount);
    }

}
