package com.random_enchant.mixin_helper;

public class CuriosHelper {
    private static final boolean LOADED = isCuriosLoadedInternal();

    private static boolean isCuriosLoadedInternal() {
        try {
            Class.forName("top.theillusivec4.curios.api.CuriosApi");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean isCuriosLoaded() { return LOADED; }
}
