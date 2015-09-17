package org.phantom.notificator.util;

import org.h2.tools.Backup;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;

/**
 * Created by Victor on 15/09/2015.
 */
// TODO: vic: When should this run?? (before starting ItpNotif, after starting ItpNotif, scheduled time of day etc.)
public class BackUpDatabaseUtil {

    public static final String BACKUP_TYPE = ".zip";
    private static final String BACKUP_DIRECTORY = "backups";
    private static final String PATH_SEPARATOR = "\\";
    private static final String BACKUP_NAME_PREFIX = "vectorDB_backup_";
    public static final String BACKUP_DIRECTORY_AND_NAME_PREFIX = BACKUP_DIRECTORY + PATH_SEPARATOR + BACKUP_NAME_PREFIX;
    private static final Logger LOGGER = LoggerFactory.getLogger(BackUpDatabaseUtil.class);
    private static final String DB_NAME = "vectorDB";
    private static final String DB_SUFFIX = ".mv.db";
    private static final String PROD_DB_FILE_NAME = DB_NAME + DB_SUFFIX;
    private static final int MAX_BACKUP_LIMIT = 20;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("dd-MM-yy_hh_mm_ss");

    public static void main(String[] args) throws SQLException {

        LOGGER.info("---->>> Database Backup Started ... <<<----");

        String currentDateTimeString = getCurrentDateTimeString();
        String backupFileName = BACKUP_DIRECTORY_AND_NAME_PREFIX + currentDateTimeString + BACKUP_TYPE;
        String databaseDirectoryPath = getDirectoryToFile(PROD_DB_FILE_NAME);

        Backup.execute(backupFileName,
                databaseDirectoryPath,
                DB_NAME,
                false);

        LOGGER.info("---->>> Database backup completed!! New backup file is: {} <<<----", backupFileName);
        deleteOldestBackupIfLimitExceeded(backupFileName);
    }

    private static void deleteOldestBackupIfLimitExceeded(String backupFileName) {
        // Keep only MAX_BACKUP_LIMIT backups
        File backupDirectory = new File(BACKUP_DIRECTORY);

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
        int startIndexOfDate = BACKUP_NAME_PREFIX.length();
        String fileCreationDateString = fileName.substring(startIndexOfDate, fileName.indexOf(BACKUP_TYPE));
        LOGGER.debug("---->>> Date string is: {} <<<----", fileCreationDateString);
        return DATE_TIME_FORMATTER.parseLocalDateTime(fileCreationDateString);
    }

    private static String getCurrentDateTimeString() {
        return DATE_TIME_FORMATTER.print(new DateTime());
    }

    private static String getDirectoryToFile(String fileName) {
        URL resource = BackUpDatabaseUtil.class.getClassLoader().getResource(fileName);
        String absolutePath;

        if (resource != null) {
            absolutePath = resource.getPath();
            return absolutePath.substring(1, absolutePath.indexOf(fileName));
        } else {
            throw new RuntimeException("File " + fileName + " cannot be found. Check the file exists!");
        }
    }
}
