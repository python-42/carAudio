package com.jlh.bt.onboard.menu;

import java.util.ArrayList;
import java.util.List;

import com.jlh.bt.onboard.media.Track;

/**
 * Represents a group of songs. 
 */
public class Playlist {

    private final String name;
    private final List<Integer> order;

    private final List<Track> songs;

    private int currentTrack = 0;
    private boolean isShuffled = false;

    public Playlist(String name, List<Track> list) {
        this.name = name;

        songs = new ArrayList<>(list.size());
        songs.addAll(list);

        order = new ArrayList<>(getTrackCount());
        setNaturalOrder();
    }

    public void toggleShuffle() {
        if (isShuffled) {
            setNaturalOrder();
        }else {
            shuffle();
        }
    }

    /**
     * Set the order of the tracks into their natural order (0, 1, 2, etc)
     */
    private void setNaturalOrder() {
        order.clear();
        for (int i = 0; i < getTrackCount(); i++) {
            order.add(i);
        }
    }

    /**
     * Set the order of the tracks into a random order.
     */
    private void shuffle() {
        List<Integer> temp = List.copyOf(order);

        order.clear();
        for (int i = 0; i < getTrackCount(); i++) {
            order.add(temp.remove(
                (int)(Math.random() * getTrackCount())
            ));
        }
    }

    /**
     * Move to the previous track. If at the first track, nothing happens.
     */
    public void previousTrack() {
        if (currentTrack != 0) {
            currentTrack--;
        }
    }

    /**
     * Move to the next track. If at the final track, loop back to zero. 
     */
    public void nextTrack() {
        if (currentTrack != getTrackCount() -1) {
            currentTrack = 0;
        }
    }

    /**
     * Set the current track index. 
     * This method is protected, meaning it can be accessed within this package only.
     * @param index The current track index to use
     */
    protected void setIndex(int index) {
        if (index > 0 && index < getTrackCount()) {
            currentTrack = index;
        }
    }

    public Track getCurrentTrack() {
        return songs.get(order.get(currentTrack));
    }

    public int getTrackCount() {
        return songs.size();
    }

    public String getName() {
        return name;
    }

    public void appendTrack(Track track) {
        songs.add(track);
        //we need to update the order list to include a number for our new track. 
        //new track will play at the end whether or not shuffle is enabled\
        order.add(order.size()); 
    }
        
}
