package com.tyt.qiuzhi.asyncmq;

import java.util.HashMap;
import java.util.Map;

public class EventModel {
    private int actorId;        //触发者
    private int entityType;     //事件实体类型
    private int entityId;       //事件实体ID
    private int entityOwnerId;      //事件实体的拥有者
    private EventType type;         //事件类型

    private Map<String,String> exts = new HashMap<>();


    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "EventModel{" +
                "actorId=" + actorId +
                ", entityType=" + entityType +
                ", entityId=" + entityId +
                ", entityOwnerId=" + entityOwnerId +
                ", type=" + type +
                ", exts=" + exts +
                '}';
    }



    public EventModel() {
    }

    public EventModel(EventType type) {
        this.type = type;
    }

    public int getActorId() {

        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }

    public String getExt(String key) {
        return exts.get(key);
    }

    public EventModel setExt(String key, String value) {
        exts.put(key,value);
        return this;
    }

}
