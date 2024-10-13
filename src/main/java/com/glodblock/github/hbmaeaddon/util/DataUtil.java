package com.glodblock.github.hbmaeaddon.util;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public final class DataUtil {

    public static <L, R> Pair<L, R> pair(L a, R b) {
        return ImmutablePair.of(a, b);
    }

}
