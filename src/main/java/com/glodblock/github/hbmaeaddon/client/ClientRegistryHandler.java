package com.glodblock.github.hbmaeaddon.client;

import java.util.Arrays;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;

import com.glodblock.github.hbmaeaddon.client.icon.ExtraIcons;
import com.glodblock.github.hbmaeaddon.client.icon.IconFluidHBM;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ClientRegistryHandler {

    public static final ClientRegistryHandler INSTANCE = new ClientRegistryHandler();

    private ClientRegistryHandler() {
        // NO-OP
    }

    @SubscribeEvent
    public void updateTextureSheet(final TextureStitchEvent.Pre ev) {
        if (ev.map.getTextureType() == 0) {
            IconFluidHBM.getAll().forEach(p -> p.register(ev.map));
            Arrays.stream(ExtraIcons.values()).forEach(p -> p.register(ev.map));
        }
    }

    public void register() {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
        FMLCommonHandler.instance().bus().register(INSTANCE);
    }

}
