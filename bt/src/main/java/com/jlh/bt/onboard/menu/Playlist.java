package com.jlh.bt.onboard.menu;

import java.util.ArrayList;
import java.util.LinkedList;
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
        for (int i = 0; i < getTrackCount(); i++) {
            order.add(i);
        }
    }

    public void sortSongs() {
        songs.sort(new Track.TrackComparator());
    }

    public void toggleShuffle() {
        if (isShuffled) {
            isShuffled = false;
            setNaturalOrder();
        }else {
            isShuffled = true;
            shuffle();
        }
    }

    /**
     * Set the order of the tracks into their natural order (0, 1, 2, etc)
     */
    private void setNaturalOrder() {
        int id = songs.get(order.get(currentTrack)).id();
        order.clear();
        for (int i = 0; i < getTrackCount(); i++) {
            order.add(i);
        }
        fixCurrentTrack(id);
    }

    /**
     * Set the order of the tracks into a random order.
     */
    private void shuffle() {
        List<Integer> temp = copyList(order);

        order.clear();
        for (int i = 0; i < getTrackCount(); i++) {
            order.add(temp.remove(
                (int)(Math.random() * (temp.size()-1))
            ));
        }
        fixCurrentTrack(songs.get(currentTrack).id());
    }

    /**
     * After shuffling or unshuffling, the currentTrack variable will no longer
     * represent the position of the currently playing track. We restore it to the
     * correct value with this method.
     * 
     * @param trackID ID of the currently playing track.
     */
    private void fixCurrentTrack(int trackID) {
        for (int i = 0; i < getTrackCount(); i++) {
            if (songs.get(order.get(i)).id() == trackID) {
                currentTrack = i;
            }
        }
    }

    private List<Integer> copyList(List<Integer> toCopy) {
        List<Integer> temp = new LinkedList<>();
        for (int i : toCopy) {
            temp.add(i);
        }

        return temp;
    }

    /**
     * Move to the previous track. If at the first track, loop back to the end.
     */
    public void previousTrack() {
        if (currentTrack == 0) {
            currentTrack = getTrackCount() -1;
        }else {
            currentTrack--;
        }
    }

    /**
     * Move to the next track. If at the final track, loop back to zero. 
     */
    public void nextTrack() {
        if (currentTrack == getTrackCount() -1) {
            currentTrack = 0;
        }else {
            currentTrack++;
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
