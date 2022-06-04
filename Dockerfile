FROM openjdk:11.0.12-slim-buster

COPY /target/createproducts*.jar createproducts.jar

SHELL ["/bin/sh", "-c"]

CMD java -jar createproducts.jar