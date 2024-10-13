package com.glodblock.github.hbmaeaddon;

import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.Logger;

import com.glodblock.github.hbmaeaddon.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = HBMAEAddon.MODID,
        version = HBMAEAddon.VERSION,
        name = HBMAEAddon.MODNAME,
        dependencies = "required-after:appliedenergistics2@[rv3-beta-238,);required-after:hbm;required-after:ae2fc")
public class HBMAEAddon {

    public static final String MODID = "GRADLETOKEN_MODID";
    public static final String VERSION = "1.0";
    public static final String MODNAME = "GRADLETOKEN_MODNAME";

    @Mod.Instance(MODID)
    public static HBMAEAddon INSTANCE;

    @SidedProxy(
            clientSide = "com.glodblock.github.hbmaeaddon.proxy.ClientProxy",
            serverSide = "com.glodblock.github.hbmaeaddon.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static Logger log;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        log = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        proxy.onLoadComplete(event);
    }

    public static ResourceLocation resource(String path) {
        return new ResourceLocation(MODID, path);
    }

}
