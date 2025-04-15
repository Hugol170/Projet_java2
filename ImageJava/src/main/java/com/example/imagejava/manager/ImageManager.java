package com.example.imagejava.manager;

import javafx.scene.image.Image;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageManager {
    private final List<File> imageFiles = new ArrayList<>();
    private int currentIndex = 0;

    public ImageManager() {
        URL resource = getClass().getResource("/com/example/imagejava/Image/");
        if (resource != null) {
            File folder = new File(resource.getPath());
            File[] files = folder.listFiles((dir, name) -> name.matches(".*\\.(png|jpe?g)"));
            if (files != null) {
                imageFiles.addAll(List.of(files));
            }
        }
    }

    public Image getCurrentImage() {
        if (imageFiles.isEmpty()) return null;
        return new Image(imageFiles.get(currentIndex).toURI().toString());
    }

    public Image getNextImage() {
        if (currentIndex < imageFiles.size() - 1) {
            currentIndex++;
        }
        return getCurrentImage();
    }

    public Image getPreviousImage() {
        if (currentIndex > 0) {
            currentIndex--;
        }
        return getCurrentImage();
    }

    public Image searchImage(String name) {
        for (int i = 0; i < imageFiles.size(); i++) {
            if (imageFiles.get(i).getName().toLowerCase().contains(name.toLowerCase())) {
                currentIndex = i;
                return getCurrentImage();
            }
        }
        return null;
    }
    public String getCurrentFileName() {
        if (imageFiles.isEmpty()) return null;
        return imageFiles.get(currentIndex).getName();
    }

}

