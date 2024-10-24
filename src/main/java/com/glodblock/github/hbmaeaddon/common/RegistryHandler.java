package com.glodblock.github.hbmaeaddon.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;

import org.apache.commons.lang3.tuple.Pair;

import com.glodblock.github.hbmaeaddon.HBMAEAddon;
import com.glodblock.github.hbmaeaddon.common.fluid.FluidHBM;
import com.glodblock.github.hbmaeaddon.common.fluid.FluidHE;
import com.glodblock.github.hbmaeaddon.util.DataUtil;
import com.glodblock.github.hbmaeaddon.util.Tabs;

import appeng.block.AEBaseItemBlock;
import cpw.mods.fml.common.registry.GameRegistry;

public class RegistryHandler {

    private final List<Pair<Item, String>> items = new ArrayList<>();
    private final List<Pair<Block, String>> blocks = new ArrayList<>();
    private final List<Pair<Class<? extends TileEntity>, String>> tiles = new ArrayList<>();

    public static final RegistryHandler INSTANCE = new RegistryHandler();

    private RegistryHandler() {
        // NO-OP
    }

    public void item(Item item, String id) {
        this.items.add(DataUtil.pair(item, id));
    }

    public void block(Block block, String id) {
        this.blocks.add(DataUtil.pair(block, id));
    }

    public void block(Block block, Class<? extends TileEntity> tile, String id) {
        this.blocks.add(DataUtil.pair(block, id));
        this.tiles.add(DataUtil.pair(tile, id));
    }

    public void register() {
        this.items.forEach(p -> GameRegistry.registerItem(p.getKey(), p.getValue(), HBMAEAddon.MODID));
        this.blocks.forEach(p -> GameRegistry.registerBlock(p.getKey(), AEBaseItemBlock.class, p.getValue()));
        this.tiles.forEach(p -> GameRegistry.registerTileEntity(p.getKey(), p.getValue()));
        this.items.forEach(p -> p.getKey().setCreativeTab(Tabs.MAIN));
        this.blocks.forEach(p -> p.getKey().setCreativeTab(Tabs.MAIN));
        FluidHBM.init();
        FluidHE.init();
    }

}
