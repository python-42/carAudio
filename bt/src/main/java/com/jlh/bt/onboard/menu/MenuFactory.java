package com.jlh.bt.onboard.menu;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jlh.bt.constants.Constants;
import com.jlh.bt.onboard.media.Track;
import com.jlh.bt.util.Pair;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class MenuFactory {

    private MenuFactory() {}

    private static MenuFactory instance = null;

    public static MenuFactory getInstance() {
        if (instance == null) {
            instance = new MenuFactory();
        }

        return instance;
    }

    private final Logger logger = LoggerFactory.getLogger(MenuFactory.class);
    private final Constants CONSTANTS = Constants.getInstance();

    private final FileFilter isAudioFile = (file) -> {
        if (file.isDirectory()) {
            return false;
        }
        int index = file.getName().lastIndexOf('.');
        return file.getName().substring(index).equals(CONSTANTS.ONBOARD_MEDIA_FILE_EXTENSION());
    };

    private final HashMap<String, byte[]> albumArtCache = new HashMap<>();
    private final HashMap<String, Playlist> artistPlaylistMap = new HashMap<>();
    private final HashMap<String, Playlist> genrePlaylistMap = new HashMap<>();
    
    public Pair<Menu, Playlist> constructHighLevelMenu(File directory) {
        Playlist allSongs = new Playlist("All Songs", loadMusicFiles(directory));

        Menu rootMenu = new Menu(
            List.of(
                new MenuItem("Artist", getCategoryMenu(artistPlaylistMap, "Artists"), allSongs, 0),
                new MenuItem("Genre", getCategoryMenu(genrePlaylistMap, "Genres"), allSongs, 0)
            ), 
            "Onboard Music"
        );

        return new Pair<Menu,Playlist>(rootMenu, allSongs);
    }

    /**
     * Used to get either the artist menu or genre menu. 
     * @param map the hash map to traverse to create the menus
     * @param title the title of the parent menu
     * @return Menu object with the provided title
     */
    private Menu getCategoryMenu(HashMap<String, Playlist> map, String title) {
        List<MenuItem> menuItems = new ArrayList<>(map.size());

        for (Entry<String, Playlist> entry : map.entrySet()) {
            Playlist playlist = entry.getValue();

            //create menu displaying an artists discography or all songs in a genre
            List<MenuItem> subMenuItems = new ArrayList<>(playlist.getTrackCount());
            for (int i = 0; i < playlist.getTrackCount(); i++) {
                subMenuItems.add(new MenuItem(playlist.getCurrentTrack().name(), null, playlist, i));
                playlist.nextTrack();
            }

            Menu bottomMenu = new Menu(subMenuItems, entry.getKey()); //bottom because there are no submenus
            
            //create menu item for menu displaying each artist
            menuItems.add(new MenuItem(entry.getKey(), bottomMenu, playlist, 0));
        }

        Menu menu = new Menu(menuItems, title);
        for (MenuItem item : menuItems) {
            item.getSubmenu().setParentMenu(menu);
        }

        return menu;
    }

    private List<Track> loadMusicFiles(File directory) {
        //clear maps of any stale data
        albumArtCache.clear();
        artistPlaylistMap.clear();
        genrePlaylistMap.clear();

        File[] audioFiles = directory.listFiles(isAudioFile);
        Arrays.sort(audioFiles);

        List<Track> trackList = new ArrayList<>(audioFiles.length);

        for (File f : audioFiles) {
            Mp3File metadata;
            try {
                metadata = new Mp3File(f);
                if (metadata.hasId3v2Tag()) {
                    ID3v2 tags = metadata.getId3v2Tag();
                    Track newTrack = new Track(tags.getTitle(), tags.getArtist(), tags.getAlbum(), convertGenreIdToString(tags.getGenre()), f);

                    trackList.add(newTrack);
                    updatePlaylistMap(artistPlaylistMap, newTrack.artist(), newTrack);
                    updatePlaylistMap(genrePlaylistMap, newTrack.genre(), newTrack);

                    if (
                        tags.getAlbumImage() != null 
                        && tags.getAlbumImageMimeType().equalsIgnoreCase(CONSTANTS.ONBOARD_MEDIA_ALBUM_ART_TYPE())
                        && !albumArtCache.containsKey(tags.getArtist() + "-" + tags.getAlbum())
                    ) {
                        logger.debug("Album " + tags.getArtist() + "-" + tags.getAlbum() + " has art, adding to map.");
                        albumArtCache.put(tags.getArtist() + "-" + tags.getAlbum(), tags.getAlbumImage());
                    }

                }else {
                    logger.info("Audio file " + f.getName() + " doesn't have Id3v2 tags, skipping file.");
                }
            } catch (UnsupportedTagException | InvalidDataException | IOException e) {
                logger.error("Encountered exception while parsing tags of audio file " + f.getName(), e);
            }
        }

        return trackList;
    }

    private void updatePlaylistMap(HashMap<String, Playlist> map, String key, Track track) {
        if (!map.containsKey(key)) {
            logger.debug("New track list created for " + key);
            map.put(key, new Playlist(key, List.of(track)));
        }else {
            map.get(key).appendTrack(track);
        }
    }

    /**
     * Get the album art associated with a particular track.
     * @param track Any track
     * @return byte array representing an image or null
     */
    public byte[] getAlbumArt(Track track) {

        return albumArtCache.get(track.artist() + "-" + track.album());
    }

    /**
     * Convert Id3v2 genre IDs into human-readable genres. 
     * Only a subset are converted because there's no chance im going to listen to most of the genres on the list. 
     * 
     * See https://exiftool.org/TagNames/ID3.html
     */
    private String convertGenreIdToString(int genre) {
        switch (genre) {
            case 2:
                return "Country";
            case 6:
                return "Grunge";
            case 9:
                return "Metal";
            case 15:
                return "Rap";
            case 17:
                return "Rock";
            case 66:
                return "New Wave";
            case 131:
                return "Indie / Alternative";
            default:
                return "Unknown";
        }
    }

}
