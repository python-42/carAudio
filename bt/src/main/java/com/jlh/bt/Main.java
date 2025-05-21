package com.jlh.bt;

import java.io.File;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.simple.SimpleLogger;

import com.jlh.bt.constants.Constants;
import com.jlh.bt.gui.BtPlayerController;
import com.jlh.bt.gui.GUIDriver;
import com.jlh.bt.gui.OnboardController;
import com.jlh.bt.hardware.CANDriver;
import com.jlh.bt.onboard.media.MediaController;
import com.jlh.bt.onboard.menu.MenuController;
import com.jlh.bt.os.BluetoothController;
import com.jlh.bt.os.FavoriteFileController;
import com.jlh.bt.os.ShellController;

public class Main {

    private final Logger logger;
    private final BtPlayerController bluetoothUI;
    private final OnboardController onboardUI;

    private final MediaController media;
    private final MenuController menu;

    private final Constants CONSTANTS;

    public static void main(String[] args) {
        new Main(args);
    }

    private Main(String[] args) {
        CONSTANTS = Constants.getInstance();

        System.setProperty(CONSTANTS.LOG_LEVEL_CLASSES_PROPERTY(), CONSTANTS.LOG_LEVEL());
        System.setProperty(SimpleLogger.LOG_FILE_KEY, CONSTANTS.LOG_FILE_PATH());

        logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Execution started.");
        debugPrintConstants();

        new Thread( () -> {
                new GUIDriver(args);
                System.exit(0); //kill other threads if UI closes. 
            }, 
            "GUI driver thread"
        ).start();

        while (GUIDriver.getBluetoothUIController() == null || GUIDriver.getOnboardUIController() == null) {
            //do nothing
        }

        bluetoothUI = GUIDriver.getBluetoothUIController();
        onboardUI = GUIDriver.getOnboardUIController();
        logger.debug("GUIDriver successfully provided UI controller");

        menu = new MenuController(new File(CONSTANTS.ONBOARD_MEDIA_DIRECTORY()));
        media = new MediaController(() -> onboardUI.updateTrack());

        onboardUI.setOnboardController(media, menu);

        ShellController.getInstance().resetVolume();
        registerCanCallbacks();
        //bluetoothInit();
    }

    private void registerCanCallbacks() {
        CANDriver can = CANDriver.getInstance();
        can.registerCallback(() -> menu.focusUp(), CONSTANTS.UP_BUTTON(), false);
        can.registerCallback(() -> menu.focusDown(), CONSTANTS.DOWN_BUTTON(), false);
        can.registerCallback(() -> {menu.ascend(); onboardUI.updateMenu();}, CONSTANTS.LEFT_BUTTON(), false);
        can.registerCallback(() -> {menu.descend(); onboardUI.updateMenu();}, CONSTANTS.RIGHT_BUTTON(), false);
        can.registerCallback(() -> {media.setPlaylist(menu.getPlaylist()); System.out.println(media.getCurrentTrack().name());}, CONSTANTS.OK_BUTTON(), false);
    }

    private void bluetoothInit() {
        BluetoothController.getInstance().setDiscoverableState(false);

        //assume that there is only one device connected at once
        BluetoothController.getInstance().setOnDisconnectionEvent(() -> {
            BluetoothController.getInstance().setDiscoverableState(true);
        });
        BluetoothController.getInstance().setOnConnectionEvent(() -> {
            BluetoothController.getInstance().setDiscoverableState(false);
            FavoriteFileController.getInstance().setAddress(BluetoothController.getInstance().getConnectedDevices().get(0).getAddress());
        });

        bluetoothUI.setBluetoothController(BluetoothController.getInstance());
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
                BluetoothController.getInstance().setDiscoverableState(true);
            }
        }else {
            //start over. No devices should be connected before this program starts running anyways.
            BluetoothController.getInstance().disconnectAllExcept("");
            BluetoothController.getInstance().setDiscoverableState(true);
        }
    }

    /**
     * Used for debug purposes only.
     */
    private void debugPrintConstants() {
        logger.debug("All Constants: ");
        for (Method m : Constants.class.getMethods()) {
            try {
                if (m.getName().equals("toString") || m.getName().equals("hashCode") || m.getName().equals("getClass")) {
                    continue;
                }
                logger.debug(m.getName() + ": " + m.invoke(Constants.getInstance()) + "(" + m.getReturnType().getName() +")");
            } catch (Exception ignored) {}
            
        }
    }
}
