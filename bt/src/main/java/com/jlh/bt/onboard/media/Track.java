package com.jlh.bt.onboard.media;

import java.io.File;

public record Track(int id, String name, String artist, String album, String genre, File file) {

    public static final Track ERROR = new Track(-1, "ERROR", "ERROR", "ERROR", "ERROR", null);
    public static final Track BLANK = new Track(0, "", "", "", "", null);
    public static final Track NOTHING = new Track(0, "No track playing", "", "", "", null);

    @Override
    public String toString() {
        return id+": " + name + "-" + artist + "-" + album;
    }
}
