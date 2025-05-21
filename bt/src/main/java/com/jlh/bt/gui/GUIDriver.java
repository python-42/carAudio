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
    private static volatile PlayerController controller = null;

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
        ClassLoader resourceLoader = ClassLoader.getSystemClassLoader();
        //TODO font
        //Font.loadFont(resourceLoader.getResourceAsStream(""), 0);

        FXMLLoader loader = new FXMLLoader(resourceLoader.getResource(CONSTANTS.FXML_FILENAME()));
        stage.setScene(new Scene(loader.load()));
        controller = loader.getController();
        logger.debug("UI controller assigned");

        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();

        logger.info("UI resource loading complete, UI shown.");
    }

    public static synchronized PlayerController getUIController() {
        return controller;
    }
    
}
