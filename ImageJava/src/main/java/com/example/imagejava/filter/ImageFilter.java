package com.example.imagejava.filter;

import javafx.scene.image.Image;

public interface ImageFilter {
    Image apply(Image input);
}
