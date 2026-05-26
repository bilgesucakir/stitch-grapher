# Use official Java image
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy project
COPY . .

# Build
RUN ./mvnw clean package -DskipTests

# Run
CMD ["java", "-jar", "target/*.jar"]