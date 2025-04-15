package com.example.imagejava.filter;

public class SwapRGBFilter extends RGBFilterBase {
    @Override
    protected int[] transform(int r, int g, int b) {
        return new int[]{b, r, g}; // Swap RGB to BRG
    }
}
