@echo off
rem Springboot build with gradle
echo ## gradlew.bat assemble
gradlew.bat assemble && cd .\build\libs && java -jar otel-sdk-springboot-01-project-0.0.1-SNAPSHOT.jar

rem Run SpringBoot
rem echo ## cd .\build\libs && java -jar otel-sdk-springboot-01-project-0.0.1-SNAPSHOT.jar
