package com.sternitc.docx;

import com.sternitc.docx.domain.TemplateData;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Text;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class DocxTemplateMerger extends AbstractTemplateMerger implements TemplateMerger {

    private final WordprocessingMLPackage wordMLPackage;
    private final List<Text> texts;
    private final String completeString;

    public DocxTemplateMerger(InputStream input, OutputStream output) throws Docx4JException {
        super(input, output);
        wordMLPackage = WordprocessingMLPackage.load(input);
        texts = getAllElementsByType(wordMLPackage.getMainDocumentPart(), Text.class);
        completeString = getAsString(texts);
    }

    @Override
    public void merge(TemplateData templateData) throws Docx4JException {
        Map<Integer, TextPosition> textPositions = getTextPositions(texts);
        Replacer replacer = new Replacer(textPositions, completeString);
        templateData.getData().forEach(replacer::doReplace);
        wordMLPackage.save(output);
    }

}

