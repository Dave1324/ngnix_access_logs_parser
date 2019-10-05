FROM maven:3.6.2-jdk-8
COPY ./ ./
RUN mvn clean package