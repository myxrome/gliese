package com.whiteboxteam.gliese.common;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.util.Random;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 23.03.2015
 * Time: 17:11
 */
public final class StringHelper {

    public static String randomString(int length) {
        int r = new Random(System.currentTimeMillis()).nextInt(Integer.MAX_VALUE);
        HashFunction hashFunction = Hashing.md5();
        HashCode hash = hashFunction.newHasher().putInt(r).hash();
        return hash.toString().substring(0, length).toUpperCase();
    }

}
