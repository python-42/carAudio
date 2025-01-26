package com.jlh.bt.onboard.menu;

import com.jlh.bt.Constants;

import javafx.scene.control.Label;

public class MenuItem {

    public final static MenuItem NO_ITEMS_ITEM = new MenuItem("No items", null, null, 0); 

    private final Menu submenu;
    private final Label component;
    private final Playlist playlist;
    private final int index;
    
    public MenuItem(String name, Menu submenu, Playlist playlist, int index) {
        component = new Label(name);
        this.submenu = submenu;
        this.playlist = playlist;

        this.index = index;
    }
    
    public Label getUIComponent() {
        return component;
    }

    public void setFocused(boolean focused) {
        if (focused) {
            component.setStyle(Constants.FOCUSED_CSS);
        }else {
            component.setStyle(Constants.UNFOCUSED_CSS);
        }
    }

    /**
     * Get the submenu which this button leads to. May be null. 
     * 
     * @return Menu object or null
     */
    public Menu getSubmenu() {
        return submenu;
    }

    /**
     * Get the playlist associated with this menu item. May be null. 
     * The caller should start playback from the current track. 
     * 
     * @return Playlist object or null.
     */
    public Playlist getPlaylist() {
        playlist.setIndex(index);
        return playlist;
    }

}
