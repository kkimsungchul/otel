# DB 접속
- url : http://localhost:8080/h2-console
- Driver Class : org.h2.Driver
- JDBC URL: jdbc:h2:~/otel
- User Name : sa
- Password : 

# 개발 환경
- Gradle-8.8
- JAVA17
- H2 database

# API 설명
- 로그 조회 : localhost:8080/log
- 로그 저장 : localhost:8080/log/save
- 게시글 전체 조회 : localhost:8080/board
- 게시글 수량 지정 조회 : localhost:8080/board/{count}


# 작업 순서

# 라이브러리 추가
- opentelemetry 1.3.9 버전으로 통일
```groovy
dependencyManagement {
    imports {
    mavenBom("io.opentelemetry:opentelemetry-bom:1.39.0")
    mavenBom("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom-alpha:2.4.0-alpha")
    }
}
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    compileOnly 'org.projectlombok:lombok'
    runtimeOnly 'com.h2database:h2'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation "io.opentelemetry:opentelemetry-api"
    implementation "io.opentelemetry:opentelemetry-sdk"
    implementation "io.opentelemetry:opentelemetry-exporter-logging"
    implementation "io.opentelemetry:opentelemetry-exporter-otlp"
    implementation "io.opentelemetry.semconv:opentelemetry-semconv"
    implementation "io.opentelemetry:opentelemetry-sdk-extension-autoconfigure"
    //    implementation 'org.apache.logging.log4j:log4j-core:2.23.1'
    //    implementation 'org.apache.logging.log4j:log4j-api:2.23.1'
    //    implementation "io.opentelemetry.instrumentation:opentelemetry-log4j-appender-2.17:2.4.0-alpha"
}
```

# OpenTelemetryConfig 클래스 생성
- 경로 : com/kt/otelsdkspringboot01/config/OpenTelemetryConfig.java
  - opentelemetry 설정 클래스
  - Spring Bean에 등록하여 사용

# OpenTelemetryAspect 클래스 생성
- 경로 : com/kt/otelsdkspringboot01/config/OpenTelemetryAspect.java
  - Spring의 AOP를 사용하여 모든 로직에 대한 opentelemetry metric ,trace 를 생성해주는 클래스 
  - Spring Bean에 등록하여 사용

# SpanUtils
- 경로 : com/kt/otelsdkspringboot01/utils/SpanUtils.java
  - Child Span을 가져올때 사용하려고 만든 클래스
  - Spring Bean에 등록하여 사용

# 프로세스 흐름
1. 사용자(Client)가 Request를 보냄
2. Spring의 AOP의 @Before에서  기본적인 metric , Span  데이터 생성
3. Controller ~ Service 로직에서는 AOP에서 생성한 Span을 가져와서 하위(Child) Span을 생성
4. 모든 로직이 끝난 후에 AOP의 @After에서 생성한 Span을 종료
5. 사용자에게 Response 전달

## Data end point
- Span(trace) Data
  - Application -> Jaeger

- Metric
  - Application -> Opentelemetry ->  Prometheus


## FAQ

#### try (Scope scope = childSpan.makeCurrent()) 사용 이유
- 요약 
```text
try-with-resources 를 하지 않아도 정상적으로 작동하나, 아래의 항목들을 위해 사용함
아래와 같이 사용해도 오류는 없음, 다만 예외 발생할 경우 디버깅이 안됨

Span childSpan = spanUtils.getChildSpan("BoardController.all");
비즈니스로직
childSpan.end();
```

- 컨텍스트 전환:
```text
Scope scope = childSpan.makeCurrent()를 호출하면, childSpan이 현재 컨텍스트로 설정됩니다. 
이를 통해 코드 블록 내에서 실행되는 모든 작업이 childSpan과 연결된 상태로 실행됩니다.
이렇게 하지 않으면, childSpan이 현재 컨텍스트로 설정되지 않기 때문에, logService.save()나 boardRepository.findAll()과 같은 메서드 호출이 childSpan과 연결되지 않습니다. 
이는 분산 추적에서 각 작업이 어느 스팬과 연관되어 있는지를 명확히 하는 데 있어 중요합니다.
```

- 자동 복원:
```text
try-with-resources 구문을 사용하면 Scope가 자동으로 닫히면서 이전 컨텍스트가 복원됩니다. 
이는 오류 없이 이전 상태로 돌아가기 위해 매우 중요합니다.
이를 수동으로 관리하는 것은 실수할 가능성이 높고 코드가 복잡해질 수 있습니다.
```

- 예외 처리:
```text
try 블록 내에서 예외가 발생하더라도, childSpan은 예외를 기록하고 종료됩니다. 
try-with-resources 구문은 예외가 발생하더라도 Scope를 자동으로 닫아주는 이점을 제공합니다.
```

- 코드 가독성 및 유지보수:
```text
try-with-resources 구문을 사용하면 컨텍스트 전환과 복원을 명시적으로 관리하는 코드가 간결해지고, 유지보수가 용이해집니다.
다시 말해, try (Scope scope = childSpan.makeCurrent()) 구문은 현재 컨텍스트를 올바르게 설정하고 자동으로 복원하여 코드의 안전성과 명확성을 높이는 역할을 합니다. 
이러한 패턴은 분산 추적 시스템에서 올바른 상관 관계를 유지하고, 문제가 발생했을 때 정확한 원인을 추적하는 데 중요한 역할을 합니다.
```


# 기타 참고 링크
---

### SpringBoot starter
- https://opentelemetry.io/docs/zero-code/java/spring-boot-starter/
- https://mvnrepository.com/artifact/io.opentelemetry.instrumentation/opentelemetry-spring-boot-starter/2.4.0-alpha

### log4j2 사용
- 현재 적용이 안됨
- https://opentelemetry.io/docs/zero-code/java/spring-boot-starter/additional-instrumentations/
- https://github.com/open-telemetry/opentelemetry-java-instrumentation/blob/main/instrumentation/log4j/log4j-appender-2.17/library/README.md
- https://central.sonatype.com/artifact/io.opentelemetry.instrumentation/opentelemetry-log4j-appender-2.17
- https://docs.honeycomb.io/send-data/logs/opentelemetry/sdk/java/

https://github.com/open-telemetry/opentelemetry-java-examples/blob/main/log-appender/src/main/java/io/opentelemetry/example/logappender/Application.java

### CPU 측정
https://nangmandeveloper.tistory.com/12
https://velog.io/@limsubin/Spring-Boot%EC%97%90%EC%84%9C-metric%EC%9D%84-%EC%A0%81%EC%9A%A9%ED%95%98%EC%9E%90
https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-actuator/3.3.0


### Spring actuator 사용
https://javadoc.io/doc/org.springframework.boot/spring-boot-actuator/latest/index.html
https://incheol-jung.gitbook.io/docs/study/srping-in-action-5th/chap-16.


### autoconfigure
https://github.com/open-telemetry/opentelemetry-java/blob/main/sdk-extensions/autoconfigure/README.md