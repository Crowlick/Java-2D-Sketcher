@cd/d "%~dp0"
"./jdk-17.0.8/bin/java.exe" "-Djava.library.path=./lib/natives/" -cp "./lib/jars/lwjgl.jar;./lib/jars/lwjgl_util.jar;./lib/jars/slick-util.jar;./lib/jars/json-java.jar;./bin" main.Main
pause