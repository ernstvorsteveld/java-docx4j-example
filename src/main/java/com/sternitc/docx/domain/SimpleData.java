package com.sternitc.docx.domain;

import lombok.Getter;

@Getter
public class SimpleData extends Data {

    private final String data;

    public SimpleData(String data) {
        super(DataType.SimpleString);
        this.data = data;
    }

    public String getValue() {
        return data;
    }

}
