package com.jlh.bt.hardware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO this class
public class Button {

    private final Logger logger;

    public Button() {
        logger = LoggerFactory.getLogger(this.getClass());
        logger.debug("Button created");
    }

    public void setEventHandler(Runnable handler, String can) {
    }

}
