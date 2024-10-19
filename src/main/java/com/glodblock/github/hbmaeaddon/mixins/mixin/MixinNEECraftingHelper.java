package com.glodblock.github.hbmaeaddon.mixins.mixin;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiScreenEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.github.vfyjxf.nee.config.NEEConfig;
import com.github.vfyjxf.nee.nei.NEECraftingHelper;
import com.github.vfyjxf.nee.utils.GuiUtils;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.recipe.GuiRecipe;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Mixin(NEECraftingHelper.class)
public abstract class MixinNEECraftingHelper {

    /**
     * @author x
     * @reason x
     */
    @SubscribeEvent
    @Overwrite(remap = false)
    public void onActionPerformedEventPre(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (NEEConfig.noShift) {
            if (event.gui instanceof GuiRecipe<?>guiRecipe) {
                final int OVERLAY_BUTTON_ID_START = 4;
                boolean isGtnhNei = true;
                try {
                    var overlayButtonsField = GuiRecipe.class.getDeclaredField("overlayButtons");
                    overlayButtonsField.setAccessible(true);
                } catch (NoSuchFieldException e) {
                    isGtnhNei = false;
                }
                if (event.button.id >= OVERLAY_BUTTON_ID_START) {
                    boolean isPatternTerm = GuiUtils.isPatternTerm(guiRecipe.firstGui);
                    boolean isCraftingTerm = GuiUtils.isGuiCraftingTerm(guiRecipe.firstGui);
                    if (isCraftingTerm || isPatternTerm) {
                        var handler = guiRecipe.currenthandlers.get(guiRecipe.recipetype);
                        int recipeIndex;
                        if (isGtnhNei) {
                            recipeIndex = event.button.id - OVERLAY_BUTTON_ID_START;
                        } else {
                            int recipesPerPage = 2;
                            recipeIndex = guiRecipe.page * recipesPerPage + event.button.id - OVERLAY_BUTTON_ID_START;
                        }

                        if (handler != null && recipeIndex >= 0 && recipeIndex < handler.numRecipes()) {
                            var overlayHandler = handler.getOverlayHandler(guiRecipe.firstGui, recipeIndex);
                            Minecraft.getMinecraft().displayGuiScreen(guiRecipe.firstGui);
                            overlayHandler.overlayRecipe(
                                    guiRecipe.firstGui,
                                    guiRecipe.currenthandlers.get(guiRecipe.recipetype),
                                    recipeIndex,
                                    NEIClientUtils.shiftKey());
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

}
