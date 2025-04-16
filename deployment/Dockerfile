FROM eclipse-temurin:17-jdk
VOLUME /tmp

RUN apt-get update
RUN apt-get install -y curl

RUN addgroup --system appgroup
RUN adduser --system appuser
RUN usermod -a -G appgroup appuser
RUN chown -R appuser:appgroup /tmp

COPY /applications/app-service/build/libs/git push -u origin main.jar technical_test_ms.jar
RUN sh -c 'touch /technical_test_ms.jar'

ENV TZ=America/Bogota
ENV JAVA_OPTS=" -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -XX:TieredStopAtLevel=1 -XX:+UseG1GC -Djava.security.egd=file:/dev/./urandom"

USER appuser
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar technical_test_ms.jar" ]
