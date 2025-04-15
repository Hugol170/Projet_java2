package com.example.imagejava.service;

import javafx.scene.image.*;

public class ImageTransformerService {

    public Image rotate(Image image) {
        return transformImage(image, (reader, writer, x, y, w, h) ->
                writer.setArgb(h - y - 1, x, reader.getArgb(x, y)), true);
    }

    public Image mirror(Image image) {
        return transformImage(image, (reader, writer, x, y, w, h) ->
                writer.setArgb(w - x - 1, y, reader.getArgb(x, y)), false);
    }

    private Image transformImage(Image image, ImageTransformer transformer, boolean swapSize) {
        if (image == null) return null;
        int w = (int) image.getWidth(), h = (int) image.getHeight();
        WritableImage newImage = new WritableImage(swapSize ? h : w, swapSize ? w : h);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = newImage.getPixelWriter();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                transformer.apply(reader, writer, x, y, w, h);
            }
        }

        return newImage;
    }

    @FunctionalInterface
    public interface ImageTransformer {
        void apply(PixelReader reader, PixelWriter writer, int x, int y, int w, int h);
    }
}

