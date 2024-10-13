package com.glodblock.github.hbmaeaddon.common.me;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.glodblock.github.hbmaeaddon.api.HBMFluidInventory;
import com.glodblock.github.hbmaeaddon.api.IHBMFluidExposerHost;
import com.glodblock.github.hbmaeaddon.api.IHBMFluidInventoryHost;
import com.glodblock.github.hbmaeaddon.util.HBMFluidBridge;
import com.glodblock.github.hbmaeaddon.util.HBMUtil;
import com.glodblock.github.inventory.MEMonitorIFluidHandler;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;

import api.hbm.fluid.IFluidStandardSender;
import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.events.MENetworkChannelsChanged;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.MachineSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IStorageMonitorable;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AECableType;
import appeng.core.settings.TickRates;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.storage.MEMonitorPassThrough;
import appeng.me.storage.NullInventory;
import appeng.util.Platform;

public class HBMFluidExposer
        implements IGridTickable, IFluidStandardSender, IHBMFluidInventoryHost, IStorageMonitorable {

    public static final int TANK_SLOT = 6;
    public static final int TANK_CAPACITY = 64000;
    private final HBMFluidInventory storage = new HBMFluidInventory(this, TANK_SLOT, TANK_CAPACITY);
    private final HBMFluidInventory config = new HBMFluidInventory(this, TANK_SLOT);
    private final FluidStack[] requireWork = new FluidStack[TANK_SLOT];
    private final AENetworkProxy gridProxy;
    private final IHBMFluidExposerHost iHost;
    private final BaseActionSource mySource;
    private boolean hasConfig = false;
    private int isWorking = -1;
    private final MEMonitorPassThrough<IAEFluidStack> fluids = new MEMonitorPassThrough<IAEFluidStack>(
            new NullInventory<>(),
            StorageChannel.FLUIDS);
    private boolean resetConfigCache = true;

    public HBMFluidExposer(AENetworkProxy networkProxy, IHBMFluidExposerHost ih) {
        this.gridProxy = networkProxy;
        this.gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
        this.iHost = ih;
        this.mySource = new MachineSource(this.iHost);
        this.fluids.setChangeSource(this.mySource);
        for (int i = 0; i < 6; ++i) {
            this.requireWork[i] = null;
        }
    }

    private FluidStack getStandardFluidUnsafe(Object obj) {
        return new FluidStack(HBMUtil.getFluidType(obj), 1000);
    }

    public FluidStack getStandardFluid(Object obj) {
        var unsafe = getStandardFluidUnsafe(obj);
        if (unsafe.type == null || unsafe.type == Fluids.NONE) {
            return null;
        }
        return unsafe;
    }

    public AECableType getCableConnectionType(final ForgeDirection dir) {
        return AECableType.SMART;
    }

    public void saveChanges() {
        this.iHost.saveChanges();
    }

    public void onChannelStateChange(final MENetworkChannelsChanged c) {
        this.notifyNeighbors();
    }

    public void onPowerStateChange(final MENetworkPowerStatusChange c) {
        this.notifyNeighbors();
    }

    public void gridChanged() {
        try {
            this.fluids.setInternal(this.gridProxy.getStorage().getFluidInventory());
        } catch (GridAccessException e) {
            this.fluids.setInternal(new NullInventory<>());
        }
        this.notifyNeighbors();
    }

    public void writeToNBT(NBTTagCompound data) {
        this.storage.saveToNBT(data, "storage");
        this.config.saveToNBT(data, "config");
    }

    public void readFromNBT(NBTTagCompound data) {
        this.storage.loadFromNBT(data, "storage");
        this.config.loadFromNBT(data, "config");
        this.readConfig();
    }

    public void notifyNeighbors() {
        if (this.gridProxy.isActive()) {
            try {
                this.gridProxy.getTick().wakeDevice(this.gridProxy.getNode());
            } catch (GridAccessException ignored) {}
        }
        TileEntity te = this.iHost.getTileEntity();
        if (te != null && te.getWorldObj() != null) {
            Platform.notifyBlocksOfNeighbors(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord);
        }
    }

    public HBMFluidInventory getConfig() {
        return this.config;
    }

    public HBMFluidInventory getTanks() {
        return this.storage;
    }

    private IMEMonitor<IAEFluidStack> getFluidGrid() {
        try {
            return this.gridProxy.getGrid().<IStorageGrid>getCache(IStorageGrid.class).getFluidInventory();
        } catch (GridAccessException e) {
            return null;
        }
    }

    @Override
    public TickingRequest getTickingRequest(IGridNode node) {
        return new TickingRequest(1, TickRates.Interface.getMax(), !this.hasWorkToDo(), true);
    }

    @Override
    public TickRateModulation tickingRequest(IGridNode node, int TicksSinceLastCall) {
        if (!this.gridProxy.isActive()) {
            return TickRateModulation.SLEEP;
        } else {
            this.sendAround();
            boolean couldDoWork = this.updateStorage();
            return this.hasWorkToDo() ? (couldDoWork ? TickRateModulation.URGENT : TickRateModulation.SAME)
                    : TickRateModulation.SLOWER;
        }
    }

    private void sendAround() {
        var tile = this.iHost.getTileEntity();
        var w = tile.getWorldObj();
        var x = tile.xCoord;
        var y = tile.yCoord;
        var z = tile.zCoord;
        for (var dir : this.iHost.getTargets()) {
            for (var type : this.availableFluid()) {
                this.sendFluid(type, 0, w, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir);
            }
        }
    }

    @Override
    public void sendFluid(FluidType type, int pressure, World world, int x, int y, int z, ForgeDirection dir) {
        var te = world.getTileEntity(x, y, z);
        if (te instanceof IGridHost gh) {
            try {
                if (this.gridProxy.getGrid() == gh.getGridNode(dir.getOpposite()).getGrid()) {
                    return;
                }
            } catch (Exception e) {
                return;
            }
        }
        IFluidStandardSender.super.sendFluid(type, pressure, world, x, y, z, dir);
    }

    private Collection<FluidType> availableFluid() {
        var list = new HashSet<FluidType>();
        for (var tank : this.storage.asArray()) {
            list.add(tank.getTankType());
        }
        list.remove(Fluids.NONE);
        return list;
    }

    private boolean updateStorage() {
        boolean didSomething = false;
        for (int x = 0; x < TANK_SLOT; ++x) {
            if (this.requireWork[x] != null) {
                didSomething = this.usePlan(x) || didSomething;
            }
        }
        return didSomething;
    }

    private boolean usePlan(int slot) {
        var work = this.requireWork[slot];
        this.isWorking = slot;
        boolean changed = false;
        IMEInventory<IAEFluidStack> dest = getFluidGrid();
        if (dest == null) {
            this.isWorking = -1;
            return false;
        }
        IAEFluidStack toStore;
        if (work.fill > 0) {
            if (this.storage.fill(slot, work, false) != work.fill) {
                changed = true;
            } else if (Objects.requireNonNull(getFluidGrid()).getStorageList().findPrecise(HBMFluidBridge.get(work))
                    != null) {
                        toStore = dest.extractItems(HBMFluidBridge.get(work), Actionable.MODULATE, this.mySource);
                        if (toStore != null) {
                            changed = true;
                            int filled = this.storage.fill(slot, HBMFluidBridge.get(toStore), true);
                            if ((long) filled != toStore.getStackSize()) {
                                throw new IllegalStateException("bad attempt at managing tanks. ( fill )");
                            }
                        }
                    }
        } else if (work.fill < 0L) {
            toStore = HBMFluidBridge.get(work);
            toStore.setStackSize(-toStore.getStackSize());
            var canExtract = this.storage.drain(slot, HBMFluidBridge.get(toStore), false);
            if (canExtract != null && (long) canExtract.fill == toStore.getStackSize()) {
                IAEFluidStack notStored = dest.injectItems(toStore, Actionable.MODULATE, this.mySource);
                toStore.setStackSize(toStore.getStackSize() - (notStored == null ? 0L : notStored.getStackSize()));
                if (toStore.getStackSize() > 0L) {
                    changed = true;
                    var removed = this.storage.drain(slot, HBMFluidBridge.get(toStore), true);
                    if (removed == null || toStore.getStackSize() != (long) removed.fill) {
                        throw new IllegalStateException("bad attempt at managing tanks. ( drain )");
                    }
                }
            } else {
                changed = true;
            }
        }

        if (changed) {
            this.updatePlan(slot);
        }

        this.isWorking = -1;
        return changed;
    }

    private void readConfig() {
        this.hasConfig = false;

        for (int i = 0; i < this.config.size(); ++i) {
            if (this.config.getFluidType(i) != Fluids.NONE) {
                this.hasConfig = true;
                break;
            }
        }

        boolean had = this.hasWorkToDo();

        for (int x = 0; x < 6; ++x) {
            this.updatePlan(x);
        }

        boolean has = this.hasWorkToDo();
        if (had != has) {
            try {
                if (has) {
                    this.gridProxy.getTick().alertDevice(this.gridProxy.getNode());
                } else {
                    this.gridProxy.getTick().sleepDevice(this.gridProxy.getNode());
                }
            } catch (GridAccessException ignored) {}
        }

        this.notifyNeighbors();
    }

    private boolean hasWorkToDo() {
        for (var requiredWork : this.requireWork) {
            if (requiredWork != null) {
                return true;
            }
        }
        return false;
    }

    private void updatePlan(int slot) {
        var req = getStandardFluid(this.config.getFluidType(slot));
        var stored = this.storage.getFluidStack(slot);

        if (req == null && (stored != null && stored.fill > 0)) {
            var work = HBMUtil.copyNoPressure(stored);
            work.fill = -work.fill;
            this.requireWork[slot] = work;
            return;
        } else if (req != null) {
            if (stored == null || stored.fill == 0) {
                this.requireWork[slot] = HBMUtil.copyNoPressure(req);
                this.requireWork[slot].fill = TANK_CAPACITY;
                return;
            } else if (HBMUtil.equalsType(req, stored)) {
                if (stored.fill != TANK_CAPACITY) {
                    this.requireWork[slot] = HBMUtil.copyNoPressure(req);
                    this.requireWork[slot].fill = TANK_CAPACITY - stored.fill;
                    return;
                }
            } else {
                var work = HBMUtil.copyNoPressure(stored);
                work.fill = -work.fill;
                this.requireWork[slot] = work;
                return;
            }
        }
        this.requireWork[slot] = null;
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return this.storage.asArray();
    }

    @Override
    public FluidTank[] getAllTanks() {
        return this.storage.asArray();
    }

    @Override
    public boolean isLoaded() {
        return this.iHost.isLoaded();
    }

    @Override
    public void onInventoryChange(HBMFluidInventory inventory, int slot) {
        if (this.isWorking != slot) {
            boolean had;
            if (inventory == this.config) {
                had = this.hasConfig;
                this.readConfig();
                if (had != this.hasConfig) {
                    this.resetConfigCache = true;
                    this.notifyNeighbors();
                }
            } else if (inventory == this.storage) {
                this.saveChanges();
                had = this.hasWorkToDo();
                this.updatePlan(slot);
                boolean now = this.hasWorkToDo();
                if (had != now) {
                    try {
                        if (now) {
                            this.gridProxy.getTick().alertDevice(this.gridProxy.getNode());
                        } else {
                            this.gridProxy.getTick().sleepDevice(this.gridProxy.getNode());
                        }
                    } catch (GridAccessException ignored) {}
                }
            }
        }
    }

    @Override
    public HBMFluidInventory getHBMFluidInventory() {
        return this.storage;
    }

    @Override
    public IMEMonitor<IAEItemStack> getItemInventory() {
        return null;
    }

    @Override
    public IMEMonitor<IAEFluidStack> getFluidInventory() {
        if (this.hasConfig) {
            if (this.resetConfigCache) {
                this.resetConfigCache = false;
                return new ExposerInventory(this);
            }
        }
        return this.fluids;
    }

    private static class ExposerInventory extends MEMonitorIFluidHandler {

        ExposerInventory(HBMFluidExposer exposer) {
            super(exposer.storage);
        }
    }
}
