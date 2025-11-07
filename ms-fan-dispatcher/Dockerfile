# ---------- BUILD ----------
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copia lo mínimo para cachear dependencias
COPY gradlew settings.gradle build.gradle ./
# si usas archivos extra: COPY main.gradle ./
COPY gradle ./gradle
RUN chmod +x ./gradlew
# Calienta el wrapper (no falla si aún faltan fuentes)
RUN ./gradlew --no-daemon help || true

# Ahora el código
COPY . .

# Empaqueta (sin tests) — build genérico
RUN ./gradlew --no-daemon clean app-service:build -x test -x validateStructure --stacktrace --info

# ---------- RUNTIME ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# usuario no-root
RUN addgroup -S app -g 10001 && adduser -S app -G app -u 10001

# copia el jar construido (ajusta el patrón si tu jar cambia de nombre)
COPY --from=builder /app/applications/app-service/build/libs/*.jar app.jar

ENV JAVA_OPTS="-XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
USER app
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
