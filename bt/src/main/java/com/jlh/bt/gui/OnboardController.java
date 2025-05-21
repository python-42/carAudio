package com.jlh.bt.gui;

import java.io.ByteArrayInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jlh.bt.onboard.media.MediaController;
import com.jlh.bt.onboard.menu.MenuController;
import com.jlh.bt.onboard.menu.MusicLoader;
import com.jlh.bt.os.ShellController;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class OnboardController {
    
    @FXML private AnchorPane menuPane;

    @FXML private ImageView albumArt;
    @FXML private Label album;
    @FXML private Label name;
    @FXML private Label artist;

    @FXML private ProgressBar volume;
    @FXML private ProgressBar trackProgress;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private MediaController onboard;
    private MenuController menu;
    private Image unknownArtImage;

    public void setOnboardController(MediaController onboard, MenuController menu) {
        this.onboard = onboard;
        this.menu = menu;

        this.unknownArtImage = albumArt.getImage();
        startTrackProgressUpdater();
        updateMenu();
        updateTrack();
    }

    public void updateMenu() {
        Platform.runLater(() -> {
            menuPane.getChildren().clear();
            menuPane.getChildren().add(menu.getUIComponent());
        });
    }

    public void updateTrack() {
        Platform.runLater(() -> {
            artist.setText(onboard.getCurrentTrack().artist());
            album.setText(onboard.getCurrentTrack().album());
            name.setText(onboard.getCurrentTrack().name());
            albumArt.setImage(getAlbumArt());
            trackProgress.setProgress(0);
        });
    }

    private void startTrackProgressUpdater() {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        trackProgress.setProgress(onboard.getPercentageComplete());
                        volume.setProgress(ShellController.getInstance().getCurrentVolume() / 100.0);
                    }
                }
            ),
            new KeyFrame(Duration.seconds(0.5))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public Image getAlbumArt() {
        byte[] buffer = MusicLoader.getInstance().getAlbumArt(onboard.getCurrentTrack());
        if (buffer == null) {
            logger.trace("Unknown album art found for track " + onboard.getCurrentTrack() + ", reverting to default image");
            return unknownArtImage;
        }
        return new Image(new ByteArrayInputStream(buffer));
    }
    
}