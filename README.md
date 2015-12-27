# itp-notificator
Application to send SMS and/or Email to clients, reminding them of their upcoming ITP.

############## Prerequisites ####################

1- Install latest Java JDK
2- Install Maven
3- Make sure the environment variables are defined correctly
4- Modify the section of the dailyScheduler.bat & backUpDatabase.bat files to the DIRECTORY of this file
5- Create the Windows Task Scheduler to run the .bat files daily
 	OR: Import the existing DailyScheduler_ItpNotificator_WindowsScheduledTask.xml in Windows Task Scheduler
6- Define the properties in the \itp-notificator\src\main\resources\config.properties file


############## To check once above are finished ###############

1- GUI starts properly
2- Daily Scheduler starts
3- Logs are created in "logs" dir
4- BackUps are created in "backup" dir