

## 개발 환경
- Gradle-8.8
- JAVA17
- H2 database
---
## DB 접속
- url : http://localhost:6060/h2-console
- Driver Class : org.h2.Driver
- JDBC URL: jdbc:h2:~/otel-auto
- User Name : sa
- Password :
---
## API 설명
- 로그 조회 : localhost:6060/log
- 로그 저장 : localhost:6060/log/save
- 게시글 전체 조회 : localhost:6060/board
- 게시글 수량 지정 조회 : localhost:6060/board/{count}

---
## 라이브러리 추가
- opentelemetry 1.3.9 버전으로 통일
```groovy
dependencyManagement {
    imports {
        mavenBom("io.opentelemetry:opentelemetry-bom:1.39.0")
        mavenBom("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom-alpha:2.4.0-alpha")
    }
}
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    compileOnly 'org.projectlombok:lombok'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    runtimeOnly 'com.h2database:h2'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'


    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter'
    implementation 'io.opentelemetry.instrumentation:opentelemetry-log4j-appender-2.17'
}
```
---
## 설정 파일
- Auto 방식은 yml파일에 설정을 추가하여 사용
- application.yml
```yaml
spring:
  application:
    name: otel-auto-springboot-01
  datasource :
    url:  jdbc:h2:~/otel-auto
    username : sa
    password :
    driver-class-name : org.h2.Driver
  jpa :
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        show_sql: false
        format_sql: false
    defer-datasource-initialization: true
  sql:
    init:
      mode: always
  h2:
    console:
      enabled: true
      path: /h2-console
server:
  port: 6060

otel:
  sdk:
    metrics:
      period: 500
    traces:
      period: 500
  service:
    name: otel-auto-springboot-01-service
  resource:
    attributes:
      deployment:
        environment: dev
      service:
        name: otel-auto-springboot-01
      env: dev
  instrumentation:
    micrometer:
      enabled: true
    spring-web:
      enabled: true
    spring-webmvc:
      enabled: true
    spring-webflux:
      enabled: true
    kafka:
      enabled: true
  exporter:
    otlp:
      protocol : grpc
      endpoint: http://127.0.0.1:9999 # Opentelemetry Collector ????
  metrics:
    exporter: otlp, logging
  logs:
    exporter: otlp
  traces:
    exporter: otlp
  log:
    level: debug
```


## 프로세스 흐름
1. 사용자(Client)가 Request를 보냄
2. Spring의 AOP의 @Before에서  기본적인 metric , Span  데이터 생성
3. Controller ~ Service 로직에서는 AOP에서 생성한 Span을 가져와서 하위(Child) Span을 생성
4. 모든 로직이 끝난 후에 AOP의 @After에서 생성한 Span을 종료
5. 사용자에게 Response 전달
---
## Data end point
- Span(trace) Data
    - Application -> OpenTelemetry collector -> Jaeger

- Metric
    - Application -> OpenTelemetry collector ->  Prometheus

- log
    - Application -> OpenTelemetry collector -> file & loki

---
## FAQ
#### yml 파일 설정만으로도 사용해도 괜찮은가?
```text
기본적인 애플리케이션에 대한 계측은 제공해주지만, 설정에 대해서 상세히 알고 있어야 함
기본설정만을 사용할 경우 필요없는 데이터의 계측 또는 필요한 데이터의 계측이 원할하지 않을 수 있음
ex) 현재 해당 프로젝트에서는 SpringBoot의 Controller , Service에 대한계측이 제대로 이뤄지지 않고 있음
좀더 편리하게 사용하기 위해서는 오픈텔레메트리 라이브러리에서 제공해주는 @WithSpan 을 메서드 위에 붙여 사용하는 것이 좋음
```

#### configuration class 를 사용할 수 있는가?
```text
configuration 클래스를 사용하여서 오픈텔레메트리 설정을 할 수 있음
yml파일보다 더 상세하게 설정가능함
```


---
## 기타 참고 링크

### SpringBoot starter
- https://opentelemetry.io/docs/zero-code/java/spring-boot-starter/
- https://mvnrepository.com/artifact/io.opentelemetry.instrumentation/opentelemetry-spring-boot-starter/2.4.0-alpha
