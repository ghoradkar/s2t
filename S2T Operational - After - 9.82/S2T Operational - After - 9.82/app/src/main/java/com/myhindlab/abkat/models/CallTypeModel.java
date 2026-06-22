package com.myhindlab.abkat.models;

public class CallTypeModel {

    private String typeName;
    private int typeId;


    public CallTypeModel(String typeName, int typeId) {
        this.typeName = typeName;
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
