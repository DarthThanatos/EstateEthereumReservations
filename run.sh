mkdir bin
mkdir lib
find -name "*.java" > sources
./gradlew build
./gradlew copyRuntimeLibs
javac -d bin -cp "lib/*:bin" @sources
java -cp "bin;lib/*" main.Main
# java -cp "out/production/classes:lib/*" main.Main
