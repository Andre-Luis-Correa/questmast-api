# Imagem base com Maven e JDK
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copia os arquivos necessários e faz o build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Segunda etapa: imagem menor para rodar a aplicação
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Copia o JAR gerado
COPY --from=build /app/target/*.jar app.jar

# Exponha a porta usada pela aplicação
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
