package com.whiteboxteam.gliese.data.entity;

import android.graphics.Bitmap;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 13.03.2015
 * Time: 14:56
 */
public class ValueEntity {

    public long id;
    public long categoryId;
    public String name;
    public String remoteThumbUri;
    public String localThumbUri;
    public Bitmap thumb;
    public int oldPrice;
    public int discount;
    public int newPrice;
    public String url;

}
