md bin
md lib
dir /s /B *.java > sources
call gradle build
call gradle copyRuntimeLibs
javac -d bin -cp "lib/*;bin" @sources
java -cp "bin;lib/*" extra.CustomTest
rem java -cp "out/production/classes;lib/*" extra.CustomTest
