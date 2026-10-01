java.exe ^
-Djava.util.logging.config.file=logging.properties ^
-Dfile.encoding=UTF-8 ^
-p ".\bin;.\neurophcore.jar;.\slf4japi.jar;.\slf4jnop.jar;.\visrecapi.jar;..\SysMLinJava\bin" ^
-XX:+ShowCodeDetailsInExceptionMessages ^
-m AIControlledDBSSystem/dbssystem.sensors.DBSSensorsContainer