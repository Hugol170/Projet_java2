package com.example.imagejava.filter;

import javafx.scene.image.*;

public class SobelFilter implements ImageFilter {

    @Override
    public Image apply(Image input) {
        int width = (int) input.getWidth();
        int height = (int) input.getHeight();
        WritableImage output = new WritableImage(width, height);
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        int[][] gx = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };

        int[][] gy = {
                {-1, -2, -1},
                { 0,  0,  0},
                { 1,  2,  1}
        };

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int sumX = 0;
                int sumY = 0;

                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int argb = reader.getArgb(x + kx, y + ky);
                        int gray = getGray(argb); // Convertit le pixel en niveaux de gris
                        sumX += gx[ky + 1][kx + 1] * gray;
                        sumY += gy[ky + 1][kx + 1] * gray;
                    }
                }

                int gradient = (int) Math.min(255, Math.hypot(sumX, sumY));
                int argb = (0xFF << 24) | (gradient << 16) | (gradient << 8) | gradient;
                writer.setArgb(x, y, argb);
            }
        }

        return output;
    }

    private int getGray(int argb) {
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        return (r + g + b) / 3;
    }
}
