package com.jlh.bt.onboard.menu;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import com.jlh.bt.Constants;
import com.jlh.bt.onboard.media.Track;
import com.jlh.bt.util.Pair;

public class MenuFactory {

    private MenuFactory() {}

    private final static FileFilter isAudioFile = (file) -> {
        if (file.isDirectory()) {
            return false;
        }
        int index = file.getName().lastIndexOf('.');
        return file.getName().substring(index).equals(Constants.ONBOARD_MEDIA_FILE_EXTENSION);
    };

    private final static FileFilter isDirectory = (file) -> {
        return file.isDirectory();
    };

    public static Pair<Menu, Playlist> constructHighLevelMenu(File directory) {
        // get all subdirectories
        File[] files = directory.listFiles(isDirectory);

        // get the genre and artist name based on the file path
        String playlistName = directory.getName();
        String menuName = String.format("%s - %s", directory.getParent(), playlistName);

        List<MenuItem> menuItems = new ArrayList<>(); // list of menu items which will compose this menu

        ArrayList<Playlist> playlistList = new ArrayList<>(files.length);
        List<Menu> menuList = new ArrayList<>(files.length);

        // construct each
        for (File file : files) {
            Pair<Menu, Playlist> pair;
            if (file.listFiles(isDirectory).length == 0) {
                // if there are no subdirectories, we have reached the album layer.
                // construct an album
                pair = constructAlbumMenu(directory);
            } else {
                pair = constructHighLevelMenu(directory);
            }

            menuItems.add(new MenuItem(file.getName(), pair.getKey(), pair.getValue(), 0));
            playlistList.add(pair.getValue());

            menuList.add(pair.getKey()); // maintain a reference to each menu so we can set the parent
        }

        Menu menu = new Menu(menuItems, menuName);

        for (Menu subMenu : menuList) {
            // set the parent for all submenus
            subMenu.setParentMenu(menu);
        }

        // this playlist contains all of the songs for this category (artist, genre,
        // etc)
        return new Pair<Menu, Playlist>(menu, new Playlist(playlistName, playlistList));
    }

    /**
     * Get the menu associated with a specific album
     * 
     * @param albumDirectory The directory containing songs
     * @param parentMenu     The parent menu for this menu
     * @return The menu associated with a specific album
     */
    public static Pair<Menu, Playlist> constructAlbumMenu(File albumDirectory) {
        File[] files = albumDirectory.listFiles(isAudioFile);
        List<Track> trackList = new ArrayList<>(files.length);

        String album = albumDirectory.getName();
        String artist = albumDirectory.getParent();

        for (File file : files) {
            trackList.add(
                    new Track(
                            file.getName().replace("." + Constants.ONBOARD_MEDIA_FILE_EXTENSION, ""),
                            artist,
                            album,
                            file));
        }

        Playlist playlist = new Playlist(albumDirectory.getName(), trackList);
        List<MenuItem> menuList = new ArrayList<>(playlist.getTrackCount());

        for (int i = 0; i < trackList.size(); i++) {
            menuList.add(new MenuItem(trackList.get(i).name(), null, playlist, i));
        }

        return new Pair<Menu, Playlist>(
                new Menu(menuList, String.format("%s - %s", artist, album)),
                playlist);
    }

}
