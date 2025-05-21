package com.jlh.bt.constants;

//visibility modifier omitted intentionally
class DevConstants extends Constants {

    @Override
    public String COMMON_PATH_PREFIX() {return "/org/bluez/hci0/dev_";}

    @Override
    public String FAVORITE_FILE_NAME() {return "/home/jake/CarDev/favoriteAddr";}

    @Override
    public int FAVORITE_CONNECTION_TIMEOUT() {return 1000;}

    @Override
    public String LOG_FILE_PATH() {return "/home/jake/CarDev/java-bt.log";}

    @Override
    public String LOG_LEVEL() {return "debug";}

    @Override
    public String ONBOARD_MEDIA_DIRECTORY() {return "/home/jake/CarDev/media";}
}