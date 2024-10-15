package com.glodblock.github.hbmaeaddon.mixins.mixin;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.glodblock.github.hbmaeaddon.util.HBMFluidBridge;
import com.glodblock.github.hbmaeaddon.util.HBMUtil;
import com.glodblock.github.inventory.slot.OptionalFluidSlotFakeTypeOnly;
import com.glodblock.github.util.Util;

@Mixin(OptionalFluidSlotFakeTypeOnly.class)
public class MixinOptionalFluidSlotFakeTypeOnly {

    @Redirect(
            method = "putStack",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/glodblock/github/util/Util;getFluidFromItem(Lnet/minecraft/item/ItemStack;)Lnet/minecraftforge/fluids/FluidStack;",
                    remap = false))
    private FluidStack checkHBMFluid(ItemStack stack) {
        var fluidStack = Util.getFluidFromItem(stack);
        if (fluidStack != null) {
            return fluidStack;
        }
        var hbmFluid = HBMFluidBridge.get(HBMUtil.getFluidType(stack));
        if (hbmFluid != null) {
            return new FluidStack(hbmFluid, 1);
        }
        return null;
    }

}
