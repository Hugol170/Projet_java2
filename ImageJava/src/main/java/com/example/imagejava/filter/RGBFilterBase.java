package com.example.imagejava.filter;

import javafx.scene.image.*;

public abstract class RGBFilterBase implements ImageFilter {

    @Override
    public Image apply(Image input) {
        int w = (int) input.getWidth();
        int h = (int) input.getHeight();
        WritableImage output = new WritableImage(w, h);
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int argb = reader.getArgb(x, y);
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;
                int a = (argb >> 24) & 0xFF;

                int[] newRGB = transform(r, g, b);
                int newArgb = (a << 24) | (newRGB[0] << 16) | (newRGB[1] << 8) | newRGB[2];
                writer.setArgb(x, y, newArgb);
            }
        }

        return output;
    }

    protected abstract int[] transform(int r, int g, int b);
}

