package com.jlh.bt.onboard.media;

import java.io.File;

public record Track(String name, String artist, String album, File file) {

    public static final Track ERROR = new Track("ERROR", "ERROR", "ERROR", null);
    public static final Track BLANK = new Track("", "", "", null);
}
