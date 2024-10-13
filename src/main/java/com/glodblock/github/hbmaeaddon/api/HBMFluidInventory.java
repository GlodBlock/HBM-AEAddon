package com.glodblock.github.hbmaeaddon.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.glodblock.github.hbmaeaddon.util.HBMFluidBridge;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;

import appeng.util.Platform;

public class HBMFluidInventory implements IFluidHandler {

    private final static String TANK_TAG = "tk";
    private final FluidTank[] fluids;
    private final int capacity;
    private final IHBMFluidInventoryHost handler;
    public int lastIndex;

    public HBMFluidInventory(IHBMFluidInventoryHost handler, int slots, int capacity) {
        this.fluids = new FluidTank[slots];
        this.handler = handler;
        this.capacity = capacity;
        this.init();
    }

    public HBMFluidInventory(IHBMFluidInventoryHost handler, int slots) {
        this(handler, slots, Integer.MAX_VALUE);
    }

    public HBMFluidInventory(int slots) {
        this(null, slots, Integer.MAX_VALUE);
    }

    private void init() {
        for (int x = 0; x < this.size(); x++) {
            final int xx = x;
            this.fluids[x] = new CallBackTank(Fluids.NONE, this.capacity, () -> onChange(xx));
        }
    }

    public void loadFromNBT(NBTTagCompound data, String name) {
        var c = data.getCompoundTag(name);
        if (c != null) {
            for (int x = 0; x < this.size(); x++) {
                var sub = c.getCompoundTag("#" + x);
                if (sub != null) {
                    this.fluids[x].readFromNBT(sub, TANK_TAG);
                }
            }
        }
    }

    public void saveToNBT(NBTTagCompound data, String name) {
        var tag = new NBTTagCompound();
        for (int x = 0; x < this.size(); x++) {
            var sub = new NBTTagCompound();
            this.fluids[x].writeToNBT(sub, TANK_TAG);
            tag.setTag("#" + x, sub);
        }
        data.setTag(name, tag);
    }

    public FluidTank[] asArray() {
        return this.fluids;
    }

    public FluidTank getTank(int slot) {
        return this.fluids[slot];
    }

    public int fill(int slot, FluidStack resource, boolean doFill) {
        if (resource == null || resource.fill <= 0) {
            return 0;
        }
        var tank = this.fluids[slot];
        var type = tank.getTankType();
        if (type != Fluids.NONE && type != resource.type) {
            return 0;
        }
        int amountToStore = this.capacity;
        if (type != Fluids.NONE) {
            amountToStore -= tank.getFill();
        }
        amountToStore = Math.min(amountToStore, resource.fill);
        if (doFill) {
            if (type == Fluids.NONE) {
                this.setFluidStack(slot, resource);
            } else {
                tank.setFill(tank.getFill() + amountToStore);
                this.onChange(slot);
            }
        }
        return amountToStore;
    }

    public FluidStack drain(int slot, FluidStack resource, boolean doDrain) {
        var type = this.fluids[slot].getTankType();
        if (type == Fluids.NONE || resource == null || type != resource.type) {
            return null;
        }
        return this.drain(slot, resource.fill, doDrain);
    }

    public FluidStack drain(int slot, int maxDrain, boolean doDrain) {
        var tank = this.fluids[slot];
        var type = tank.getTankType();
        if (type == Fluids.NONE || maxDrain <= 0) {
            return null;
        }
        int drained = maxDrain;
        if (tank.getFill() < drained) {
            drained = tank.getFill();
        }

        var stack = new FluidStack(type, drained);
        if (doDrain) {
            tank.setFill(tank.getFill() - drained);
            if (tank.getFill() <= 0) {
                tank.setTankType(Fluids.NONE);
                tank.setFill(0);
            }
            this.onChange(slot);
        }
        return stack;
    }

    public FluidType getFluidType(int slot) {
        return this.fluids[slot].getTankType();
    }

    public FluidStack getFluidStack(int slot) {
        var tank = this.fluids[slot];
        if (tank.getTankType() == Fluids.NONE || tank.getFill() <= 0) {
            return null;
        } else {
            return new FluidStack(tank.getTankType(), tank.getFill());
        }
    }

