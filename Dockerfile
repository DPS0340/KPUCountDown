FROM adoptopenjdk/openjdk14

WORKDIR /code
COPY . .

RUN ./gradlew bootJar

ARG JAR_FILE=/code/build/libs/*

RUN cp ${JAR_FILE} /code/app.jar

ENTRYPOINT ["java", "-jar", "/code/app.jar"]
