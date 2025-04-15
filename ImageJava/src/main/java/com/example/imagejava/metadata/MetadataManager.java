package com.example.imagejava.metadata;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class MetadataManager {
    private static final String FILE_PATH = "metadata.json";
    private Map<String, ImageMetadata> metadataMap = new HashMap<>();
    private final Gson gson = new Gson();

    public MetadataManager() {
        load();
    }

    public ImageMetadata getOrCreateMetadata(String fileName) {
        return metadataMap.computeIfAbsent(fileName, ImageMetadata::new);
    }

    public void save() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(metadataMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, ImageMetadata>>() {}.getType();
            metadataMap = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
