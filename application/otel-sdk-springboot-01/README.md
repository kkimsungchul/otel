# DB 접속
- url : http://localhost:8080/h2-console
- Driver Class : org.h2.Driver
- JDBC URL: jdbc:h2:~/otel
- User Name : sa
- Password : 

# 개발환경
- Gradle-8.8
- JAVA17
- H2 database

# 로그 조회
localhost:8080/log
localhost:8080/log/save
localhost:8080/board



# 작업 순서

# 라이브러리 추가
    implementation 'io.opentelemetry:opentelemetry-api'
    implementation 'io.opentelemetry:opentelemetry-sdk'
    implementation 'io.opentelemetry:opentelemetry-exporter-logging'
    implementation 'io.opentelemetry:opentelemetry-exporter-otlp'
    implementation 'io.opentelemetry.semconv:opentelemetry-semconv:1.25.0-alpha'
    implementation 'io.opentelemetry:opentelemetry-sdk-extension-autoconfigure'

# OpenTelemetryConfig 클래스 생성

# OpenTelemetryAspect 클래스 생성