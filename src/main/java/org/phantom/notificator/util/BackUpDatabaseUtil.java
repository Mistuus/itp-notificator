package org.phantom.notificator.util;

import org.h2.tools.Backup;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.phantom.notificator.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.SQLException;

/**
 * Created by Victor on 15/09/2015.
 */
public class BackUpDatabaseUtil {

    public static final int MAX_BACKUP_LIMIT = 20;
    public static final String DATE_TIME_FORMAT = "dd-MM-yy_HH_mm_ss";
    private static final Logger LOGGER = LoggerFactory.getLogger(BackUpDatabaseUtil.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_TIME_FORMAT);

    public static void main(String[] args) throws SQLException {

        LOGGER.info("---->>> Database Backup Started ... <<<----");

        String currentDateTimeString = getCurrentDateTimeString();
        String backupFileName = Constants.BACKUP_DIRECTORY_AND_NAME_PREFIX + currentDateTimeString + Constants.BACKUP_TYPE;
        String databaseDirectoryPath = getDirectoryToFile();

        Backup.execute(backupFileName,
                databaseDirectoryPath,
                Constants.PROD_DB_NAME,
                false);

        LOGGER.info("---->>> Database backup completed!! New backup file is: {} <<<----", backupFileName);
        deleteOldestBackupIfLimitExceeded(backupFileName);
    }

    private static void deleteOldestBackupIfLimitExceeded(String backupFileName) {
        // Keep only MAX_BACKUP_LIMIT backups
        File backupDirectory = new File(Constants.BACKUP_DIRECTORY);

        File[] backups = backupDirectory.listFiles();
        if (backups == null) {
            LOGGER.info("---->>> No backups found!! <<<----");
            return;
        }

        LOGGER.info("---->>> Current number of backups is {} and MAX_BACKUP_LIMIT is {} <<<----",
                backups.length, MAX_BACKUP_LIMIT);

        if (backups.length > MAX_BACKUP_LIMIT) {

            LOGGER.info("---->>> MAX_BACKUP_LIMIT exceeded! Starting to delete oldest file. <<<----");
            File oldestFile = getOldestFile(backups);
            LOGGER.info("---->>> Oldest File out of all backups is {} <<<----", oldestFile);
            boolean isDeleted = oldestFile.delete();

            if (isDeleted) {
                LOGGER.info("---->>> Finished deleting oldest backup!! <<<----");
            } else {
                LOGGER.info("---->>> Could not delete oldest backup!! Try to delete manually. <<<----");
            }
        }


    }

    private static File getOldestFile(File[] backups) {
        File oldestFile = null;
        LocalDateTime oldestFileCreationTime = null;
        LocalDateTime backupFileCreationTime;

        for (File backup : backups) {
            if (oldestFile == null) {
                oldestFile = backup;
                oldestFileCreationTime = getCreationDate(backup);
            } else {
                backupFileCreationTime = getCreationDate(backup);
                if (backupFileCreationTime.isBefore(oldestFileCreationTime)) {
                    oldestFile = backup;
                    oldestFileCreationTime = backupFileCreationTime;
                    LOGGER.debug("---->>> New Oldest File is {} <<<----", oldestFile);
                }
            }
        }

        return oldestFile;
    }

    private static LocalDateTime getCreationDate(File file) {
        String fileName = file.getName();
        int startIndexOfDate = Constants.BACKUP_NAME_PREFIX.length();
        String fileCreationDateString = fileName.substring(startIndexOfDate, fileName.indexOf(Constants.BACKUP_TYPE));
        LOGGER.debug("---->>> Date string is: {} <<<----", fileCreationDateString);
        return DATE_TIME_FORMATTER.parseLocalDateTime(fileCreationDateString);
    }

    private static String getCurrentDateTimeString() {
        return DATE_TIME_FORMATTER.print(new DateTime());
    }

    private static String getDirectoryToFile() {
        File file = new File(Constants.PROD_DB_FILE_RELATIVE_PATH);
        String absolutePath;

        if (file.exists()) {
            absolutePath = file.getAbsolutePath();
            return absolutePath.substring(0, absolutePath.indexOf(Constants.PROD_DB_FILE_NAME));
        } else {
            throw new RuntimeException("File " + Constants.PROD_DB_FILE_RELATIVE_PATH + " cannot be found. Check the file exists!");
        }
    }
}