    public void setFluidStack(int slot, FluidStack fluid) {
        var tank = this.fluids[slot];
        if (fluid == null) {
            tank.setTankType(null);
            tank.setFill(0);
            return;
        }
        tank.setTankType(fluid.type);
        tank.setFill(fluid.fill);
    }

    public int size() {
        return this.fluids.length;
    }

    public void onChange(int slot) {
        if (this.handler != null && Platform.isServer()) {
            this.handler.onInventoryChange(this, slot);
        }
    }

    @Override
    public int fill(ForgeDirection from, net.minecraftforge.fluids.FluidStack fluid, boolean doFill) {
        if (fluid == null || fluid.amount <= 0) {
            return 0;
        }
        var insert = fluid.copy();
        int totalFillAmount = 0;
        for (int slot = 0; slot < this.size(); ++slot) {
            int fillAmount = this.fill(slot, HBMFluidBridge.get(insert), doFill);
            totalFillAmount += fillAmount;
            insert.amount -= fillAmount;
            if (insert.amount <= 0) {
                break;
            }
        }
        return totalFillAmount;
    }

    @Override
    public net.minecraftforge.fluids.FluidStack drain(ForgeDirection from, net.minecraftforge.fluids.FluidStack fluid,
            boolean doDrain) {
        if (fluid == null || fluid.amount <= 0) {
            return null;
        }

        var resource = fluid.copy();

        net.minecraftforge.fluids.FluidStack totalDrained = null;
        for (int slot = 0; slot < this.size(); ++slot) {
            if (fluid.getFluid() != HBMFluidBridge.get(this.getFluidType(slot))) {
                continue;
            }
            var drain = this.drain(slot, HBMFluidBridge.get(resource), doDrain);
            if (drain != null) {
                if (totalDrained == null) {
                    totalDrained = HBMFluidBridge.get2(drain);
                } else {
                    totalDrained.amount += drain.fill;
                }

                resource.amount -= drain.fill;
                if (resource.amount <= 0) {
                    break;
                }
            }
        }
        return totalDrained;
    }

    @Override
    public net.minecraftforge.fluids.FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (maxDrain == 0) {
            return null;
        }
        net.minecraftforge.fluids.FluidStack totalDrained = null;
        int toDrain = maxDrain;
        for (int slot = lastIndex; slot < this.size(); ++slot) {
            if (this.getFluidStack(slot) == null) {
                if (slot == this.size() - 1) lastIndex = 0;
                continue;
            }
            if (totalDrained == null) {
                totalDrained = HBMFluidBridge.get2(this.drain(slot, toDrain, doDrain));
                if (totalDrained != null) {
                    lastIndex = slot;
                    toDrain -= totalDrained.amount;
                }
            } else {
                var copy = totalDrained.copy();
                copy.amount = toDrain;
                var drain = this.drain(slot, HBMFluidBridge.get(copy), doDrain);
                if (drain != null) {
                    totalDrained.amount += drain.fill;
                    toDrain -= drain.fill;
                }
            }
            if (toDrain <= 0) {
                break;
            }
        }
        return totalDrained;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fill(from, new net.minecraftforge.fluids.FluidStack(fluid, 1), false) == 1;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return drain(from, new net.minecraftforge.fluids.FluidStack(fluid, 1), false) != null;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[0];
    }

    private static class CallBackTank extends FluidTank {

        final Runnable callback;

        public CallBackTank(FluidType type, int maxFluid, Runnable callback) {
            super(type, maxFluid);
            this.callback = callback;
        }

        @Override
        public void setFill(int i) {
            boolean needUpdate = this.getFill() != i;
            super.setFill(i);
            if (needUpdate) {
                this.callback.run();
            }
        }

        @Override
        public void setTankType(FluidType type) {
            boolean needUpdate = this.getTankType() != type;
            super.setTankType(type);
            if (needUpdate) {
                this.callback.run();
            }
        }

        @Override
        public int changeTankSize(int size) {
            boolean needUpdate = this.getMaxFill() != size;
            int res = super.changeTankSize(size);
            if (needUpdate) {
                this.callback.run();
            }
            return res;
        }

    }

}
