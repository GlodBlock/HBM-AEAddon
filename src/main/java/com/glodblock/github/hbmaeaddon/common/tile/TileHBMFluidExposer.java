package com.glodblock.github.hbmaeaddon.common.tile;

import java.util.EnumSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.glodblock.github.hbmaeaddon.api.IHBMFluidExposerHost;
import com.glodblock.github.hbmaeaddon.common.me.HBMFluidExposer;
import com.hbm.inventory.fluid.tank.FluidTank;

import api.hbm.fluid.IFluidStandardSender;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.IGridNode;
import appeng.api.networking.events.MENetworkChannelsChanged;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IStorageMonitorable;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AECableType;
import appeng.me.GridAccessException;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.grid.AENetworkTile;
import io.netty.buffer.ByteBuf;

public class TileHBMFluidExposer extends AENetworkTile
        implements IHBMFluidExposerHost, IGridTickable, IStorageMonitorable, IFluidStandardSender, IPowerChannelState {

    private final HBMFluidExposer exposer = new HBMFluidExposer(this.getProxy(), this);

    private static final int POWERED_FLAG = 1;
    private static final int CHANNEL_FLAG = 2;
    private static final int BOOTING_FLAG = 4;
    private int clientFlags = 0; // sent as byte.
    private boolean isLoaded = true;

    @MENetworkEventSubscribe
    public void stateChange(final MENetworkChannelsChanged c) {
        this.exposer.onChannelStateChange(c);
        this.exposer.notifyNeighbors();
        markForUpdate();
    }

    @MENetworkEventSubscribe
    public void stateChange(final MENetworkPowerStatusChange c) {
        this.exposer.onPowerStateChange(c);
        this.exposer.notifyNeighbors();
        markForUpdate();
    }

    @Override
    public void gridChanged() {
        this.exposer.gridChanged();
    }

    @Override
    public void onReady() {
        this.getProxy().setValidSides(EnumSet.complementOf(EnumSet.of(ForgeDirection.UNKNOWN)));
        this.isLoaded = true;
        super.onReady();
    }

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    public void writeToNBT_TileInterface(final NBTTagCompound data) {
        this.exposer.writeToNBT(data);
    }

    @TileEvent(TileEventType.WORLD_NBT_READ)
    public void readFromNBT_TileInterface(final NBTTagCompound data) {
        this.exposer.readFromNBT(data);
    }

    @Override
    public AECableType getCableConnectionType(final ForgeDirection dir) {
        return this.exposer.getCableConnectionType(dir);
    }

    @TileEvent(TileEventType.NETWORK_READ)
    public boolean readFromStream_HBMExposer(final ByteBuf data) {
        int newState = data.readByte();
        if (newState != clientFlags) {
            clientFlags = newState;
            this.markForUpdate();
            return true;
        } else {
            return false;
        }
    }

    @TileEvent(TileEventType.NETWORK_WRITE)
    public void writeToStream_HBMExposer(final ByteBuf data) {
        clientFlags = 0;
        try {
            if (this.getProxy().getEnergy().isNetworkPowered()) clientFlags |= POWERED_FLAG;
            if (this.getProxy().getNode().meetsChannelRequirements()) clientFlags |= CHANNEL_FLAG;
            if (this.getProxy().getPath().isNetworkBooting()) clientFlags |= BOOTING_FLAG;
        } catch (final GridAccessException e) {
            // meh
        }
        data.writeByte(clientFlags);
    }

    @Override
    public IMEMonitor<IAEItemStack> getItemInventory() {
        return this.exposer.getItemInventory();
    }

    @Override
    public IMEMonitor<IAEFluidStack> getFluidInventory() {
        return this.exposer.getFluidInventory();
    }

    @Override
    public TickingRequest getTickingRequest(final IGridNode node) {
        return this.exposer.getTickingRequest(node);
    }

    @Override
    public TickRateModulation tickingRequest(final IGridNode node, final int ticksSinceLastCall) {
        return this.exposer.tickingRequest(node, ticksSinceLastCall);
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return this.exposer.getSendingTanks();
    }

    @Override
    public FluidTank[] getAllTanks() {
        return this.exposer.getAllTanks();
    }

    @Override
    public boolean isPowered() {
        return (clientFlags & POWERED_FLAG) == POWERED_FLAG;
    }

    @Override
    public boolean isActive() {
        return (clientFlags & CHANNEL_FLAG) == CHANNEL_FLAG;
    }

    @Override
    public boolean isBooting() {
        return (clientFlags & BOOTING_FLAG) == BOOTING_FLAG;
    }

    @Override
    public HBMFluidExposer getExposer() {
        return this.exposer;
    }

    @Override
    public EnumSet<ForgeDirection> getTargets() {
        return EnumSet.complementOf(EnumSet.of(ForgeDirection.UNKNOWN));
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
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
