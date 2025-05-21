package com.jlh.bt;

public class Constants {
    
    //dbus
    public static final String BUSNAME = "org.bluez";
    public static final String COMMON_PATH_PREFIX = "/org/bluez/hci0/dev_";
    public static final String PLAYER_SUFFIX = "/player0";

    //favorite device
    public static final String FAVORITE_FILE_NAME = "/home/dodge/favoriteAddr";
    public static final int FAVORITE_CONNECTION_TIMEOUT = 1000;

    //volume
    public static final int VOLUME_RESET_PERCENTAGE = 50;
    public static final int VOLUME_CHANGE_PERCENTAGE = 5;

    //ui
    public static final String FXML_FILENAME = "main.fxml";

    //logging
    public static final String LOG_LEVEL_CLASSES_PROPERTY = "org.slf4j.simpleLogger.log.com.jlh.bt";
    public static final String LOG_FILE_PATH = "/home/dodge/java-bt.log";
    public static final String LOG_LEVEL = "debug";

    //menu css
    public static final String FOCUSED_CSS = "-fx-border-color: white";
    public static final String UNFOCUSED_CSS = "-fx-border-color: black";

    //onboard
    public static final String ONBOARD_MEDIA_DIRECTORY = "/home/dodge/media";
    public static final String ONBOARD_MEDIA_FILE_EXTENSION = "mp3";
    public static final String ONBOARD_MEDIA_ALBUM_ART_TYPE = "png";
    public static final double ONBOARD_MEDIA_MAX_VOLUME = 1;

}
