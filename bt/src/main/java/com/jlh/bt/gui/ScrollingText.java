package com.jlh.bt.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.util.Duration;

public class ScrollingText extends Label {

    private int index = 0;

    public ScrollingText(String text, int length) {
        this.setTextOverrun(OverrunStyle.CLIP);
        if (text.length() < length) {
            this.setText(text);
        }else {
            index = 0;

            Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            if (index + length > text.length()) {
                                index = 0;
                            }
                            setText(text.substring(index, index + length));
                            index++;
                        }
                    }
                ),
                new KeyFrame(Duration.seconds(0.75))
            );

            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
    }    
}
