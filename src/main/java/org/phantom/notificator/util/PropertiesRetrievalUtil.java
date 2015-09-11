package org.phantom.notificator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Victor on 26/08/2015.
 * This class loads properties specified in the config.properties file.
 */
public class PropertiesRetrievalUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesRetrievalUtil.class);
    public final static Properties PROPERTIES = initProperties();

    public static String getProperty(String propertyName) {
        return PROPERTIES.getProperty(propertyName);
    }

    private static Properties initProperties() {
        Properties prop = new Properties();
        String configFilename = "config.properties";
        InputStream input = null;

        try {
            input = PropertiesRetrievalUtil.class.getClassLoader().getResourceAsStream(configFilename);
            if (input == null) {
                String errorMessage = "Sorry, unable to find " + configFilename;
                LOGGER.error(errorMessage);
                throw new ExceptionInInitializerError(errorMessage);
            }

            //load a properties file from class path, inside static method
            prop.load(input);
            return prop;

        } catch (IOException exception) {
            LOGGER.error("Unable to read config file {}. Error: {}", configFilename, exception.getMessage());
            throw new ExceptionInInitializerError(exception.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOGGER.error("Unable to close input stream. Error: {}", e.getMessage());
                }
            }
        }
    }
}
