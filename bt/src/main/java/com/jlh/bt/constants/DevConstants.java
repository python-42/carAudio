package com.jlh.bt.constants;

//visibility modifier omitted intentionally
class DevConstants extends Constants {

    @Override
    public boolean IS_PROD() {return false;}

    @Override
    public String COMMON_PATH_PREFIX() {return "/org/bluez/hci0/dev_";}

    @Override
    public String FAVORITE_FILE_NAME() {return "/home/jake/CarDev/favoriteAddr";}

    @Override
    public String FAVORITE_PLAYLIST_FILE() {return "/home/jake/CarDev/favoritePlaylist";}

    @Override
    public int FAVORITE_CONNECTION_TIMEOUT() {return 1000;}

    @Override
    public String LOG_FILE_PATH() {return "/home/jake/CarDev/java-bt.log";}

    @Override
    public String LOG_LEVEL() {return "debug";}

    @Override
    public boolean IS_UI_FULLSCREEN() {return false;}


    @Override
    public String ONBOARD_MEDIA_DIRECTORY() {return "/home/jake/Music/Rap";}

    @Override
    public int LEFT_BUTTON() {return 30;}

    @Override
    public int RIGHT_BUTTON() {return 32;}

    @Override
    public int UP_BUTTON() {return 17;}

    @Override
    public int DOWN_BUTTON() {return 31;}

    @Override
    public int OK_BUTTON() {return 57;}

    @Override
    public int PREV_BUTTON() {return 16;}

    @Override
    public int SKIP_BUTTON() {return 18;}

    @Override
    public int VOLUME_DECREMENT_BUTTON() {return 12;}

    @Override
    public int VOLUME_INCREMENT_BUTTON() {return 13;}

    @Override
    public int MUTE_BUTTON() {return 50;}

    @Override
    public int M_BUTTON() {return 41;}

    @Override
    public int SHUFFLE_BUTTON() {return 19;}

    @Override
    public int EQUALIZER_BUTTON() {return -1;}

    @Override
    public int RADIO_BUTTON() {return -1;}

    @Override
    public int MEDIA_BUTTON() {return -1;}

    @Override
    public int PHONE_BUTTON() {return -1;}

    @Override
    public int BUTTON_1() {return 2;}

    @Override
    public int BUTTON_2() {return 3;}

    @Override
    public int BUTTON_3() {return 4;}

    @Override
    public int BUTTON_4() {return 5;}

    @Override
    public int BUTTON_5() {return 6;}

    @Override
    public int BUTTON_6() {return 7;}

    @Override
    public int BUTTON_7() {return 8;}

    @Override
    public int BUTTON_8() {return 9;}

    @Override
    public int BUTTON_9() {return 10;}

    @Override
    public int BUTTON_0() {return 11;}

    @Override
    public int HASHTAG_BUTTON() {return 15;} //tab

    @Override
    public int STAR_BUTTON() {return -1;}
}