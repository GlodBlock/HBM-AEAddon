package com.glodblock.github.hbmaeaddon.client.icon;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;

import com.glodblock.github.hbmaeaddon.HBMAEAddon;

public enum ExtraIcons {

    HE_LIQUID("fluids/liquid_he_unit");

    private final String resource;
    private IIcon icon;

    ExtraIcons(String resource) {
        this.resource = HBMAEAddon.resource(resource).toString();
    }

    public void register(TextureMap map) {
        this.icon = map.registerIcon(this.resource);
    }

    public IIcon getIcon() {
        return this.icon;
    }

}
