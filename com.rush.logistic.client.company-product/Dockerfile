FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} company-product-app.jar

EXPOSE ${COMPANY_SERVER_PORT}

ENTRYPOINT ["java","-jar","/company-product-app.jar"]