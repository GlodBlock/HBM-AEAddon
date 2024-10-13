package com.glodblock.github.hbmaeaddon.mixins.mixin;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextureMap.class)
public class MixinTextureMap {

    @Inject(method = "completeResourceLocation", at = @At(value = "HEAD"), cancellable = true)
    private void redirectHBMResource(ResourceLocation raw, int type,
            CallbackInfoReturnable<ResourceLocation> callbackInfo) {
        var rawS = raw.toString();
        if (rawS.startsWith("hbm:textures/gui/fluids/") && type == 0) {
            callbackInfo.setReturnValue(raw);
            callbackInfo.cancel();
        }
    }

}
