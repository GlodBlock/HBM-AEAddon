package com.glodblock.github.hbmaeaddon.common.fluid;

import com.glodblock.github.hbmaeaddon.client.icon.ExtraIcons;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidHE extends Fluid {

    public static Fluid HE;

    public static void init() {
        HE = register(new FluidHE("liquid_he_unit", ExtraIcons.HE_LIQUID).setLuminosity(15).setTemperature(1));
    }

    private final ExtraIcons icon;

    public FluidHE(String name, ExtraIcons icon) {
        super(name);
        this.icon = icon;
    }

    @Override
    public IIcon getStillIcon() {
        return this.icon.getIcon();
    }

    @Override
    public IIcon getFlowingIcon() {
        return this.icon.getIcon();
    }

    private static Fluid register(Fluid fluid) {
        var name = fluid.getName();
        if (!FluidRegistry.isFluidRegistered(name)) {
            FluidRegistry.registerFluid(fluid);
            return fluid;
        } else {
            return FluidRegistry.getFluid(name);
        }
    }

}
