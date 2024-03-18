# Dockerfile

# jdk17 Image Start
FROM openjdk:17

# 인자 설정 - JAR_File
ARG JAR_FILE=build/libs/*.jar

# jar 파일 복제
COPY ${JAR_FILE} app.jar

# 인자 설정 부분과 jar 파일 복제 부분 합쳐서 진행해도 무방
#COPY build/libs/*.jar app.jar

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]

#
## Dockerfile Build stage
## jdk17 Image Start
#FROM openjdk:17 AS build
#WORKDIR /yourLuck
## 필요한 파일만 명확하게 복사
#COPY gradlew .
#COPY gradle gradle
#COPY build.gradle .
#COPY settings.gradle .
#COPY src src
## 필요한 도구 설치
#RUN apt-get update && apt-get install -y findutils
#
## 프로젝트 빌드, 결과물은 build/libs/에 위치
#RUN ./gradlew clean build
#
## Run stage
#FROM openjdk:17
#WORKDIR /app
## 빌드 스테이지에서 생성된 JAR 파일을 현재 스테이지로 복사
#COPY --from=build /yourLuck/build/libs/*.jar app.jar
## 애플리케이션 실행
#ENTRYPOINT ["java", "-jar", "app.jar"]
#
## 빌드 스테이지에서 생성된 JAR 파일을 복사
#COPY --from=build /yourLuck/app/build/libs/*.jar app.jar
#
## jar 파일 복제
#COPY ${JAR_FILE} app.jar
#
## 인자 설정 부분과 jar 파일 복제 부분 합쳐서 진행해도 무방
##COPY build/libs/*.jar app.jar
#
## 실행 명령어
#ENTRYPOINT ["java", "-jar", "app.jar"]

