package com.glodblock.github.hbmaeaddon.util;

import net.minecraft.item.ItemStack;

import com.glodblock.github.util.Util;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.ItemFluidIcon;
import com.hbm.items.machine.ItemInfiniteFluid;

import api.hbm.fluid.IFillableItem;

public class HBMUtil {

    public static FluidStack copyNoPressure(FluidStack fluid, int amount) {
        if (fluid == null) {
            return null;
        }
        return new FluidStack(fluid.type, amount);
    }

    public static FluidStack copyNoPressure(FluidStack fluid) {
        if (fluid == null) {
            return null;
        }
        return new FluidStack(fluid.type, fluid.fill);
    }

    public static FluidStack copy(FluidStack fluid) {
        if (fluid == null) {
            return null;
        }
        return new FluidStack(fluid.type, fluid.fill, fluid.pressure);
    }

    public static boolean equals(FluidStack a, FluidStack b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.type == b.type && a.fill == b.fill && a.pressure == b.pressure;
    }

    public static boolean equalsType(FluidStack a, FluidStack b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.type == b.type;
    }

    public static FluidType getFluidType(Object obj) {
        if (obj == null) {
            return Fluids.NONE;
        } else if (obj instanceof FluidType fluid) {
            return fluid;
        } else if (obj instanceof FluidStack fluid) {
            return fluid.type;
        } else if (obj instanceof net.minecraftforge.fluids.FluidStack fluid) {
            return HBMFluidBridge.get(fluid.getFluid());
        } else if (obj instanceof ItemStack stack) {
            var item = stack.getItem();
            if (item instanceof IFillableItem handler) {
                return handler.getFirstFluidType(stack);
            } else if (item instanceof ItemInfiniteFluid handler) {
                return handler.getType();
            } else if (item instanceof IItemFluidIdentifier id) {
                return id.getType(null, 0, 0, 0, stack);
            } else if (item instanceof ItemFluidIcon) {
                return Fluids.fromID(stack.getItemDamage());
            } else {
                var content = FluidContainerRegistry.getFluidType(stack);
                if (content != Fluids.NONE) {
                    return content;
                }
                var forgeFluid = Util.getFluidFromItem(stack);
                if (forgeFluid != null) {
                    return HBMFluidBridge.get(forgeFluid.getFluid());
                }
            }
        }
        return Fluids.NONE;
    }

}
