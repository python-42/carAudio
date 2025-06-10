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

    @Override
    public int PREV_BUTTON() {return -1;}

    @Override
    public int SKIP_BUTTON() {return -1;}

    @Override
    public int VOLUME_DECREMENT_BUTTON() {return -1;}

    @Override
    public int VOLUME_INCREMENT_BUTTON() {return -1;}

    @Override
    public int MUTE_BUTTON() {return -1;}

    @Override
    public int M_BUTTON() {return -1;}

    @Override
    public int SHUFFLE_BUTTON() {return -1;}

    @Override
    public int EQUALIZER_BUTTON() {return -1;}

    @Override
    public int RADIO_BUTTON() {return -1;}

    @Override
    public int MEDIA_BUTTON() {return -1;}

    @Override
    public int PHONE_BUTTON() {return -1;}

    @Override
    public int BUTTON_1() {return -1;}

    @Override
    public int BUTTON_2() {return -1;}

    @Override
    public int BUTTON_3() {return -1;}

    @Override
    public int BUTTON_4() {return -1;}

    @Override
    public int BUTTON_5() {return -1;}

    @Override
    public int BUTTON_6() {return -1;}

    @Override
    public int BUTTON_7() {return -1;}

    @Override
    public int BUTTON_8() {return -1;}

    @Override
    public int BUTTON_9() {return -1;}

    @Override
    public int BUTTON_0() {return -1;}
}