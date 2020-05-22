FROM 127.0.0.1:5000/jdk:v1.8
MAINTAINER zhangl@51xyb.cn
ENV LANG zh_CN.UTF-8
ENV LC_ALL zh_CN.UTF-8
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN mkdir -p /data/opt/activiti/tmp
VOLUME /tmp
ADD target/activiti-service.jar app.jar
ADD src/key  /key
RUN /bin/chmod -R 777 ./app.jar
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/test/./urandom -jar app.jar --server.port=8080