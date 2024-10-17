package com.glodblock.github.hbmaeaddon.mixins.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.glodblock.github.hbmaeaddon.nei.LoadExtractor;
import com.glodblock.github.nei.recipes.DefaultExtractorLoader;

@Mixin(DefaultExtractorLoader.class)
public class MixinDefaultExtractorLoader {

    @Inject(method = "run", at = @At("HEAD"), remap = false)
    private void addHBMExtractor(CallbackInfo ci) {
        LoadExtractor.load();
    }

}
