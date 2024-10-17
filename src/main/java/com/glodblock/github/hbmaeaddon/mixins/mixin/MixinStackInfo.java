package com.glodblock.github.hbmaeaddon.mixins.mixin;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import codechicken.nei.recipe.StackInfo;

@Mixin(StackInfo.class)
public class MixinStackInfo {

    @Inject(method = "equalItemAndNBT", at = @At("HEAD"), remap = false, cancellable = true)
    private static void checkNull(ItemStack stackA, ItemStack stackB, boolean useNBT,
            CallbackInfoReturnable<Boolean> cir) {
        if (stackA == stackB) {
            cir.setReturnValue(true);
            cir.cancel();
        }
        if (stackA == null || stackB == null) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

}
