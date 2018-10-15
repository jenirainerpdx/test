# coding-challenge

##Notes to the Reviewer

1.  Project was completed using the build framework and gradle.
  - Due to this, you don't actually need to know where Main is, because you can simply 
  start the project using `java -jar ./build/libs/coding-challenge-shadow.jar`
  - For review purposes, the point of entry for the application (main method) is in com.newrelic.codingchallenge.server.SocketManager
2.  There are some documentation and design artifacts for your consideration under nr_sockets/docs
    - I use plantuml and there is an intellij plugin for that which allows you to look at the diagrams within the ide, if you like.
    - Otherwise, you can grab the syntax and take it to: plantuml.com - there is a rendering screen near the bottom of the page.
    - There is also a digest of the requirements with my assumptions and clarifications around them in Requirements.md
    - A class diagram is also included 
3. This was the best coding challenge I have done in my years of interviewing in the industry.  There were many ways that I 
could have taken this and many avenues I would have liked to explore, given more time.  But, with only 8 hours (and I probably spent closer to 12), 
I had to prioritize.  This challenge brought to the spotlight the interesting challenges of: 
  - How decoupling with messaging/queues can enhance throughput
  - Threading and how we manage concurrency with multiple data flows and aggregation
    of those processing streams
    
To be very transparent: I have been deeply entrenched in some tightly coupled java 6 code that, while challenging, 
has left me wondering if perhaps Java *is* too bloated for most purposes.  As I worked through this exercise, I was focused 
on reminding myself that my Object Oriented bias doesn't always play well.  Sometimes, there are functional needs.  
Sometimes, OO is not the right tool.  I think that there were places in this challenge where that may have been true.  In any 
case, it has prompted me to resume my work on clojure to expand my understanding of where functional is more expedient.

             


Coding Challenge Build Framework

## Starter build framework for the coding challenge

First, you do not need to use this starter framework for your project.
If you would rather use a different build system (maven, javac, ...)
you are free to so long as you provide clear commands to build your
project and start your com.newrelic.codingchallenge.server.  Failure to do so will invalidate your
submission.


## Install Java

This coding challenge is in Java so it is recommended you install Java
1.8 from Oracle.


## Gradle

The build framework provided here uses gradle to build your project
and manage your dependencies.  The `gradlew` command used here will
automatically download gradle for you so you shouldn't need to install
anything other than java.


### Project Layout

All source code should be located in the `src/main/java` folder.
If you wish to write any tests (not a requirement) they should be
located in the `src/test/java` folder.

A starter `Main.java` file has been provided in the `com/newrelic/codingchallenge` package under `src/main/java`.


### Dependencies

If your project has any dependencies you can list them in the
`build.gradle` file in the `dependencies` section.


### Building your project from the command line

To build the project on Linux or MacOS run the command `./gradlew build` in a shell terminal.  This will build the source code in
`src/main/java`, run any tests in `src/test/java` and create an output
jar file in the `build/libs` folder.

To clean out any intermediate files run `./gradlew clean`.  This will
remove all files in the `build` folder.


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

If you are planning to use IDEA you can generate the IDEA project files
by running `./gradlew idea` and directly opening the project folder
as a project in idea.

