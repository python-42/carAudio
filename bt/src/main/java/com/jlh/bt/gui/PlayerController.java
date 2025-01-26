package com.jlh.bt.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jlh.bt.onboard.media.Track;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PlayerController {

    @FXML private Label device;
    @FXML private Circle discoverable;

    @FXML private Label artist;
    @FXML private Label album;
    @FXML private Label name;

    @FXML private ProgressBar volumeBar;
    @FXML private Label volume;

    private final Logger logger;

    public PlayerController() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    public void setTrackInformation(Track track) {
        logger.trace("Track UI set to " + track);
        artist.setText(track.artist());
        album.setText(track.album());
        name.setText(track.name());
    }

    public void setDevice(String deviceName) {
        logger.trace("UI device name set to " + deviceName);
        device.setText(deviceName);
    }

    public void setVolume(int currVolume) {
        logger.trace("UI volume set to " + currVolume);
        volume.setText(currVolume + "");
        volumeBar.setProgress(currVolume / 100.0); //TODO bind all properties instead of using methods
    }

    public void setIsDiscoverable(boolean isDiscoverable) {
        logger.trace("Discoverable indicator set to " + isDiscoverable + " on UI.");
        if (isDiscoverable) {
            discoverable.setFill(Color.GREEN);
        }else {
            discoverable.setFill(Color.RED);
        }
    }
    
}