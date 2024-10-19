package com.glodblock.github.hbmaeaddon.mixins.mixin;

import java.util.HashMap;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.glodblock.github.nei.object.IRecipeExtractor;
import com.glodblock.github.nei.object.OrderStack;
import com.glodblock.github.nei.recipes.FluidRecipe;

import codechicken.nei.recipe.IRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;

@Mixin(FluidRecipe.class)
public class MixinFluidRecipe {

    @Shadow(remap = false)
    @Final
    private static HashMap<String, IRecipeExtractor> IdentifierMapLegacy;

    @Shadow(remap = false)
    private static List<OrderStack<?>> getDefaultPackageInputs(TemplateRecipeHandler tRecipe, int index) {
        return null;
    }

    @Shadow(remap = false)
    private static List<OrderStack<?>> getDefaultPackageOutputs(TemplateRecipeHandler tRecipe, int index) {
        return null;
    }

    @Inject(method = "getPackageInputsLegacy", at = @At("HEAD"), remap = false, cancellable = true)
    private static void addDefault(IRecipeHandler recipe, int index, CallbackInfoReturnable<List<OrderStack<?>>> cir) {
        if (recipe != null && !IdentifierMapLegacy.containsKey(recipe.getClass().getName())) {
            cir.setReturnValue(getDefaultPackageInputs((TemplateRecipeHandler) recipe, index));
            cir.cancel();
        }
    }

    @Inject(method = "getPackageOutputsLegacy", at = @At("HEAD"), remap = false, cancellable = true)
    private static void addDefault2(IRecipeHandler recipe, int index, boolean useOther,
            CallbackInfoReturnable<List<OrderStack<?>>> cir) {
        if (recipe != null && !IdentifierMapLegacy.containsKey(recipe.getClass().getName())) {
            cir.setReturnValue(getDefaultPackageOutputs((TemplateRecipeHandler) recipe, index));
            cir.cancel();
        }
    }

}
