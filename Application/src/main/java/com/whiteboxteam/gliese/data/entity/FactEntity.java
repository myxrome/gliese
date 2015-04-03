package com.whiteboxteam.gliese.data.entity;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 19.03.2015
 * Time: 12:05
 */
public class FactEntity {

    public long id;

    public static FactEntity newInstance(long id) {
        FactEntity result = new FactEntity();
        result.id = id;
        return result;
    }

}
