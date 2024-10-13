package com.glodblock.github.hbmaeaddon.api;

public enum GuiMode {

    TILE,
    PART;

    public static GuiMode decode(int num) {
        return values()[num & 0b11];
    }

}
