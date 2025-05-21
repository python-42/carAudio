package com.jlh.bt.gui;

import java.io.ByteArrayInputStream;

import com.jlh.bt.onboard.media.MediaController;
import com.jlh.bt.onboard.menu.MenuFactory;
import com.jlh.bt.os.ShellController;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class OnboardController {
    
    @FXML private Pane menuPane;

    @FXML private ImageView albumArt;
    @FXML private Label album;
    @FXML private Label name;
    @FXML private Label artist;

    @FXML private ProgressBar volume;
    @FXML private ProgressBar trackProgress;

    private MediaController onboard;
    private Image unknownArtImage;

    public void setOnboardController(MediaController onboard) {
        this.onboard = onboard;
        bind();
    }

    private void bind() {
        artist.textProperty().bind(Bindings.createStringBinding(() -> onboard.getCurrentTrack().artist()));
        album.textProperty().bind(Bindings.createStringBinding(() -> onboard.getCurrentTrack().album()));
        name.textProperty().bind(Bindings.createStringBinding(() -> onboard.getCurrentTrack().name()));

        this.unknownArtImage = albumArt.getImage();
        albumArt.imageProperty().bind(Bindings.createObjectBinding(this::getAlbumArt));

        volume.progressProperty().bind(Bindings.createDoubleBinding(() -> ShellController.getInstance().getCurrentVolume() / 100.0));
        trackProgress.progressProperty().bind(Bindings.createDoubleBinding(() -> onboard.getPercentageComplete()));
    }


    public Image getAlbumArt() {
        byte[] buffer = MenuFactory.getInstance().getAlbumArt(onboard.getCurrentTrack());
        if (buffer == null) {
            return unknownArtImage;
        }
        return new Image(new ByteArrayInputStream(buffer));
    }
    
}