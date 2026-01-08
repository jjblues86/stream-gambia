# 1. Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk

# Install FFmpeg
RUN apt-get update && apt-get install -y ffmpeg

# 2. Set the working directory inside the container
WORKDIR /app

# 3. Copy the compiled jar file into the container
# (Note: We will rename the jar to app.jar for simplicity)
COPY target/*.jar app.jar

# 4. Expose the port this service runs on
EXPOSE 8082

# 5. Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]