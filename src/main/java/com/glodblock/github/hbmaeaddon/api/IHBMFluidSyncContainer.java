package com.glodblock.github.hbmaeaddon.api;

import java.util.Map;

import com.hbm.inventory.FluidStack;

public interface IHBMFluidSyncContainer {

    void receiveHBMSlots(final Map<Integer, FluidStack> fluids);

}
