package com.glodblock.github.hbmaeaddon.util;

import java.util.IdentityHashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;

import appeng.api.storage.data.IAEFluidStack;
import appeng.util.item.AEFluidStack;

public final class HBMFluidBridge {

    private static final Map<FluidType, Fluid> MAP = new IdentityHashMap<>();
    private static final Map<Fluid, FluidType> RE_MAP = new IdentityHashMap<>();
    private static boolean freeze = false;

    public static void addMap(FluidType type, Fluid fluid) {
        if (freeze) {
            return;
        }
        MAP.put(type, fluid);
    }

    public static void freeze() {
        freeze = true;
        for (var e : MAP.entrySet()) {
            RE_MAP.put(e.getValue(), e.getKey());
        }
    }

    @Nullable
    public static Fluid get(FluidType type) {
        return MAP.get(type);
    }

    @Nonnull
    public static FluidType get(Fluid fluid) {
        return RE_MAP.getOrDefault(fluid, Fluids.NONE);
    }

    public static IAEFluidStack get(com.hbm.inventory.FluidStack stack) {
        var fluid = get(stack.type);
        if (fluid == null) {
            return null;
        }
        return AEFluidStack.create(new FluidStack(fluid, stack.fill));
    }

    public static FluidStack get2(com.hbm.inventory.FluidStack stack) {
        var fluid = get(stack.type);
        if (fluid == null) {
            return null;
        }
        return new FluidStack(fluid, stack.fill);
    }

    public static com.hbm.inventory.FluidStack get(IAEFluidStack stack) {
        var type = get(stack.getFluid());
        if (type == Fluids.NONE) {
            return null;
        }
        return new com.hbm.inventory.FluidStack(type, (int) stack.getStackSize());
    }

    public static com.hbm.inventory.FluidStack get(FluidStack stack) {
        var type = get(stack.getFluid());
        if (type == Fluids.NONE) {
            return null;
        }
        return new com.hbm.inventory.FluidStack(type, stack.amount);
    }

}
