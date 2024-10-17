package com.glodblock.github.hbmaeaddon.nei.extractor;

import java.util.ArrayList;
import java.util.List;

import com.glodblock.github.nei.object.IRecipeExtractorLegacy;
import com.glodblock.github.nei.object.OrderStack;
import com.glodblock.github.nei.recipes.extractor.ExtractorUtil;
import com.hbm.handler.nei.ChemplantRecipeHandler;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.IRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class ChemplantExtractor implements IRecipeExtractorLegacy {

    @Override
    public List<OrderStack<?>> getInputIngredients(List<PositionedStack> rawInputs) {
        return ExtractorUtil.packItemStack(rawInputs);
    }

    @Override
    public List<OrderStack<?>> getOutputIngredients(List<PositionedStack> rawOutputs) {
        return ExtractorUtil.packItemStack(rawOutputs);
    }

    @Override
    public List<OrderStack<?>> getInputIngredients(List<PositionedStack> rawInputs, IRecipeHandler recipe, int index) {
        TemplateRecipeHandler tRecipe = (TemplateRecipeHandler) recipe;
        List<OrderStack<?>> tmp = new ArrayList<>();
        if (tRecipe.arecipes.get(index) instanceof ChemplantRecipeHandler.RecipeSet recipeSet) {
            tmp = getInputIngredients(recipeSet.getIngredients());
        }
        return tmp;
    }

    @Override
    public List<OrderStack<?>> getOutputIngredients(List<PositionedStack> rawOutputs, IRecipeHandler recipe,
            int index) {
        TemplateRecipeHandler tRecipe = (TemplateRecipeHandler) recipe;
        List<OrderStack<?>> tmp = new ArrayList<>();
        if (tRecipe.arecipes.get(index) instanceof ChemplantRecipeHandler.RecipeSet recipeSet) {
            tmp = getOutputIngredients(recipeSet.getOtherStacks());
        }
        return tmp;
    }

    @Override
    public String getClassName() {
        return ChemplantRecipeHandler.class.getName();
    }

}
