# Etapa 1: build da aplicação
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew && ./gradlew clean build -x test -x check

# Etapa 2: imagem final, mais leve, só com o JRE
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copia apenas o jar executável (ignora o -plain.jar)
COPY --from=build /app/build/libs/*.jar /tmp/libs/
RUN find /tmp/libs -name "*.jar" ! -name "*-plain.jar" -exec cp {} /app/app.jar \;

EXPOSE 8080
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar /app/app.jar"]
