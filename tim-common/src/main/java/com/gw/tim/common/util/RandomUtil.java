package com.gw.tim.common.util;

import java.util.Random;
import java.util.UUID;

/**
 *
 * @since JDK 1.8
 */
public class RandomUtil {

    public static int getRandom() {

        double random = Math.random();
        return (int) (random * 100);
    }
}
