
FROM openjdk:21
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

COPY ./target/*.war ./app.war

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]