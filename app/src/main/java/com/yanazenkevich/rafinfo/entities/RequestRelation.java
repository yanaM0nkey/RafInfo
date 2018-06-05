package com.yanazenkevich.rafinfo.entities;

public class RequestRelation {

    private String objectId;
    private Relation mRelation;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Relation getmRelation() {
        return mRelation;
    }

    public void setmRelation(Relation mRelation) {
        this.mRelation = mRelation;
    }
}
