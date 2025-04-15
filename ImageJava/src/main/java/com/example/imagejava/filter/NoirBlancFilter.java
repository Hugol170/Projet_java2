package com.example.imagejava.filter;

public class NoirBlancFilter extends RGBFilterBase {
    @Override
    protected int[] transform(int r, int g, int b) {
        int gray = (r + g + b) / 3;
        return new int[]{gray, gray, gray};
    }
}
