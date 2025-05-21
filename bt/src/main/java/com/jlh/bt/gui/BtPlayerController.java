package com.jlh.bt.gui;

import com.jlh.bt.os.BluetoothController;
import com.jlh.bt.os.ShellController;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BtPlayerController {

    @FXML private Label device;
    @FXML private Circle discoverable;

    @FXML private Label artist;
    @FXML private Label album;
    @FXML private Label name;

    @FXML private ProgressBar volumeBar;
    @FXML private Label volume;

    private BluetoothController bt;

    private boolean bluetoothActive = true;

    public void setBluetoothController(BluetoothController bt) {
        this.bt = bt;
        createBindings();
    }

    private void createBindings() {
        device.textProperty().bind(Bindings.createStringBinding(() -> {
            return bluetoothActive ?
                bt.getConnectedDeviceName() : 
                "Onboard Media;";
        }));

        artist.textProperty().bind(Bindings.createStringBinding(() -> bt.getCurrentTrack().artist()));
        album.textProperty().bind(Bindings.createStringBinding(() -> bt.getCurrentTrack().album()));
        name.textProperty().bind(Bindings.createStringBinding(() -> bt.getCurrentTrack().name()));

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

    public void setBluetoothActive(boolean active) {
        bluetoothActive = active;
    }
    
}