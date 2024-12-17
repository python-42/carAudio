package com.jlh.bt;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.simple.SimpleLogger;

import com.jlh.bt.hardware.GUIDriver;
import com.jlh.bt.hardware.ScreenController;
import com.jlh.bt.os.BluetoothController;
import com.jlh.bt.os.FavoriteFileController;
import com.jlh.bt.os.ShellController;
import com.jlh.bt.os.Track;

import javafx.application.Platform;

public class Main {

    private Logger logger;
    private ScreenController screen;
    public static void main(String[] args) {
        new Main(args);
    }

    private Main(String[] args) {
        System.setProperty(Constants.LOG_LEVEL_CLASSES_PROPERTY, Constants.LOG_LEVEL);
        System.setProperty(SimpleLogger.LOG_FILE_KEY, Constants.LOG_FILE_PATH);

        logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Execution started.");
        debugPrintConstants();

        new Thread( () -> {
                new GUIDriver(args);
                System.exit(0); //kill other threads if UI closes. 
            }, 
            "GUI driver thread"
        ).start();

        while (GUIDriver.getUIController() == null) {
            //do nothing
        }

        screen = GUIDriver.getUIController();
        logger.debug("UI controller gotten from GUIDriver");

        setUndiscoverable();
        ShellController.getInstance().resetVolume();

        new Thread(this::updateUI, "UI Update Thread").start();

        //assume that there is only one device connected at once
        BluetoothController.getInstance().setOnDisconnectionEvent(() -> {
            setDiscoverable();
            Platform.runLater(() -> screen.setDevice("Disconnected"));
        });
        BluetoothController.getInstance().setOnConnectionEvent(() -> {
            setUndiscoverable();
            FavoriteFileController.getInstance().setAddress(BluetoothController.getInstance().getConnectedDevices().get(0).getAddress());
            Platform.runLater(() -> screen.setDevice(BluetoothController.getInstance().getConnectedDevices().get(0).getName()));
        });

        //TODO buttons

        attemptFavoriteConnection();
    }

    private void attemptFavoriteConnection() {
        //load mac from file
        String address = FavoriteFileController.getInstance().getAddress();

        logger.info("Favorite address retrieved: " + address);

        if (address != null) {
            BluetoothController.getInstance().disconnectAllExcept(address);
            BluetoothController.getInstance().discover();
            if (BluetoothController.getInstance().connect(address)) {
                logger.info("Favorite connection successful.");
                BluetoothController.getInstance().play();
            }else {
                logger.info("Favorite device connection unsuccessful. Enabling discovery.");
                setDiscoverable();
            }
        }else {
            //start over. No devices should be connected before this program starts running anyways.
            BluetoothController.getInstance().disconnectAllExcept("");
            setDiscoverable();
        }
    }

    private void setDiscoverable() {
        Platform.runLater(() -> screen.setIsDiscoverable(true));
        BluetoothController.getInstance().setDiscoverableState(true);
    }

    private void setUndiscoverable() {
        Platform.runLater(() -> screen.setIsDiscoverable(false));
        BluetoothController.getInstance().setDiscoverableState(false);
    }

    private void updateUI() {
        logger.info("Update UI thread started.");
        int previousVolume = -100;
        Track previousTrack = null;

        while (true) {
            int vol = ShellController.getInstance().getCurrentVolume();
            Track track = BluetoothController.getInstance().getConnectedDevices().isEmpty() ?
                        Track.BLANK :
                        BluetoothController.getInstance().getCurrentTrack();

            if (vol != previousVolume) {
                logger.debug("New volume " + vol);
                Platform.runLater(() -> screen.setVolume(vol));
            }

            if (!track.equals(previousTrack)) {
                logger.debug("New track: " + track);
                Platform.runLater(() -> screen.setTrackInformation(track));
            }
        
            previousVolume = vol;
            previousTrack = track;
        }
    }

    /**
     * Used for debug purposes only.
     */
    private void debugPrintConstants() {
        logger.debug("All Constants: ");
        for (Field f : Constants.class.getFields()) {
            try {
                logger.debug(f.getName() + ": " + f.get(null) + "(" + f.getType().getName() + ")");
            } catch (IllegalArgumentException | IllegalAccessException ignored) {}
        }
    }
}
