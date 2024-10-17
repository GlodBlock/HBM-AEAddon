package com.glodblock.github.hbmaeaddon.nei;

import com.glodblock.github.hbmaeaddon.nei.extractor.ChemplantExtractor;
import com.glodblock.github.nei.recipes.FluidRecipe;

public class LoadExtractor {

    public static void load() {
        FluidRecipe.addRecipeMap(null, new ChemplantExtractor());
    }

}
