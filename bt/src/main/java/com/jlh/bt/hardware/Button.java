package com.jlh.bt.hardware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalState;

public class Button {

    private final DigitalInput button;
    private final Logger logger;
    private static final Context context = Pi4J.newAutoContext();


    public Button(int pin) {
        button = context.digitalInput().create(pin);
        logger = LoggerFactory.getLogger(this.getClass());
    }

    public void setEventHandler(Runnable handler, DigitalState triggerState) {
        button.addListener( (event) -> {
            if (event.state() == triggerState) {
                logger.trace("Button " + button.getId() + " event handler triggered by state " + triggerState);
                handler.run();
            }
        });
    }

    public static void shutdown() {
        context.shutdown();
    }

}
