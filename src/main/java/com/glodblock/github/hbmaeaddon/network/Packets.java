package com.glodblock.github.hbmaeaddon.network;

import com.glodblock.github.hbmaeaddon.HBMAEAddon;
import com.glodblock.github.hbmaeaddon.network.packet.CSHBMSlotUpdate;

import cpw.mods.fml.relauncher.Side;

public class Packets {

    private static int nextID = 0;

    public static void init() {
        HBMAEAddon.proxy.netHandler
                .registerMessage(new CSHBMSlotUpdate.SHandler(), CSHBMSlotUpdate.class, nextID++, Side.SERVER);
        HBMAEAddon.proxy.netHandler
                .registerMessage(new CSHBMSlotUpdate.CHandler(), CSHBMSlotUpdate.class, nextID++, Side.CLIENT);
    }

}
