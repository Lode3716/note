FROM adoptopenjdk/openjdk15

ARG JAR_FILE=target/note-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} note.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","/note.jar"]
