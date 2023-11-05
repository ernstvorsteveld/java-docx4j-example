package com.sternitc.docx;

import com.sternitc.docx.domain.TemplateData;
import org.docx4j.openpackaging.exceptions.Docx4JException;

public interface TemplateMerger {

    void merge(TemplateData templateData) throws Docx4JException;
}
