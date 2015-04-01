package com.whiteboxteam.gliese.data.entity;

import android.graphics.Bitmap;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 13.03.2015
 * Time: 14:56
 */
public class ValueEntity {

    public long   id;
    public long   categoryId;
    public String name;
    public String remoteThumbUri;
    public String localThumbUri;
    public Bitmap thumb;
    public int    oldPrice;
    public int    discount;
    public int    newPrice;
    public String url;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ValueEntity entity = (ValueEntity) o;

        if (categoryId != entity.categoryId) {
            return false;
        }
        if (discount != entity.discount) {
            return false;
        }
        if (id != entity.id) {
            return false;
        }
        if (newPrice != entity.newPrice) {
            return false;
        }
        if (oldPrice != entity.oldPrice) {
            return false;
        }
        if (localThumbUri != null ? !localThumbUri.equals(entity.localThumbUri) : entity.localThumbUri != null) {
            return false;
        }
        if (name != null ? !name.equals(entity.name) : entity.name != null) {
            return false;
        }
        if (remoteThumbUri != null ? !remoteThumbUri.equals(entity.remoteThumbUri) : entity.remoteThumbUri != null) {
            return false;
        }
        return !(url != null ? !url.equals(entity.url) : entity.url != null);

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

}
