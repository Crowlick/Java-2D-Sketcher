if not exist bin mkdir bin

./jdk1.1.8/bin/javac.exe -d bin -cp "./lib/jars/lwjgl.jar:./lib/jars/lwjgl_util.jar:./lib/jars/slick-util.jar:./lib/jars/json-java.jar"  src/Contraints/*.java  ./src/engineTester/*.java ./src/entities/*.java src/main/*.java src/renderEngine/*.java src/shaders/*.java src/toolBox/*.java

copy src/shaders/*.txt bin/shaders/
