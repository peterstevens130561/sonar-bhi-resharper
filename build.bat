SET DEST="C:\Program Files\sonarqube-5.1.2\extensions\plugins"
SET CURDIR="%cd%"
SET SONAR="C:\Program Files\sonarqube-5.1.2\bin\windows-x86-64\StartSonar.bat"
ECHO *** Building sonar-mscover
ECHO ON
cd ../vstowrapper
call mvn clean install -q 
if %errorlevel% neq 0 (
	ECHO "**** BUILD FAILED"
	CD %CURDIR%
	exit /b %errorlevel%
)

cd ../sonar-dotnet-projectbuilder
call mvn -U clean install -q 
if %errorlevel% neq 0 (
	ECHO "**** BUILD FAILED"
	CD %CURDIR%
	exit /b %errorlevel%
)
COPY target\sonar-dotnet-projectbuilder-*.jar  %DEST%

cd ../sonar-mscover
call mvn clean install -q -DskipTests=true
if %errorlevel% neq 0 (
	ECHO "**** BUILD FAILED"
	CD %CURDIR%
	exit /b %errorlevel%
)
COPY target\sonar-mscover-plugin*.jar  %DEST%

CD %CURDIR%
call mvn clean install -q
if %errorlevel% neq 0 (
	ECHO "**** BUILD FAILED"
	CD %CURDIR%
	exit /b %errorlevel%
)

COPY target\sonar-resharper-plugin-1.0-SNAPSHOT.jar %DEST%
CD %CURDIR%
call %SONAR%
CD %CURDIR%

