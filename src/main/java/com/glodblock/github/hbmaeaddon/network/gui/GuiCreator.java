package com.glodblock.github.hbmaeaddon.network.gui;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.glodblock.github.hbmaeaddon.api.GuiMode;

import appeng.api.parts.IPartHost;
import appeng.container.AEBaseContainer;
import appeng.container.ContainerOpenContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class GuiCreator<T> {

    private final Class<T> type;
    private final int id;
    private static int nextID = 0;
    private static final Map<Integer, GuiCreator<?>> ID_MAP = new HashMap<>();

    public GuiCreator(Class<T> type) {
        this.type = type;
        this.id = nextID++;
        ID_MAP.put(this.id, this);
    }

    T castInventory(Object inv) {
        return type.isInstance(inv) ? type.cast(inv) : null;
    }

    public abstract Object createServerGui(EntityPlayer player, T inventory);

    public abstract Object createClientGui(EntityPlayer player, T inventory);

    public int getID() {
        return this.id;
    }

    @SuppressWarnings("unchecked")
    public static <T> GuiCreator<T> getGui(int id) {
        return (GuiCreator<T>) ID_MAP.get(id);
    }

    private T getInventory(GuiMode mode, World world, int x, int y, int z, ForgeDirection face) {
        var tile = world.getTileEntity(x, y, z);
        if (mode == GuiMode.TILE) {
            return castInventory(tile);
        } else if (mode == GuiMode.PART) {
            if (tile instanceof IPartHost host) {
                return castInventory(host.getPart(face));
            }
        }
        return null;
    }

    @Nullable
    Object createServerGui(GuiMode mode, EntityPlayer player, World world, int x, int y, int z, ForgeDirection face) {
        T inv = getInventory(mode, world, x, y, z, face);
        if (inv == null) {
            return null;
        }
        var gui = createServerGui(player, inv);
        if (gui instanceof AEBaseContainer) {
            ContainerOpenContext ctx = new ContainerOpenContext(inv);
            ctx.setWorld(world);
            ctx.setX(x);
            ctx.setY(y);
            ctx.setZ(z);
            ctx.setSide(face);
            ((AEBaseContainer) gui).setOpenContext(ctx);
        }
        return gui;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    Object createClientGui(GuiMode mode, EntityPlayer player, World world, int x, int y, int z, ForgeDirection face) {
        T inv = getInventory(mode, world, x, y, z, face);
        if (inv == null) {
            return null;
        }
        return createClientGui(player, inv);
    }

}
