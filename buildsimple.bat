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
call mvn -U clean install
if %errorlevel% neq 0 (
	ECHO "**** BUILD FAILED"
	CD %CURDIR%
	exit /b %errorlevel%
)
COPY target\sonar-dotnet-projectbuilder-*.jar  %DEST%
