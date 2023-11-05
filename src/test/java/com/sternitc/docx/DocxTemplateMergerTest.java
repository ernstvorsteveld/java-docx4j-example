package com.sternitc.docx;

import com.sternitc.docx.domain.Data;
import com.sternitc.docx.domain.SimpleData;
import com.sternitc.docx.domain.TemplateData;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

class DocxTemplateMergerTest {

    @Test
    public void should_merge_docx_template_with_data() throws IOException, Docx4JException {
        InputStream input = new ClassPathResource("template.docx").getInputStream();
        OutputStream output = new FileOutputStream("merged1.docx");

        DocxTemplateMerger templateMerger = new DocxTemplateMerger(input,output);
        templateMerger.merge(getTemplateData());
    }

    private TemplateData getTemplateData() {
        TemplateData templateData = new TemplateData();

        SimpleData header = new SimpleData("My Company Name");
        List<Data> headers = new ArrayList<>();
        headers.add(header);
        templateData.add("${header_meant_for}", headers);

        return templateData;
    }

}