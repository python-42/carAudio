package com.jlh.bt.gui;

import com.jlh.bt.onboard.media.MediaController;
import com.jlh.bt.onboard.media.Track;
import com.jlh.bt.os.BluetoothController;
import com.jlh.bt.os.ShellController;

import javafx.beans.binding.Bindings;
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

    private final BluetoothController bt;
    private final MediaController onboard;

    private boolean bluetoothActive = true;

    public PlayerController(BluetoothController bt, MediaController onboard) {
        this.bt = bt;
        this.onboard = onboard;

        device.textProperty().bind(Bindings.createStringBinding(() -> {
            return bluetoothActive ?
                bt.getConnectedDeviceName() : 
                "Onboard Media;";
        }));

        artist.textProperty().bind(Bindings.createStringBinding(() -> getCurrentTrack().artist()));
        album.textProperty().bind(Bindings.createStringBinding(() -> getCurrentTrack().album()));
        name.textProperty().bind(Bindings.createStringBinding(() -> getCurrentTrack().name()));

        volume.textProperty().bind(Bindings.createStringBinding(() -> ShellController.getInstance().getCurrentVolume() + ""));
        volumeBar.progressProperty().bind(Bindings.createDoubleBinding(() -> ShellController.getInstance().getCurrentVolume() / 100.0));

        discoverable.fillProperty().bind(Bindings.createObjectBinding(() -> {
            if (bluetoothActive) {
                return BluetoothController.getInstance().isDiscoverable() ? 
                    Color.GREEN :
                    Color.RED;
            }
            return Color.BLACK;
        }));
    }

    private Track getCurrentTrack() {
        return bluetoothActive ?
            bt.getCurrentTrack() :
            onboard.getCurrentTrack();
    }

    public void setBluetoothActive(boolean active) {
        bluetoothActive = active;
    }
    
}