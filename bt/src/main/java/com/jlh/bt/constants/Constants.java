package com.jlh.bt.constants;

import java.io.File;

public abstract class Constants {

    private static Constants instance = null;

    public static Constants getInstance() {
        if (instance == null) {
            if (new File("/home/jake/CarDev").exists()) {
                instance = new DevConstants();
            }else {
                instance = new ProdConstants();
            }
        }
        return instance;
    }

    public abstract boolean IS_PROD();
    
    //dbus
    public final String BUSNAME() {return "org.bluez";};
    public abstract String COMMON_PATH_PREFIX();
    public final String PLAYER_SUFFIX() {return "/player0";}

    //favorite device
    public abstract String FAVORITE_FILE_NAME();
    public abstract int FAVORITE_CONNECTION_TIMEOUT();

    //volume
    public final int VOLUME_RESET_PERCENTAGE() {return 50;}
    public final int VOLUME_CHANGE_PERCENTAGE() {return 5;}

    //ui
    public final String PLAYER_FXML_FILENAME() {return "player.fxml";}
    public final String ONBOARD_FXML_FILENAME() {return "onboard.fxml";}
    public abstract boolean IS_UI_FULLSCREEN();

    //logging
    public final String LOG_LEVEL_CLASSES_PROPERTY() {return "org.slf4j.simpleLogger.log.com.jlh.bt";}
    public abstract String LOG_FILE_PATH();
    public abstract String LOG_LEVEL();

    //menu css
    public final String FOCUSED_CSS() {return "-fx-border-color: white";}
    public final String UNFOCUSED_CSS() {return "-fx-border-color: black";}
    public final int ONBOARD_MENU_WIDTH() {return 440;}

    //onboard
    public abstract String ONBOARD_MEDIA_DIRECTORY();
    public final String ONBOARD_MEDIA_FILE_EXTENSION() {return "mp3";}    
    public final String ONBOARD_MEDIA_ALBUM_ART_TYPE() {return "image/png";}
    public final double ONBOARD_MEDIA_MAX_VOLUME() {return 1;}

    //CAN codes
    public abstract int LEFT_BUTTON();
    public abstract int RIGHT_BUTTON();
    public abstract int UP_BUTTON();
    public abstract int DOWN_BUTTON();
    public abstract int OK_BUTTON();
}
