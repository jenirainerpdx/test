# An interesting bit of code that exercises understanding of Parallelism, Concurrency and Decoupling.

##Notes

1.  You can run this project using `java -jar ./build/libs/coding-challenge-shadow.jar`.  The point of entry for the application (main method) is in com.jentest.sockets.server.SocketManager
2.  There are some documentation and design artifacts under nr_sockets/docs
    - I use plantuml and there is an intellij plugin for that which allows you to look at the diagrams within the ide, if you like.
    - Otherwise, you can grab the syntax and take it to: plantuml.com - there is a rendering screen near the bottom of the page.
    - There is a digest of the requirements with assumptions and clarifications around them in Requirements.md
    - A class diagram is also included 
    
            
### Building the project from the command line

To build the project on Linux or MacOS run the command `./gradlew build` in a shell terminal.  This will build the source code in
`src/main/java`, run any tests in `src/test/java` and create an output
jar file in the `build/libs` folder.

To clean out any intermediate files run `./gradlew clean`.  This will
remove all files in the `build` folder.


### Running the application from the command line

You first must create a shadow jar file.  This is a file which contains your project code and all dependencies in a single jar file.  To build a shadow jar from your project run `./gradlew shadowJar`.  This will create a `coding-challenge-shadow.jar` file in the `build/libs` directory.

You can then start your application by running the command
`java -jar ./build/libs/coding-challenge-shadow.jar`


