package com.jlh.bt.onboard.menu;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class Menu {
    
    private Menu parentMenu;
    private final List<MenuItem> items;
    private final String title;

    private int focus = 0;

    public Menu(List<MenuItem> items, String title) {
        this.items = new ArrayList<>();
        this.items.addAll(items);
        if (items.size() == 0) {
            this.items.add(MenuItem.NO_ITEMS_ITEM);   
        }

        this.items.get(focus).setFocused(true);

        this.title = title;
    }

    public void focusDown() {
        items.get(focus).setFocused(false);
        if (focus != items.size() - 1) {
            focus++;
        }
        items.get(focus).setFocused(true);
    }

    public void focusUp() {
        items.get(focus).setFocused(false);
        if (focus != 0) {
            focus--;
        }
        items.get(focus).setFocused(true);
    }

    /**
     * Get the submenu which is associated with the currently focused item. 
     * @return Menu object or null
     */
    public Menu descend() {
        return items.get(focus).getSubmenu();
    }

    public boolean canDescend() {
        return descend() != null;
    }

    /**
     * Get the playlist associated with the current menu item
     * @return Playlist object or null
     */
    public Playlist getPlaylist() {
        return items.get(focus).getPlaylist();
    }

    public VBox getUIComponent() {
        VBox rtn = new VBox();
        ObservableList<Node> children = rtn.getChildren();

        for (MenuItem item : items) {
            children.add(item.getUIComponent());
        }

        return rtn;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Get the menu which owns this one. May be null
     * @return The parent menu
     */
    public Menu getParentMenu() {
        return parentMenu;
    }

    /**
     * Set the parent menu. 
     * 
     * If the parent menu already has a non-null value, 
     * this method will do nothing.
     * 
     * @param parentMenu
     */
    public void setParentMenu(Menu parentMenu) {
        if (this.parentMenu == null) {
            this.parentMenu = parentMenu;
        }
    }

    public boolean hasParentMenu() {
        return parentMenu != null;
    }
    
}
