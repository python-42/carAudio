package com.jlh.bt.os;

public record Track(String name, String artist, String album) {

    public static final Track ERROR = new Track("ERROR", "ERROR", "ERROR");
    public static final Track BLANK = new Track("", "", "");
}
