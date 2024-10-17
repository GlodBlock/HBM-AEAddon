package com.glodblock.github.hbmaeaddon.proxy;

import com.glodblock.github.hbmaeaddon.HBMAEAddon;
import com.glodblock.github.hbmaeaddon.common.BlockAndItems;
import com.glodblock.github.hbmaeaddon.common.Recipes;
import com.glodblock.github.hbmaeaddon.common.RegistryHandler;
import com.glodblock.github.hbmaeaddon.network.Guis;
import com.glodblock.github.hbmaeaddon.network.Packets;
import com.glodblock.github.hbmaeaddon.network.gui.HBMGuiHandler;
import com.glodblock.github.hbmaeaddon.util.HBMFluidBridge;
import com.glodblock.github.network.wrapper.FCNetworkWrapper;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public final FCNetworkWrapper netHandler = new FCNetworkWrapper(HBMAEAddon.MODID);

    public void preInit(FMLPreInitializationEvent event) {
        Guis.ensureLoad();
        Packets.init();
        BlockAndItems.init(RegistryHandler.INSTANCE);
        RegistryHandler.INSTANCE.register();
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(HBMAEAddon.INSTANCE, HBMGuiHandler.INSTANCE);
        Recipes.init();
    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        HBMFluidBridge.freeze();
    }

}
