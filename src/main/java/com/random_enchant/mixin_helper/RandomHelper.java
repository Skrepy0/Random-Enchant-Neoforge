package com.random_enchant.mixin_helper;

import java.util.Random;

public class RandomHelper {
    private RandomHelper() {}

    public static boolean random(float probability) {
        Random r = new Random();
        return r.nextFloat() < probability;
    }
}
