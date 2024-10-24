package com.glodblock.github.hbmaeaddon.common;

import net.minecraft.item.ItemStack;

import com.glodblock.github.loader.ItemAndBlockHolder;
import com.hbm.items.ModItems;

import appeng.core.Api;
import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {

    public static void init() {
        GameRegistry.addShapedRecipe(
                new ItemStack(BlockAndItems.HBM_EXPOSER),
                "AMA",
                "GIG",
                "AMA",
                'A',
                ModItems.plate_dura_steel,
                'M',
                ModItems.motor,
                'G',
                Api.INSTANCE.definitions().materials().annihilationCore().maybeStack(1).get(),
                'I',
                ItemAndBlockHolder.INTERFACE);
        GameRegistry.addShapedRecipe(
                new ItemStack(BlockAndItems.HBM_ACCEPTOR),
                "AMA",
                "GIG",
                "AMA",
                'A',
                ModItems.plate_dura_steel,
                'M',
                ModItems.motor,
                'G',
                Api.INSTANCE.definitions().materials().formationCore().maybeStack(1).get(),
                'I',
                ItemAndBlockHolder.INTERFACE);
        GameRegistry.addShapedRecipe(
            new ItemStack(BlockAndItems.HE_LIQUEFIER),
            "ZXZ",
            "ZMZ",
            "ZXZ",
            'Z',
            ModItems.plate_schrabidium,
            'X',
            ModItems.powder_co60,
            'M',
            Api.INSTANCE.definitions().blocks().condenser().maybeStack(1).get());
        GameRegistry.addShapedRecipe(
            new ItemStack(BlockAndItems.HE_EXPORT),
            "ZXZ",
            "ZMZ",
            "ZXZ",
            'Z',
            ModItems.plate_saturnite,
            'X',
            ModItems.powder_emerald,
            'M',
            Api.INSTANCE.definitions().blocks().iface().maybeStack(1).get());
    }

}
