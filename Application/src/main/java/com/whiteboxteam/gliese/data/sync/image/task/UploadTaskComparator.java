package com.whiteboxteam.gliese.data.sync.image.task;

import java.util.Comparator;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 29.01.2015
 * Time: 17:36
 */
public final class UploadTaskComparator implements Comparator<Runnable> {

    @Override
    public int compare(Runnable lhs, Runnable rhs) {
        if (lhs == null && rhs == null) {
            return 0;
        }
        if (lhs == null) {
            return -1;
        }
        if (rhs == null) {
            return 1;
        }
        return ((BaseUploadTask) lhs).getPriority() - ((BaseUploadTask) rhs).getPriority();
    }

}
