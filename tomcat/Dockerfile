FROM tomcat:11.0

COPY ./conf/tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml
COPY ./conf/server.xml /usr/local/tomcat/conf/server.xml
COPY STUTCJ_Fingerprint_Server.war /usr/local/tomcat/webapps/

EXPOSE 8080

CMD ["catalina.sh", "run"]


