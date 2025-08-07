FROM maven:3.9.11-eclipse-temurin-21-alpine

COPY ./STUTCJ_Fingerprint/pom.xml /tmp/pom.xml
COPY ./conf/settings.xml /usr/share/maven/ref/settings.xml

RUN mvn -B -f /tmp/pom.xml -s /usr/share/maven/ref/settings.xml dependency:resolve

WORKDIR /tmp

COPY ./STUTCJ_Fingerprint .

# RUN tree

#RUN mvn install tomcat7:deploy -s /usr/share/maven/ref/settings.xml

#ENTRYPOINT ["/usr/local/bin/mvn-entrypoint.sh"]
ENTRYPOINT ["/bin/sh", "-c"]
# CMD ["mvn install tomcat7:deploy -f /app/pom.xml -e"]
CMD ["mvn install -f /tmp/pom.xml"]
