package com.glodblock.github.hbmaeaddon.mixins;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

@LateMixin
public class HBMLatePlugin implements ILateMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.hbmaeaddon.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        return Lists.newArrayList("MixinOptionalFluidSlotFakeTypeOnly");
    }

}
