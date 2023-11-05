package com.sternitc.docx.domain;

public abstract class Data {

    private final DataType type;

    protected Data(DataType type) {
        this.type = type;
    }

    public DataType getType() {
        return type;
    }

    public enum DataType {
        SimpleString
    }

}

