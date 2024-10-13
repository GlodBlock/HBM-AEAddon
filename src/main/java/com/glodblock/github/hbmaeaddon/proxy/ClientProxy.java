package com.glodblock.github.hbmaeaddon.proxy;

import com.glodblock.github.hbmaeaddon.client.ClientRegistryHandler;
import com.glodblock.github.hbmaeaddon.client.icon.IconFluidHBM;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        IconFluidHBM.collect();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        ClientRegistryHandler.INSTANCE.register();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        super.onLoadComplete(event);
    }

}
