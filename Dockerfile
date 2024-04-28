FROM maven:3.8.3 as compile
WORKDIR /creep-tube
COPY ./ /creep-tube
RUN mvn clean package
FROM openjdk:17
COPY --from=compile ./creep-tube/target/creep-tube.jar ./
ENTRYPOINT ["java","-jar", "creep-tube.jar"]