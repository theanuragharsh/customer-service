FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=targer/*.jar
COPY${JAR_FILE} custmomer-service-v1.jar
EXPOSE 8080
CMD ["java", "-jar", "custmomer-service-v1.jar"]


# build and run docker image
#1. cd /mnt/d/Bank\ Service/customer-service
#2. docker build -t customer-service .
#3. docker run -p 8082:8082 customer-service