FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/mvnw mvnw
COPY --from=build /app/pom.xml pom.xml
COPY --from=build /app/src src
RUN chmod +x mvnw
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]