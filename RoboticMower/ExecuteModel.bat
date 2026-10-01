java.exe ^
-Djava.util.logging.config.file=logging.properties ^
-Dfile.encoding=UTF-8
-Dstdout.encoding=UTF-8
-Dstderr.encoding=UTF-8
-p ".\bin;..\SysMLinJava\bin"
-XX:+ShowCodeDetailsInExceptionMessages
-m RoboticMower/roboticmower.RoboticMowerDomain