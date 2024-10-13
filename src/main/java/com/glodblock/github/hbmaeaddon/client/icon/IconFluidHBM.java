package com.glodblock.github.hbmaeaddon.client.icon;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;

public class IconFluidHBM {

    private final static List<IconFluidHBM> ICONS = new ArrayList<>();
    private final static Map<FluidType, IconFluidHBM> MAP = new IdentityHashMap<>();
    private IIcon icon;
    private final FluidType type;

    public static void collect() {
        for (var fluid : Fluids.getAll()) {
            ICONS.add(new IconFluidHBM(fluid));
        }
    }

    private IconFluidHBM(FluidType type) {
        this.type = type;
        MAP.put(type, this);
    }

    public void register(TextureMap map) {
        this.icon = map.registerIcon(this.type.getTexture().toString());
    }

    public static List<IconFluidHBM> getAll() {
        return ICONS;
    }

    public static IIcon getIcon(FluidType type) {
        if (MAP.containsKey(type)) {
            return MAP.get(type).icon;
        }
        return null;
    }

}
