ECHO Efectuam copia de rezerva a bazei de date Vector Truck Management...
:: Put below the path to the DIRECTORY of the dailyScheduler.bat file
F:
cd \Projects\itp-notificator
:: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
cmd /k mvn -e compile exec:java -Dexec.mainClass="org.phantom.notificator.util.BackUpDatabaseUtil"
ECHO Programul a terminat de copiat baza de date!