package com.jlh.bt.constants;

//visibility modifier omitted intentionally
class ProdConstants extends Constants {

    @Override
    public boolean IS_PROD() {return true;}

    @Override
    public String COMMON_PATH_PREFIX() {return "/org/bluez/hci0/dev_";}

    @Override
    public String FAVORITE_FILE_NAME() {return "/home/dodge/favoriteAddr";}

    @Override
    public int FAVORITE_CONNECTION_TIMEOUT() {return 1000;}

    @Override
    public String LOG_FILE_PATH() {return "/home/dodge/java-bt.log";}

    @Override
    public String LOG_LEVEL() {return "debug";}

    @Override
    public boolean IS_UI_FULLSCREEN() {return true;}

    @Override
    public String ONBOARD_MEDIA_DIRECTORY() {return "/home/dodge/media";}

    @Override
    public int LEFT_BUTTON() {return -1;}

    @Override
    public int RIGHT_BUTTON() {return -1;}

    @Override
    public int UP_BUTTON() {return -1;}

    @Override
    public int DOWN_BUTTON() {return -1;}

    @Override
    public int OK_BUTTON() {return -1;}
}