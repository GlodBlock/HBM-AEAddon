package com.glodblock.github.hbmaeaddon.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.glodblock.github.hbmaeaddon.HBMAEAddon;
import com.glodblock.github.hbmaeaddon.common.BlockAndItems;

public final class Tabs {

    public static CreativeTabs MAIN = new CreativeTabs(HBMAEAddon.MODID) {

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(BlockAndItems.HBM_EXPOSER);
        }
    };

}
