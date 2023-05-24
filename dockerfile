FROM ibm-semeru-runtimes:open-17-jre-focal
ADD target/barshelf-user.jar app.jar
ENV PORT 8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
