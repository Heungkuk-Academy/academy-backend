# 기본 이미지는 버전을 고정한다 (21-jdk 같은 이동 태그는 빌드 시점에 따라 JDK 패치 버전이 바뀐다).
# 올릴 때는 jdk 와 jre 를 같은 버전으로 함께 바꾼다.

# 빌드 단계
FROM eclipse-temurin:21.0.13_9-jdk AS builder
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

COPY src src
RUN ./gradlew build -x test --no-daemon

# 실행 단계
FROM eclipse-temurin:21.0.13_9-jre
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8", "app.jar"]