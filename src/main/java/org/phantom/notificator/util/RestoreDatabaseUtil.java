package org.phantom.notificator.util;

import org.h2.tools.Restore;
import org.phantom.notificator.Constants;

import java.io.File;
import java.util.Scanner;

/**
 * Created by Victor on 15/09/2015.
 */
public class RestoreDatabaseUtil {

    public static void main(String[] args) {
        // Scanner for taking user input
        Scanner scanner = new Scanner(System.in);

        // Display available backups
        File backupDir = new File(Constants.BACKUP_DIRECTORY);

        if (!backupDir.exists() || backupDir.listFiles().length == 0) {
            System.out.println("There are no backups available!!");
        } else {
            File[] allBackups = backupDir.listFiles();
            printAllBackups(allBackups);
            int backupIndexToUse = getUserInput(scanner, allBackups);
            File backupToUse = allBackups[backupIndexToUse];
            System.out.println("Restoring DB from file: " + backupToUse.getAbsolutePath());
            Restore.execute(backupToUse.getAbsolutePath(),
                    Constants.DATABASE_DIRECTORY,
                    Constants.PROD_DB_NAME);
        }
    }

    private static int getUserInput(Scanner scanner, File[] allBackups) {
        int backupIndexToUse = -1;
        while (backupIndexToUse < 0 || backupIndexToUse > allBackups.length - 1) {
            System.out.print("Input the backup number you want to use (Range: 0 to " + (allBackups.length - 1) + ") :");
            try {
                String userInput = scanner.next();
                backupIndexToUse = Integer.parseInt(userInput);
                if (backupIndexToUse < 0 || backupIndexToUse > allBackups.length - 1) {
                    System.out.println("Invalid selection!! Please try again!!");
                    backupIndexToUse = -1;
                }
            } catch (NumberFormatException exception) {
                System.out.println("Input is not a number! Please try again or press Ctrl + C to exit.");
            }
        }
        return backupIndexToUse;
    }

    private static void printAllBackups(File[] allBackups) {
        System.out.println("******** NOTE: Backups have the following format : " +
                Constants.BACKUP_NAME_PREFIX + BackUpDatabaseUtil.DATE_TIME_FORMAT);

        System.out.println("The list of available backups is: ");

        for (int i = 0; i < allBackups.length; i++) {
            System.out.println(i + " - " + allBackups[i].getName());
        }
    }
}
