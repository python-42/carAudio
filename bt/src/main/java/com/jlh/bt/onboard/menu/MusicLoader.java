package com.jlh.bt.onboard.menu;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jlh.bt.constants.Constants;
import com.jlh.bt.gui.NotificationSender;
import com.jlh.bt.onboard.media.Track;
import com.jlh.bt.util.Pair;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;

public class MusicLoader {

    private MusicLoader() {}

    private static MusicLoader instance = null;

    public static MusicLoader getInstance() {
        if (instance == null) {
            instance = new MusicLoader();
        }

        return instance;
    }

    private final Logger logger = LoggerFactory.getLogger(MusicLoader.class);
    private final Constants CONSTANTS = Constants.getInstance();

    private final FileFilter isAudioFile = (file) -> {
        if (file.isDirectory()) {
            return false;
        }
        int index = file.getName().lastIndexOf('.');
        return file.getName().substring(index+1).equals(CONSTANTS.ONBOARD_MEDIA_FILE_EXTENSION());
    };

    private final FileFilter isDirectory = (file) -> {
        return file.isDirectory();
    };

    private final HashMap<String, byte[]> albumArtCache = new HashMap<>();
    private final SortedMap<String, Playlist> artistPlaylistMap = new TreeMap<>();
    private final SortedMap<String, Playlist> genrePlaylistMap = new TreeMap<>();
    
    public Pair<Menu, Playlist> constructHighLevelMenu(File directory) {
        Playlist allSongs = new Playlist("All Songs", loadMusicFiles(directory));

        Menu artistMenu = getCategoryMenu(artistPlaylistMap, "Artists", (t) -> t.name() + " - " + t.album());
        Menu genreMenu = getCategoryMenu(genrePlaylistMap, "Genres", (t) -> t.name() + " - " + t.artist());

        Menu rootMenu = new Menu(
            List.of(
                new MenuItem("Artist", artistMenu, allSongs, 0),
                new MenuItem("Genre", genreMenu, allSongs, 0)
            ), 
            "Onboard Music"
        );

        artistMenu.setParentMenu(rootMenu);
        genreMenu.setParentMenu(rootMenu);

        return new Pair<Menu,Playlist>(rootMenu, allSongs);
    }

    /**
     * Used to get either the artist menu or genre menu. 
     * @param map the hash map to traverse to create the menus
     * @param title the title of the parent menu
     * @return Menu object with the provided title
     */
    private Menu getCategoryMenu(SortedMap<String, Playlist> map, String title, Function<Track, String> trackName) {
        List<MenuItem> menuItems = new ArrayList<>(map.size());

        for (Entry<String, Playlist> entry : map.entrySet()) {
            Playlist playlist = entry.getValue();
            playlist.sortSongs();

            //create menu displaying an artists discography or all songs in a genre
            List<MenuItem> subMenuItems = new ArrayList<>(playlist.getTrackCount());
            for (int i = 0; i < playlist.getTrackCount(); i++) {
                subMenuItems.add(new MenuItem(trackName.apply(playlist.getCurrentTrack()), null, playlist, i));
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

    private void getMusicFilesRecursive(File directory, List<File[]> files) {
        files.add(directory.listFiles(isAudioFile));

        for (File dir : directory.listFiles(isDirectory)) {
            getMusicFilesRecursive(dir, files);
        }
    }

    private List<Track> loadMusicFiles(File directory) {
        //clear maps of any stale data
        albumArtCache.clear();
        artistPlaylistMap.clear();
        genrePlaylistMap.clear();

        List<File[]> files = new ArrayList<>();
        getMusicFilesRecursive(directory, files);
        
        int size = 0;
        for (File[] f : files) {
            size += f.length;
        }

        File[] audioFiles = new File[size];
        int i = 0;
        for (File[] arr : files) {
            for (File f : arr) {
                audioFiles[i] = f;
                i++;
            }
        }

        List<Track> trackList = new ArrayList<>(audioFiles.length);
        logger.info(audioFiles.length + " mp3 files found.");

        HashMap<String, Object> albumTable = new HashMap<>(); //take advantage of hashmap constant time reads to store album names

        int id = 0;
        int albumArt = 0;
        for (File f : audioFiles) {
            Mp3File metadata;
            try {
                metadata = new Mp3File(f);
                if (metadata.hasId3v2Tag()) {
                    ID3v2 tags = metadata.getId3v2Tag();
                    
                    //for some reason this object is the only one which will do the release year properly
                    int year = 0;
                    try {
                        ID3v1 yearTag = metadata.getId3v1Tag();
                        year = parseNumericalString(yearTag.getYear());
                    }catch(Exception e) {
                        logger.info("Audio file " + f.getName() + " has issue with release year tagging. Using default year value.");
                    }
                    Track newTrack = new Track(
                        id, 
                        tags.getTitle(), 
                        tags.getArtist(), 
                        tags.getAlbum(), 
                        convertGenreIdToString(tags.getGenre()),
                        parseNumericalString(tags.getTrack()),
                        year,
                        f
                    );
                    id++;

                    trackList.add(newTrack);
                    updatePlaylistMap(artistPlaylistMap, newTrack.artist(), newTrack);
                    updatePlaylistMap(genrePlaylistMap, newTrack.genre(), newTrack);

                    if (albumTable.get(newTrack.album()) == null) {
                        albumTable.put(newTrack.album(), new Object());
                    }

                    if (
                        tags.getAlbumImage() != null 
                        && !albumArtCache.containsKey(tags.getArtist() + "-" + tags.getAlbum())
                    ) {
                        logger.debug("Album " + tags.getArtist() + "-" + tags.getAlbum() + " has art, adding to map.");
                        albumArt++;
                        albumArtCache.put(tags.getArtist() + "-" + tags.getAlbum(), tags.getAlbumImage());
                    }

                }else {
                    logger.info("Audio file " + f.getName() + " doesn't have Id3v2 tags, skipping file.");
                }
            } catch (Exception e) {
                logger.error("Encountered exception while parsing tags of audio file " + f.getName(), e);
            }
        }

        logger.info(trackList.size() + " audio files loaded successfully.");

        NotificationSender.sendInfoNotification(
            "File loading complete", 
            String.format(
                "%d files of %d successfully loaded.\nArtists: %d\nGenres: %d\nAlbums: %d (%d cover art images)", 
                trackList.size(), 
                audioFiles.length, 
                artistPlaylistMap.size(), 
                genrePlaylistMap.size(),
                albumTable.size(), 
                albumArt
            )
        );

        return trackList;
    }

    private int parseNumericalString(String str) {
        try {
            return Integer.parseInt(str);
        }catch (NumberFormatException e) {
            return 0;
        }
    }

    private void updatePlaylistMap(SortedMap<String, Playlist> map, String key, Track track) {
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
