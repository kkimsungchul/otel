# Tomcat에 opentelemetry Agent 적용

## 적용 방법
- 이미 운영중인 어플리케이션의 소스코드를 수정이 어려울 경우에 Agent를 톰캣에 추가하여 바로 사용
- Agent만 설정 해주고 톰캣만 재실행 하면 바로 적용이 됨

### opentelemetry-javaagent.jar 파일 추가
아래의 경로에 파일 추가
경로 : apache-tomcat-9.0.89\bin



## Windows 환경
### catalina.bat 파일 수정
- setlocal부분 바로 밑에 옵션 추가

```shell
setlocal
set CATALINA_OPTS=%CATALINA_OPTS% -javaagent:"opentelemetry-javaagent.jar"
set CATALINA_OPTS=%CATALINA_OPTS% -Dotel.resource.attributes=service.name=tomcat_service,service.namespace=TOMCAT
set JAVA_OPTS=%JAVA_OPTS% -Duser.language=en
```

- set CATALINA_OPTS=%CATALINA_OPTS% -javaagent:"opentelemetry-javaagent.jar" <br>
※ tomcat에 opentelemetry agent 추가

- set CATALINA_OPTS=%CATALINA_OPTS% -Dotel.resource.attributes=service.name=tomcat_service,service.namespace=TOMCAT <br>
※ 아래와 같이 job name 에 내용 추가, 추가하지 않을 경우 job 정보는 job="unknown_service:java" 로 나옴
```shell
http_server_request_duration_seconds_bucket{http_request_method="GET",http_response_status_code="200",http_route="/index.jsp",instance="32a78fa6-3d8b-47f4-9d1c-826476aa5d4d",job="TOMCAT/tomcat_service",network_protocol_version="1.1",url_scheme="http",le="0.005"} 1
```

### Tomcat 실행
```shell
startup.bat
```

## Mac 환경
###catalina.sh 파일 수정
- OS 로 시작하는 부분에 CATALINA_OPTS="$CATALINA_OPTS -javaagent:opentelemetry-javaagent.jar" 내용 추가
```shell
# OS specific support.  $var _must_ be set to either true or false.
CATALINA_OPTS="$CATALINA_OPTS -javaagent:opentelemetry-javaagent.jar"
```

### Tomcat 실행
```shell
startup.sh
```


## Tomcat 접속 URL
- http://localhost:9080/

## Tomcat 서버 접속 포트 변경
- 경로 : apache-tomcat-9.0.89\conf
- 파일명 : server.xml
 
  아래의 부분을 찾아서 port 변경
```xml
    <Connector port="9080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="9883"
               maxParameterCount="1000"
			   URIEncoding="UTF-8"
               />
```