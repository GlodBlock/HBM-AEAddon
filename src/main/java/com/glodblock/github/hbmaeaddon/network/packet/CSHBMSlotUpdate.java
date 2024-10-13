package com.glodblock.github.hbmaeaddon.network.packet;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;

import com.glodblock.github.hbmaeaddon.HBMAEAddon;
import com.glodblock.github.hbmaeaddon.api.IHBMFluidSyncContainer;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.Fluids;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class CSHBMSlotUpdate implements IMessage {

    protected Map<Integer, FluidStack> list;

    public CSHBMSlotUpdate() {
        // NO-OP
    }

    public CSHBMSlotUpdate(Map<Integer, FluidStack> list) {
        this.list = list;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.list = new HashMap<>();
        try {
            NBTTagCompound HOLDER = ByteBufUtils.readTag(buf);
            if (HOLDER != null) {
                for (var key : HOLDER.func_150296_c()) {
                    int id = Integer.parseInt((String) key);
                    var tag = HOLDER.getCompoundTag((String) key);
                    if (tag.getInteger("ft") == -1) {
                        this.list.put(id, null);
                    } else {
                        this.list.put(
                                id,
                                new FluidStack(
                                        Fluids.fromID(tag.getInteger("ft")),
                                        tag.getInteger("fa"),
                                        tag.getInteger("fp")));
                    }
                }
            }
        } catch (Exception e) {
            HBMAEAddon.log.error("Fail to sync HBM fluid slot, it may cause ghost fluid display");
            HBMAEAddon.log.error(e);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound HOLDER = new NBTTagCompound();
        for (var data : this.list.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            var fluid = data.getValue();
            if (fluid != null) {
                tag.setInteger("ft", fluid.type.getID());
                tag.setInteger("fa", fluid.fill);
                tag.setInteger("fp", fluid.pressure);
            } else {
                tag.setInteger("ft", -1);
            }
            HOLDER.setTag(data.getKey().toString(), tag);
        }
        ByteBufUtils.writeTag(buf, HOLDER);
    }

    public static class SHandler implements IMessageHandler<CSHBMSlotUpdate, IMessage> {

        @Override
        public IMessage onMessage(CSHBMSlotUpdate message, MessageContext ctx) {
            Container container = ctx.getServerHandler().playerEntity.openContainer;
            if (container instanceof IHBMFluidSyncContainer sync) {
                sync.receiveHBMSlots(message.list);
            }
            return null;
        }

    }

    public static class CHandler implements IMessageHandler<CSHBMSlotUpdate, IMessage> {

        @Override
        public IMessage onMessage(CSHBMSlotUpdate message, MessageContext ctx) {
            var player = Minecraft.getMinecraft().thePlayer;
            if (player.openContainer instanceof IHBMFluidSyncContainer sync) {
                sync.receiveHBMSlots(message.list);
            }
            return null;
        }

    }

}
