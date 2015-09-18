package org.phantom.notificator;

import java.awt.*;

/**
 * Created by Victor on 18/09/2015.
 */
public class Constants {
    // Some path constants
    public static final String PATH_SEPARATOR = "/";

    // Define the Database directory (where the above DB files are stored)
    public static final String DATABASE_DIRECTORY = "database";

    // Define the Database files (PROD & DEV)
    public static final String PROD_DB_NAME = "vectorDB";
    public static final String PROD_DB_FILE_NAME = "vectorDB.mv.db";
    public static final String PROD_DB_FILE_RELATIVE_PATH = DATABASE_DIRECTORY + PATH_SEPARATOR + PROD_DB_FILE_NAME;
    public static final String TESTING_DB_FILE_NAME = "vectorDBForTesting.mv.db";

    // Backup Constants
    public static final String BACKUP_TYPE = ".zip";
    public static final String BACKUP_DIRECTORY = "backups";
    public static final String BACKUP_NAME_PREFIX = "vectorDB_backup_";
    public static final String BACKUP_DIRECTORY_AND_NAME_PREFIX = BACKUP_DIRECTORY + PATH_SEPARATOR + BACKUP_NAME_PREFIX;

    // Define common strings needed by to construct the DB arguments for H2
    public static final String URL_PREFIX = "jdbc:h2:file:";
    public static final String URL_SUFFIX = ";IFEXISTS=TRUE;";

    // GUI constants
    public static final Dimension PREFERRED_SIZE = new Dimension(500, 500);

    // Common string constants
    public static final String EMPTY_STRING = "";
    public static final String NEW_LINE = "\n";
    public static final String DOUBLE_NEW_LINE = "\n\n";
    public static final String SPACE = " ";
}
