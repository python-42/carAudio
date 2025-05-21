package com.jlh.bt.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jlh.bt.constants.Constants;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIDriver extends Application {

    private Logger logger;
    private Constants CONSTANTS;
    private static volatile BtPlayerController btController = null;
    private static volatile OnboardController onboardController  = null;

    private Stage stage;
    private Scene musicDetail;
    private Scene onboardMenu;

    public GUIDriver(String[] args) {
        Application.launch(args);
    }

    public GUIDriver() {
        logger = LoggerFactory.getLogger(this.getClass());
        CONSTANTS = Constants.getInstance();
    }

    @Override
    public void start(Stage stage) throws Exception {
        logger.info("UI resource loading started.");
        this.stage = stage;
        ClassLoader resourceLoader = ClassLoader.getSystemClassLoader();
        //TODO font
        //Font.loadFont(resourceLoader.getResourceAsStream(""), 0);

        FXMLLoader btLoader = new FXMLLoader(resourceLoader.getResource(CONSTANTS.PLAYER_FXML_FILENAME()));
        FXMLLoader onboardLoader = new FXMLLoader(resourceLoader.getResource(CONSTANTS.ONBOARD_FXML_FILENAME()));

        musicDetail = new Scene(btLoader.load());
        onboardMenu = new Scene(onboardLoader.load());

        btController = btLoader.getController();
        logger.debug("Bluetooth player UI controller assigned");
        onboardController = onboardLoader.getController();
        logger.debug("Onboard menu UI controller assigned");

        showMusicDetailScene();
        stage.setFullScreen(CONSTANTS.IS_UI_FULLSCREEN());
        stage.setFullScreenExitHint("");
        stage.show();

        logger.info("UI resource loading complete, UI shown.");
    }

    public void showMusicDetailScene() {
        stage.setScene(musicDetail);
    }

    public void showOnboardMenuScene() {
        stage.setScene(onboardMenu);
    }

    public static synchronized BtPlayerController getBluetoothUIController() {
        return btController;
    }

    public static synchronized OnboardController getOnboardUIController() {
        return onboardController;
    }
    
}
