package com.sternitc.docx;

import com.sternitc.docx.domain.Data;
import com.sternitc.docx.domain.SimpleData;
import jakarta.xml.bind.JAXBElement;
import org.apache.commons.compress.utils.Lists;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Text;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractTemplateMerger {

    protected final InputStream input;
    protected final OutputStream output;

    public AbstractTemplateMerger(InputStream input, OutputStream output) {
        this.input = input;
        this.output = output;
    }

    public <T> List<T> getAllElementsByType(Object element, Class<T> type) {
        List<T> result = new ArrayList<>();
        if (element instanceof JAXBElement) element = ((JAXBElement<?>) element).getValue();

        if (element.getClass().equals(type)) {
            result.add(type.cast(element));
        } else if (element instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) element).getContent();
            for (Object child : children) {
                result.addAll(getAllElementsByType(child, type));
            }
        }
        return result;
    }

    public String getAsString(List<Text> texts) {
        return texts.stream()
                .map(Text::getValue)
                .filter(Objects::nonNull)
                .reduce(String::concat).orElse("");
    }

    Map<Integer, TextPosition> getTextPositions(List<Text> texts) {
        TextPositionCalculator textPositionCalculator = new TextPositionCalculator();
        return texts.stream()
                .map(textPositionCalculator::calculate)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(TextPosition::from, t -> t));
    }

}

class TextPositionCalculator {
    private int start = 0;

    TextPosition calculate(Text text) {
        var length = text.getValue().length();
        var next = start + text.getValue().length();
        TextPosition textPosition = new TextPosition(text, start, start + length - 1, length, next);
        start = next;
        return textPosition;
    }
}

class Replacer {
    private final Map<Integer, TextPosition> textPositions;
    private final String completeString;

    public Replacer(Map<Integer, TextPosition> textPositions, String completeString) {
        this.textPositions = textPositions;
        this.completeString = completeString;
    }

    public void doReplace(Map.Entry<String, List<Data>> entry) {
        String source = completeString;
        int length = entry.getKey().length();
        while (true) {
            int from = source.indexOf(entry.getKey());
            if (from == -1) {
                break;
            }
            doReplace(entry, from, from + length - 1);
            source = completeString.substring(from + length);
        }
    }

    private void doReplace(Map.Entry<String, List<Data>> entry, int from, int to) {
        List<TextPosition> involvedTextPositions = Lists.newArrayList();
        TextPosition current = textPositions.get(from);
        involvedTextPositions.add(current);
        while (current.to() < to) {
            current = textPositions.get(current.next());
            involvedTextPositions.add(current);
        }
        doReplace(entry, involvedTextPositions);
    }

    private void doReplace(Map.Entry<String, List<Data>> entry, List<TextPosition> involvedTextPositions) {
        involvedTextPositions.forEach(t -> t.text().setValue(""));

        if(entry.getValue().get(0).getType().equals(Data.DataType.SimpleString)) {
            SimpleData simpleData = (SimpleData) entry.getValue().get(0);
            involvedTextPositions.get(0).text().setValue(simpleData.getValue());
        } else {
            throw new RuntimeException("Type unknown.");
        }
    }
}

record TextPosition(Text text, int from, int to, int length, int next) {
    public String getValue() {
        return text.getValue();
    }
}
