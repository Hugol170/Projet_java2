package com.example.imagejava.filter;

public class SepiaFilter extends RGBFilterBase {
    @Override
    protected int[] transform(int r, int g, int b) {
        int newR = Math.min(255, (int) (0.393 * r + 0.769 * g + 0.189 * b));
        int newG = Math.min(255, (int) (0.349 * r + 0.686 * g + 0.168 * b));
        int newB = Math.min(255, (int) (0.272 * r + 0.534 * g + 0.131 * b));
        return new int[]{newR, newG, newB};
    }
}
