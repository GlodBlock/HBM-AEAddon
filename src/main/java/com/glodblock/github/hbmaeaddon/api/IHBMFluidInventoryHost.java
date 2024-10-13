package com.glodblock.github.hbmaeaddon.api;

public interface IHBMFluidInventoryHost {

    void onInventoryChange(HBMFluidInventory inventory, int slot);

    HBMFluidInventory getHBMFluidInventory();

}
