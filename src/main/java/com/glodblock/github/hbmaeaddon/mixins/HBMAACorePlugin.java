package com.glodblock.github.hbmaeaddon.mixins;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions({ "com.glodblock.github.hbmaeaddon.mixins" })
@IFMLLoadingPlugin.Name("HBM AEAddon core plugin")
public class HBMAACorePlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {

    private static boolean DEV_ENVIRONMENT;

    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        DEV_ENVIRONMENT = !(boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public String getMixinConfig() {
        return "mixins.hbmaeaddon.early.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedCoreMods) {
        return Lists.newArrayList("MixinTextureMap");
    }

    public static boolean isDevEnv() {
        return DEV_ENVIRONMENT;
    }

}
