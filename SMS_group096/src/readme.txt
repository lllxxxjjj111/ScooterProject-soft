This readme file is to instruct you how to build and run the java program in cmd.

Preparation: 
You must insure the jdk and jre environment are 1.8.0_172 or below it.
Make sure the 8051 board has been connected by usb and watch which COM port it connects with and modify the relative part in  java file "src\boundary\Serial.java".

Execution:
Move to the dirctory where this readme.txt is in, then input command to compile:
        javac -Djava.ext.dirs=./lib @sourcelist.txt
Then input command to run:
        java -classpath .;./src; -Djava.ext.dirs=./lib boundary.Welcome
Now the program is running and you can start to operate on 8051 board.
