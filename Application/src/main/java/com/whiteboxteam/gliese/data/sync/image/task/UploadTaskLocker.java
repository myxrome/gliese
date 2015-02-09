package com.whiteboxteam.gliese.data.sync.image.task;

import java.util.HashSet;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 29.01.2015
 * Time: 18:52
 */
public final class UploadTaskLocker {

    private static final HashSet<String> lockedTasks = new HashSet<>();

    public static boolean lockTask(String task) {
        synchronized (lockedTasks) {
            return lockedTasks.add(task);
        }
    }

    public static void unlockTask(String task) {
        synchronized (lockedTasks) {
            lockedTasks.remove(task);
        }
    }

}
