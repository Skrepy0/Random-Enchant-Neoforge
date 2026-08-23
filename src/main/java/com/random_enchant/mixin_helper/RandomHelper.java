package com.random_enchant.mixin_helper;

import java.util.concurrent.ThreadLocalRandom;

public class RandomHelper {
    private RandomHelper() {}

    public static boolean random(float probability) { return ThreadLocalRandom.current().nextFloat() < probability; }
}
