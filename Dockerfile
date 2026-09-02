FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /workspace

COPY pom.xml .
RUN mvn --batch-mode --no-transfer-progress dependency:go-offline

COPY src ./src
RUN mvn --batch-mode --no-transfer-progress package -DskipTests

FROM eclipse-temurin:17-jre-jammy AS runtime

RUN groupadd --system crm \
    && useradd --system --gid crm --home-dir /app --shell /usr/sbin/nologin crm

WORKDIR /app

COPY --from=build --chown=crm:crm /workspace/target/b2b-crm-*.jar ./app.jar

USER crm

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
