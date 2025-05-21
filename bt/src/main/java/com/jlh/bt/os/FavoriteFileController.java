package com.jlh.bt.os;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jlh.bt.constants.Constants;

/**
 * Read and write values to the favorites file. 
 */
public class FavoriteFileController {
    
    private static FavoriteFileController INSTANCE = null;

    public static FavoriteFileController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FavoriteFileController();
        }

        return INSTANCE;
    }

    private final File file;
    private final Logger logger;
    private final Constants CONSTANTS;

    private FavoriteFileController() {
        logger = LoggerFactory.getLogger(this.getClass());
        CONSTANTS = Constants.getInstance();
        file = new File(CONSTANTS.FAVORITE_FILE_NAME());
    }

    /**
     * Get the MAC address of the favorite device as read from the favorite file.
     * If the favorite device doesn't exist, the return value is null.
     * 
     * @return String containing the MAC address of the favorite device or null.
     */
    public String getAddress() {
        try (Scanner sc = new Scanner(file)) {
            return sc.nextLine();
        } catch (FileNotFoundException e) {
            logger.error("Favorites file not found.", e);
            return null;
        }
    }

    /**
     * Write the given String to the favorites file. This String should be the MAC address of a bluetooth device. 
     * 
     * @param addr New favorite device MAC address.
     */
    public void setAddress(String addr) {
        try (FileWriter writer = new FileWriter(file, false)) {
            logger.info("Attempting to write new favorite address " + addr + " to favorites file.");
            writer.append(addr);
            writer.flush();
        } catch (IOException e) {
            logger.error("IOException occurred while writing new favorite address to file.", e);
        }
    }
}
