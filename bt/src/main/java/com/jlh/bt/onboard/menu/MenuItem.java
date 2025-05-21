package com.jlh.bt.onboard.menu;

import com.jlh.bt.constants.Constants;
import com.jlh.bt.gui.ScrollingText;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class MenuItem {

    public final static MenuItem NO_ITEMS_ITEM = new MenuItem("No items", null, null, 0); 

    private final Constants CONSTANTS = Constants.getInstance();

    private final Menu submenu;
    private final HBox component;
    private final Playlist playlist;
    private final int index;
    
    public MenuItem(String name, Menu submenu, Playlist playlist, int index) {
        this.submenu = submenu;
        this.playlist = playlist;
        component = setupComponent(name); 

        this.index = index;
    }

    private HBox setupComponent(String name) {
        HBox box = new HBox();
        box.setMaxWidth(Double.MAX_VALUE);
        
        Label label = new ScrollingText(name, CONSTANTS.MENU_TEXT_WIDTH());
        label.setPadding(new Insets(0, 10, 0, 5));

        box.getChildren().add(label);
        if (submenu != null) {
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            box.getChildren().addAll(spacer, new Label(">"));
        }

        return box;
    } 
    
    public HBox getUIComponent() {
        return component;
    }

    public void setFocused(boolean focused) {
        if (focused) {
            component.setStyle(CONSTANTS.FOCUSED_CSS());
        }else {
            component.setStyle(CONSTANTS.UNFOCUSED_CSS());
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
