FROM openjdk:17-jdk-slim

# Instalar gradle
RUN apt-get update && apt-get install -y wget unzip
RUN wget https://services.gradle.org/distributions/gradle-8.5-bin.zip
RUN unzip gradle-8.5-bin.zip
RUN mv gradle-8.5 /opt/gradle
ENV PATH="/opt/gradle/bin:${PATH}"

COPY . /app
WORKDIR /app

RUN gradle clean build -x test --no-daemon

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/build/libs/cuoco-0.0.1-SNAPSHOT.jar"]
