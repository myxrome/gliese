package com.whiteboxteam.gliese.data.entity;

/**
 * Gliese Project.
 * User: Aleksey
 * Date: 19.03.2015
 * Time: 13:35
 */
public class TopicEntity {

    public long id;
    public long topicGroupId;
    public String name;

    public static TopicEntity newInstance(long id, long topicGroupId, String name) {
        TopicEntity result = new TopicEntity();
        result.id = id;
        result.topicGroupId = topicGroupId;
        result.name = name;
        return result;
    }

}
