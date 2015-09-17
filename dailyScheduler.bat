ECHO Incepem sa trimitem Email/SMS clientilor Vector Truck Management...
:: Put below the path to the DIRECTORY of the dailyScheduler.bat file
F:
cd \Projects\itp-notificator
:: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
cmd /k mvn -e compile exec:java -Dexec.mainClass="org.phantom.notificator.schedulers.DailyScheduler"
ECHO Programul a terminat de trimis mesaje clientilor!
