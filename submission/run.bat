@ECHO OFF
where java > nul
if %errorlevel%==1 (
    ECHO Java not found in path.
	PAUSE
    EXIT
)
REM java -version
java -jar Database.jar
PAUSE