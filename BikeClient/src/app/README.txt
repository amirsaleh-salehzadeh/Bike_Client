1- LOGIN SERVLET: will be called after the login button pressed. checks the uname and pass
CREATES A SESSION AND KEEPS DATA IN. Then it redirects to the next screen.
2- APPENTITY: Is the only class expressing the data in the system
3- CHECKPOINTCONFIGSERVLET: only pings once and send the ip address to the screen.
4- CHECKPOINTRUNSERVLET: runs the tread which reads the info from the reader and sends data to the next screen
It also runs the thread for the autocommit.
5- CLIENTAPPDAO: 
 a) reads all tags for a specific race: public static ArrayList<RideEntity> getAllTags(String raceNo)
 b) public static String exportCSV(String raceNo)
 c) for a dtaggered race, updates all times: public static void updateAllTimes(String raceNo, String riderGroup)
6- GETSERVICE: all webservices are here. The pages call them using ajax
7- LOGOUT SERVLET: kills all alive sessions.
8- THREAD COMMIT: commit the CSV file for the period of autocommit interval.
9- THREAD SUBMIT LINE: reads tags from the reader and using a hashmap prevents duplicate values based on the 
delay time.

CAUTION:
**there is a boolean variable in the CLIENTAPPDAO called isLinux: true if the app must be compiled for the linux, false for the windows
**in the case of windows: create the following folders:
C:\BikeFiles
C:\BikeFiles\conf
C:\BikeFiles\race
in the case of linux: create the following folders:
/home/pi/BikeFiles
/home/pi/BikeFiles/race/
/home/pi/BikeFiles/conf/
** in the linux version: the user and TOMCAT user must be authorised to manipulate data in the mentioned folders