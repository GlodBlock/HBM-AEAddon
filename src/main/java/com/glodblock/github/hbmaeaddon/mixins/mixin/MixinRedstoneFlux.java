package com.glodblock.github.hbmaeaddon.mixins.mixin;

import net.minecraftforge.common.util.ForgeDirection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.glodblock.github.hbmaeaddon.util.HBMUtil;

import api.hbm.energymk2.IEnergyReceiverMK2;
import appeng.api.config.PowerUnits;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.powersink.AERootPoweredTile;
import appeng.tile.powersink.RedstoneFlux;

@Mixin(RedstoneFlux.class)
public abstract class MixinRedstoneFlux extends AERootPoweredTile implements IEnergyReceiverMK2 {

    @Unique
    private boolean isLoaded = true;

    @Override
    public long getPower() {
        return 0;
    }

    @Override
    public void setPower(long l) {
        this.setInternalCurrentPower(HBMUtil.asAE(l));
    }

    @Override
    public long transferPower(long power) {
        var aeSend = HBMUtil.asAE(power);
        var aeNeed = this.getExternalPowerDemand(PowerUnits.AE, aeSend);
        var aeUsed = Math.min(aeNeed, aeSend);
        this.injectExternalPower(PowerUnits.AE, aeUsed);
        return Math.max(0, power - HBMUtil.asHE(aeUsed));
    }

    @Override
    public long getMaxPower() {
        return HBMUtil.asHE(this.getFunnelPowerDemand(Integer.MAX_VALUE));
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

    @Unique
    @TileEvent(TileEventType.TICK)
    public void subscribe() {
        for (var dir : ForgeDirection.VALID_DIRECTIONS) {
            this.trySubscribe(
                    this.worldObj,
                    this.xCoord + dir.offsetX,
                    this.yCoord + dir.offsetY,
                    this.zCoord + dir.offsetZ,
                    dir);
        }
    }

}
