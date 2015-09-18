package org.phantom.notificator;

/**
 * Created by Victor on 18/09/2015.
 */
public class Constants {
    // Define the Database files (PROD & DEV)
    public static final String PROD_DB_FILE_NAME = "vectorDB.mv.db";
    public static final String TESTING_DB_FILE_NAME = "vectorDBForTesting.mv.db";

    // Define the Database directory (where the above DB files are stored)
    public static final String DATABASE_DIRECTORY = "database";

    // Some path constants
    public static final String PATH_SEPARATOR = "/";

    // Define common strings needed by to construct the DB arguments for H2
    public static final String URL_PREFIX = "jdbc:h2:file:";
    public static final String URL_SUFFIX = ";IFEXISTS=TRUE;DB_CLOSE_DELAY=10;";
}
