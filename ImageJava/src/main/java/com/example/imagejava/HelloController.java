package com.example.imagejava;

import com.example.imagejava.filter.ImageFilter;
import com.example.imagejava.filter.SepiaFilter;
import com.example.imagejava.filter.NoirBlancFilter;
import com.example.imagejava.filter.SobelFilter;
import com.example.imagejava.filter.SwapRGBFilter;
import com.example.imagejava.manager.ImageManager;
import com.example.imagejava.metadata.ImageMetadata;
import com.example.imagejava.metadata.MetadataManager;
import com.example.imagejava.service.ImageTransformerService;
import com.example.imagejava.filter.SecureShuffleFilter;
import com.example.imagejava.filter.SecureUnshuffleFilter;




import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class HelloController {

    @FXML
    private ImageView imageView;

    @FXML
    private TextField searchField;

    @FXML
    private TextField tagField;

    private final ImageManager imageManager = new ImageManager();
    private final ImageTransformerService transformerService = new ImageTransformerService();
    private final MetadataManager metadataManager = new MetadataManager();

    @FXML
    private TextField passwordField;

    private Image originalImageBeforeEncryption;



    // 🔁 Initialisation : affiche la première image si disponible
    @FXML
    public void initialize() {
        Image image = imageManager.getCurrentImage();
        if (image != null) {
            imageView.setImage(image);
        }
    }

    // 🔍 Recherche une image par nom
    @FXML
    public void searchImage() {
        String searchText = searchField.getText().trim();
        Image image = imageManager.searchImage(searchText);
        if (image != null) {
            imageView.setImage(image);
        } else {
            showAlert("Image non trouvée", "Aucune image correspondant à '" + searchText + "'.");
        }
    }

    // ⏩ Image suivante
    @FXML
    public void showNextImage() {
        imageView.setImage(imageManager.getNextImage());
    }

    // ⏪ Image précédente
    @FXML
    public void showPreviousImage() {
        imageView.setImage(imageManager.getPreviousImage());
    }

    // 🔄 Rotation 90°
    @FXML
    public void rotateImage() {
        Image image = imageView.getImage();
        if (image != null) {
            imageView.setImage(transformerService.rotate(image));
            saveTransformation("Rotation");
        }
    }

    // 🔃 Symétrie horizontale
    @FXML
    public void mirrorImage() {
        Image image = imageView.getImage();
        if (image != null) {
            imageView.setImage(transformerService.mirror(image));
            saveTransformation("Symétrie");
        }
    }

    // 🎨 Filtres
    @FXML
    public void applySwapRGB() {
        applyFilter(new SwapRGBFilter());
    }

    @FXML
    public void applyNoirBlanc() {
        applyFilter(new NoirBlancFilter());
    }

    @FXML
    public void applySepia() {
        applyFilter(new SepiaFilter());
    }

    @FXML
    public void applySobel() {
        applyFilter(new SobelFilter());
    }

    // ➕ Ajouter un tag à l'image
    @FXML
    public void addTagToImage() {
        String tag = tagField.getText().trim();
        if (!tag.isEmpty()) {
            String fileName = imageManager.getCurrentFileName();
            ImageMetadata metadata = metadataManager.getOrCreateMetadata(fileName);
            metadata.addTag(tag);
            metadataManager.save();
            System.out.println("✅ Tag ajouté : " + tag);
            tagField.clear();
        }
    }

    // Appliquer un filtre générique + enregistrer dans metadata.json
    private void applyFilter(ImageFilter filter) {
        Image image = imageView.getImage();
        if (image != null) {
            imageView.setImage(filter.apply(image));
            saveTransformation(filter.getClass().getSimpleName());
        }
    }

    // 🔄 Enregistrer le nom de la transformation dans le fichier JSON
    private void saveTransformation(String transformationName) {
        String fileName = imageManager.getCurrentFileName();
        if (fileName != null) {
            ImageMetadata metadata = metadataManager.getOrCreateMetadata(fileName);
            metadata.addTransformation(transformationName);
            metadataManager.save();
        }
    }

    // ⚠️ Message d’erreur
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void showImageMetadata() {
        String fileName = imageManager.getCurrentFileName();
        if (fileName == null) {
            showAlert("Aucune image", "Aucune image n'est actuellement sélectionnée.");
            return;
        }

        ImageMetadata metadata = metadataManager.getOrCreateMetadata(fileName);

        StringBuilder message = new StringBuilder();
        message.append("Nom du fichier : ").append(fileName).append("\n\n");

        message.append("📌 Tags :\n");
        if (metadata.getTags().isEmpty()) {
            message.append("  Aucun tag.\n");
        } else {
            for (String tag : metadata.getTags()) {
                message.append("  • ").append(tag).append("\n");
            }
        }

        message.append("\n🎨 Transformations :\n");
        if (metadata.getTransformations().isEmpty()) {
            message.append("  Aucune transformation.\n");
        } else {
            for (String transfo : metadata.getTransformations()) {
                message.append("  • ").append(transfo).append("\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Métadonnées de l'image");
        alert.setHeaderText(null);
        alert.setContentText(message.toString());
        alert.showAndWait();
    }

    @FXML
    public void applySecureShuffle() {
        String password = passwordField.getText().trim();
        if (password.isEmpty()) {
            showAlert("Mot de passe requis", "Veuillez entrer un mot de passe pour chiffrer l'image.");
            return;
        }

        // Sauvegarde de l'image originale avant chiffrement
        originalImageBeforeEncryption = imageView.getImage();

        applyFilter(new SecureShuffleFilter(password));
        saveTransformation("SecureShuffleFilter");
        passwordField.clear();
    }


    @FXML
    public void applySecureUnshuffle() {
        String password = passwordField.getText().trim();
        if (password.isEmpty()) {
            showAlert("Mot de passe requis", "Veuillez entrer le mot de passe pour déchiffrer l'image.");
            return;
        }

        if (originalImageBeforeEncryption == null) {
            showAlert("Erreur", "Aucune image originale à restaurer. Chiffrez d'abord une image.");
            return;
        }

        imageView.setImage(new SecureUnshuffleFilter(password).apply(originalImageBeforeEncryption));
        saveTransformation("SecureUnshuffleFilter");
        passwordField.clear();
    }




}

