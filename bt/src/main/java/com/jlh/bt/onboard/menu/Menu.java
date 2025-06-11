package com.jlh.bt.onboard.menu;

import java.util.ArrayList;
import java.util.List;

import com.jlh.bt.constants.Constants;
import com.jlh.bt.onboard.media.Playlist;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

public class Menu {
    
    private Menu parentMenu;
    private final List<MenuItem> items;
    private final Label title;
    private final Constants CONSTANTS = Constants.getInstance();

    private int focus = 0;

    private int minVisible = 0;
    private int maxVisible = CONSTANTS.MENU_MAX_VISIBLE_TRACK_COUNT();

    public Menu(List<MenuItem> items, String title) {
        this.items = new ArrayList<>();
        this.items.addAll(items);
        if (items.size() == 0) {
            this.items.add(MenuItem.NO_ITEMS_ITEM);   
        }

        this.items.get(focus).setFocused(true);

        this.title = new Label(title);
        this.title.setTextFill(Paint.valueOf("ffffff"));
    }

    public void focusDown() {
        items.get(focus).setFocused(false);
        if (focus != items.size() - 1) {
            focus++;
            if (focus >= maxVisible) {
                minVisible++;
                maxVisible++;
            }
        }
        items.get(focus).setFocused(true);
    }

    public void focusUp() {
        items.get(focus).setFocused(false);
        if (focus != 0) {
            focus--;
            if (focus < minVisible) {
                minVisible--;
                maxVisible--;
            }
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

        if (needsToScroll()) {
            HBox titleBox = new HBox();
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label scrollIndicator = new Label(getScrollIndicator());
            scrollIndicator.setTextFill(Paint.valueOf("ffffff"));

            titleBox.getChildren().addAll(title, spacer, scrollIndicator);
            children.add(titleBox);
        }else {
            children.add(title);
        }
        children.add(new Separator());

        if (needsToScroll()) {
            for (int i = minVisible; i < maxVisible; i++) {
                children.add(items.get(i).getUIComponent());
            }
        }else {
            //all of the items can be visible at once
            for (MenuItem item : items) {
                children.add(item.getUIComponent());
            }
        }

        rtn.setPrefWidth(Constants.getInstance().ONBOARD_MENU_WIDTH());
        return rtn;
    }

    /**
     * Determine if the additional logic required for scrolling items is required. 
     */
    private boolean needsToScroll() {
        return items.size() > CONSTANTS.MENU_MAX_VISIBLE_TRACK_COUNT();
    }

    private String getScrollIndicator() {
        if (minVisible > 0 && maxVisible < items.size()) {
            return "\u21F3"; // up down arrow
        }else if(minVisible > 0) {
            return "\u21E7"; //up arrow
        }else {
            return "\u21E9"; //down arrow
        }
    }

    public String getTitle() {
        return title.getText();
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
