FROM gradle:jdk17 as builder
WORKDIR /app
COPY . /app/.
RUN gradle clean build -x test

FROM bellsoft/liberica-openjre-alpine:17
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/*.jar"]