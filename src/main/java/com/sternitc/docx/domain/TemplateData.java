package com.sternitc.docx.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TemplateData {

    private Map<String, List<Data>> templateData = new HashMap<>();

    public void add(String key, List<Data> data) {
        templateData.put(key, data);
    }

    public Stream<Map.Entry<String, List<Data>>> getData() {
        return templateData.entrySet().stream();
    }
}
