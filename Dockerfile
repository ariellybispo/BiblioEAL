# ============================================================
# Dockerfile — Personal Library (Spring Boot)
# ============================================================
# Build multi-estágio:
#   Estágio 1 (builder): compila e empacota o JAR com Maven
#   Estágio 2 (runtime): imagem mínima apenas para executar
#
# Build multi-estágio reduz a imagem final de ~600MB para ~180MB.
# ============================================================

# ─── Estágio 1: Build ───────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copia apenas o pom.xml primeiro — aproveita cache de layers do Docker
# Se o pom.xml não mudou, o Maven não re-baixa as dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B 2>/dev/null || true

# Copia e compila o código-fonte
COPY src ./src
COPY pom.xml .

# -DskipTests: pula testes no build da imagem
# Os testes rodam no CI (GitHub Actions), não no Dockerfile
RUN mvn clean package -DskipTests -B

# ─── Estágio 2: Runtime ─────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine AS runtime

# Usuário não-root por segurança (princípio do menor privilégio)
RUN addgroup -S library && adduser -S library -G library

WORKDIR /app

# Copia apenas o JAR gerado (sem código-fonte, sem Maven)
COPY --from=builder /app/target/*.jar app.jar

# Muda para usuário não-root
USER library

# Porta exposta pela aplicação
EXPOSE 8080

# Opções da JVM:
# -Xmx256m: limite de memória heap (adequado para container)
# -Djava.security.egd: melhora performance de geração de números aleatórios
ENTRYPOINT ["java", \
            "-Xmx256m", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-jar", "app.jar"]