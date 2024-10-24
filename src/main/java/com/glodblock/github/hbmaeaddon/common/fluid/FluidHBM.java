package com.glodblock.github.hbmaeaddon.common.fluid;

import java.util.Locale;

import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.glodblock.github.hbmaeaddon.client.icon.IconFluidHBM;
import com.glodblock.github.hbmaeaddon.util.HBMFluidBridge;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.trait.FluidTrait;

public class FluidHBM extends Fluid {

    private final FluidType type;
    private static Class<? extends FluidTrait> GAS_TYPE;
    private static boolean GAS_TYPE_INIT = false;

    public static void init() {
        for (var fluid : Fluids.getAll()) {
            if (fluid == Fluids.NONE) {
                continue;
            }
            var forgeFluid = new FluidHBM(fluid);
            var possibleName = fluid.getName().toLowerCase(Locale.US) + "_fluid";
            if (FluidRegistry.isFluidRegistered(possibleName)) {
                HBMFluidBridge.addMap(fluid, FluidRegistry.getFluid(possibleName));
            } else if (FluidRegistry.registerFluid(forgeFluid)) {
                HBMFluidBridge.addMap(fluid, forgeFluid);
            } else {
                HBMFluidBridge.addMap(fluid, FluidRegistry.getFluid(forgeFluid.fluidName));
            }
        }
    }

    public FluidHBM(FluidType hbmFluid) {
        super(hbmFluid.getName());
        this.type = hbmFluid;
        this.unlocalizedName = hbmFluid.getUnlocalizedName();
        this.setTemperature(hbmFluid.temperature + 273);
        this.trySetGas();
    }

    @Override
    public int getColor() {
        return this.type.getColor();
    }

    @Override
    public IIcon getStillIcon() {
        return IconFluidHBM.getIcon(this.type);
    }

    @Override
    public IIcon getFlowingIcon() {
        return IconFluidHBM.getIcon(this.type);
    }

    @Override
    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    @SuppressWarnings("unchecked")
    private void trySetGas() {
        if (!GAS_TYPE_INIT) {
            try {
                GAS_TYPE = (Class<? extends FluidTrait>) Class
                        .forName("com.hbm.inventory.fluid.trait.FluidTraitSimple$FT_Gaseous");
            } catch (Exception e) {
                try {
                    GAS_TYPE = (Class<? extends FluidTrait>) Class.forName("com.hbm.inventory.fluid.trait.FT_Gaseous");
                } catch (Exception ignored) {
                    GAS_TYPE = null;
                }
            }
            GAS_TYPE_INIT = true;
        }
        if (GAS_TYPE != null) {
            this.setGaseous(this.type.hasTrait(GAS_TYPE));
        }
    }

}
