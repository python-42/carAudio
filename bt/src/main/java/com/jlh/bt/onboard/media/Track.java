package com.jlh.bt.onboard.media;

import java.io.File;

public record Track(String name, String artist, String album, String genre, File file) {

    public static final Track ERROR = new Track("ERROR", "ERROR", "ERROR", "ERROR", null);
    public static final Track BLANK = new Track("", "", "", "", null);
    public static final Track NOTHING = new Track("No track playing", "", "", "", null);

    @Override
    public String toString() {
        return name + "-" + artist + "-" + album;
    }
}
