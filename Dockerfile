# base 이미지를 저장한다.
FROM openjdk:17-sdk-slim

# WORKDIR
WORKDIR /app

# 이미지를 만들 때 실제 프로젝트 안의 jar 파일들을 이미지의 app.jar로 복사한다.
COPY build/libs/*.jar app.jar


# 이 도커이미지는 8081 번 포트를 외부에 공개할 예정이다.
EXPOSE 8081

# 이미지가 컨테이너로 실행될 때 수행할 java, -jar, app.jar 명령어를 설정한다.
ENTRYPOINT ["java", "-jar", "app.jar"]