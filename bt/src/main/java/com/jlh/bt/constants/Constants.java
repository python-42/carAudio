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
    public final int MENU_TEXT_WIDTH() {return 25;}
    public final int MUSIC_DETAIL_TEXT_WIDTH() {return 20;}
    public final int MENU_MAX_VISIBLE_TRACK_COUNT() {return 10;}

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
    //steering wheel
    public abstract int LEFT_BUTTON();
    public abstract int RIGHT_BUTTON();
    public abstract int UP_BUTTON();
    public abstract int DOWN_BUTTON();
    public abstract int OK_BUTTON(); 
    public abstract int SKIP_BUTTON();
    public abstract int PREV_BUTTON();
    public abstract int VOLUME_INCREMENT_BUTTON();
    public abstract int VOLUME_DECREMENT_BUTTON();
    public abstract int MUTE_BUTTON();

    //center control panel
    public abstract int SHUFFLE_BUTTON();
    public abstract int EQUALIZER_BUTTON();
    public abstract int RADIO_BUTTON();
    //secondary mute button - need to determine if this has a different ID or not
    public abstract int MEDIA_BUTTON();
    //clock button retains OEM functionality
    public abstract int PHONE_BUTTON();
    //menu button retains OEM functionality

    public abstract int BUTTON_1();
    public abstract int BUTTON_2();
    public abstract int BUTTON_3();
    public abstract int BUTTON_4();
    public abstract int BUTTON_5();
    public abstract int BUTTON_6();
    public abstract int BUTTON_7();
    public abstract int BUTTON_8();
    public abstract int BUTTON_9();
    public abstract int BUTTON_0();
}
