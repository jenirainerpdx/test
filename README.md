# sockets

##Notes 

There are some documentation and design artifacts for your consideration under docs
    
    - I use plantuml and there is an intellij plugin for that which allows you to look at the diagrams within the ide, if you like.
    - Otherwise, you can grab the syntax and take it to: plantuml.com - there is a rendering screen near the bottom of the page.
    - There is also a digest of the requirements with my assumptions and clarifications around them in Requirements.md
    - A class diagram is also included 


## Starter build framework for the coding challenge

First, you do not need to use this starter framework for your project.
If you would rather use a different build system (maven, javac, ...)
you are free to so long as you provide clear commands to build your
project and start your com.jentest.sockets.server.  


## Install Java

This coding challenge is in Java so it is recommended you install Java
1.11 from Oracle or OpenJDK. 


## Gradle or MVN

You can use either an gradlew or mvn with pom.xml


### Project Layout

All source code should be located in the `src/main/java` folder.
If you wish to write any tests (not a requirement) they should be
located in the `src/test/java` folder.



### Running your application from the command line

You first must create a shadow jar file.  This is a file which contains your project code and all dependencies in a single jar file.  To build a shadow jar from your project run `./gradlew shadowJar`.  This will create a `coding-challenge-shadow.jar` file in the `build/libs` directory.

You can then start your application by running the command
`java -jar ./build/libs/coding-challenge-shadow.jar`

## IDEA

You are free to use whichever editor or IDE you want providing your
projects build does not depend on that IDE.  Most of the Java
developers at New Relic use IDEA from
[JetBrains](https://www.jetbrains.com/).  JetBrains provides
a community edition of IDEA which you can download and use without
charge.
