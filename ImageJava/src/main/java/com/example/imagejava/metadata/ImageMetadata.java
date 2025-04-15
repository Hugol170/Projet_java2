package com.example.imagejava.metadata;

import java.util.ArrayList;
import java.util.List;

public class ImageMetadata {
    private String fileName;
    private List<String> tags = new ArrayList<>();
    private List<String> transformations = new ArrayList<>();

    public ImageMetadata(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getTransformations() {
        return transformations;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void addTransformation(String transformationName) {
        transformations.add(transformationName);
    }

    @Override
    public String toString() {
        return "ImageMetadata{" +
                "fileName='" + fileName + '\'' +
                ", tags=" + tags +
                ", transformations=" + transformations +
                '}';
    }
}
