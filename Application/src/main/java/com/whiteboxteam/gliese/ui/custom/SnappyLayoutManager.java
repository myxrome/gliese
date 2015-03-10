package com.whiteboxteam.gliese.ui.custom;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 10.03.2015
 * Time: 13:43
 */
/**
 * An interface that LayoutManagers that should snap to grid should implement.
 */
public interface SnappyLayoutManager {

    /**
     * @param velocityX
     * @param velocityY
     * @return the resultant position from a fling of the given velocity.
     */
    int getPositionForVelocity(int velocityX, int velocityY);

    /**
     * @return the position this list must scroll to to fix a state where the
     * views are not snapped to grid.
     */
    int getFixScrollPos();

}