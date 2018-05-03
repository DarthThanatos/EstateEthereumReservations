mkdir bin
mkdir lib
find -name "*.java" > sources
export PATH=$PATH:$HOME/gradle/gradle-4.7/bin
echo $PATH
gradle build
gradle copyRuntimeLibs
javac -d bin -cp "lib/*:bin" @sources
java -cp "bin:lib/*" main.Main
# java -cp "out/production/classes:lib/*" main.Main
