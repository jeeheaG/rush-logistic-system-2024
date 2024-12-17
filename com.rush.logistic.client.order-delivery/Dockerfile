# 베이스 이미지. alpine : 불필요한 라이브러리들을 제외한 경량화된 버전
FROM openjdk:17-alpine

#정보 - 선택으로 작성
#LABEL maintainer="jh"

#jar 파일 위치 지정
ARG JAR_FILE=build/*.jar

# 환경변수 설정
# ENV [환경변수명] [값]

#jar 파일을 컨테이너 내부로 복사해오고 새 이름(order-system.jar)을 지정(소문자만 가능)
COPY ${JAR_FILE} rush-system.jar

#외부 호스트 8080 포트로 노출
EXPOSE 8080

#컨테이너 실행 시 반드시 실행될 명령어 입력
ENTRYPOINT ["java", "-jar", "/rush-system.jar"]