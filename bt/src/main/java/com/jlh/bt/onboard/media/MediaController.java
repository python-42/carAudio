package com.jlh.bt.onboard.media;

import com.jlh.bt.constants.Constants;
import com.jlh.bt.onboard.menu.Playlist;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MediaController {
    
    private MediaPlayer player = null;
    private Playlist playlist = null;
    private final Constants CONSTANTS;

    public MediaController() {
        CONSTANTS = Constants.getInstance();
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
        //TODO buffer current playlist?
        createNewMediaPlayer(playlist.getCurrentTrack());
    }

    public Track getCurrentTrack() {
        if (playlist != null) {
            return playlist.getCurrentTrack();
        }
        return null;
    }

    /**
     * Return the percent complete the track is.
     * @return double between 0 and 1 inclusive
     */
    public double getPercentageComplete() {
        if (player != null) {
            return player.getCurrentTime().toSeconds() / player.getStopTime().toSeconds();
        }
        return 0;
    }

    public void pause() {
        if (player != null) {
            player.pause();
        }
    }

    public void play() {
        if (player != null) {
            player.play();
        }
    }

    public void fastForward() {
        if (playlist != null) {
            playlist.nextTrack();
            createNewMediaPlayer(playlist.getCurrentTrack());
        }
    }

    public void rewind() {
        if (player != null) {
            player.seek(Duration.ZERO);
        }
    }

    public void previous() {
        if (playlist != null) {
            playlist.previousTrack();
            createNewMediaPlayer(playlist.getCurrentTrack());
        }
    }

    public void toggleShuffle() {
        playlist.toggleShuffle();
    }

    private void createNewMediaPlayer(Track media) {
        if (player != null) {
            player.stop();
            player.dispose();
        }
        player = new MediaPlayer(new Media(media.file().toURI().toString()));

        player.setOnEndOfMedia(this::endOfMediaHandler);

        player.setVolume(CONSTANTS.ONBOARD_MEDIA_MAX_VOLUME());
        player.setAutoPlay(true); // as soon as playback is ready we begin to play this track
    }

    private void endOfMediaHandler() {
        playlist.nextTrack();
        createNewMediaPlayer(playlist.getCurrentTrack());
    }

}
