# 1. Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# 2. Install required packages and Google Chrome
RUN apt-get update && apt-get install -y wget gnupg2 ca-certificates \
    && wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

# 3. Set the working directory inside the container
WORKDIR /app

# 4. Copy the application JAR file from the host into the container
COPY build/libs/*.jar /app/app.jar

# 5. Expose the port the application will run on
EXPOSE 8080

# 6. Define the command to run the application
ENTRYPOINT ["nohup", "java", "-jar", "/app/app.jar", "> /app/nohup.out", "2>&1"]
