javac -classpath .;../../VocalyzeSIVOX/bin/SI_VOX.jar -d ..\bin *.java

jar cmf META-INF/MANIFEST.MF ..\bin\Listor.jar ..\bin\*.class ..\ressources\img
pause