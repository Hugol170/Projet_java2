package com.example.imagejava.filter;

import javafx.scene.image.*;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecureUnshuffleFilter implements ImageFilter {

    private final String password;

    public SecureUnshuffleFilter(String password) {
        this.password = password;
    }

    @Override
    public Image apply(Image input) {
        int width = (int) input.getWidth();
        int height = (int) input.getHeight();
        PixelReader reader = input.getPixelReader();
        WritableImage outputImage = new WritableImage(width, height);
        PixelWriter writer = outputImage.getPixelWriter();

        List<Integer> shuffledIndexes = new ArrayList<>();
        for (int i = 0; i < width * height; i++) {
            shuffledIndexes.add(i);
        }

        try {
            // ➤ Même graine que pour le shuffle
            byte[] seed = MessageDigest.getInstance("SHA-256").digest(password.getBytes());
            SecureRandom random = SecureRandom.getInstanceStrong();
            random.setSeed(seed);
            Collections.shuffle(shuffledIndexes, random);

            // ✅ INVERSE LOGIQUE CORRECTE
            // shuffledIndexes : index original → index mélangé
            // ici, on remet chaque pixel du reader à sa position d’origine
            for (int i = 0; i < shuffledIndexes.size(); i++) {
                int mixedIndex = shuffledIndexes.get(i); // où était placé le pixel original `i`

                int srcX = mixedIndex % width;
                int srcY = mixedIndex / width;

                int destX = i % width;
                int destY = i / width;

                writer.setArgb(destX, destY, reader.getArgb(srcX, srcY));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return input;
        }

        return outputImage;
    }
}
