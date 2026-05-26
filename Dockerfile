# Use official Java image
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy project
COPY . .

RUN chmod +x mvnw

# Build
RUN ./mvnw clean package -DskipTests

# Run
CMD ["java", "-jar", "target/*.jar"]