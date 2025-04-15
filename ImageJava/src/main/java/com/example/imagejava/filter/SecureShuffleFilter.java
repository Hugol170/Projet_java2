package com.example.imagejava.filter;

import javafx.scene.image.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecureShuffleFilter implements ImageFilter {

    private final String password;

    public SecureShuffleFilter(String password) {
        this.password = password;
    }

    @Override
    public Image apply(Image input) {
        int width = (int) input.getWidth();
        int height = (int) input.getHeight();
        PixelReader reader = input.getPixelReader();
        WritableImage shuffledImage = new WritableImage(width, height);
        PixelWriter writer = shuffledImage.getPixelWriter();

        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < width * height; i++) {
            indexes.add(i);
        }

        // ✅ Génére un SecureRandom basé sur le mot de passe (via SHA-256)
        try {
            byte[] seed = MessageDigest.getInstance("SHA-256").digest(password.getBytes());
            SecureRandom random = SecureRandom.getInstanceStrong();
            random.setSeed(seed);
            Collections.shuffle(indexes, random); // Shuffle les index de manière prédictible

            for (int i = 0; i < indexes.size(); i++) {
                int srcIndex = i;
                int destIndex = indexes.get(i);

                int srcX = srcIndex % width;
                int srcY = srcIndex / width;
                int destX = destIndex % width;
                int destY = destIndex / width;

                writer.setArgb(destX, destY, reader.getArgb(srcX, srcY));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return input; // Retourne l'image originale en cas d'erreur
        }

        return shuffledImage;
    }
}
