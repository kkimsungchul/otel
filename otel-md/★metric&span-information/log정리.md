# log

host.arch: amd64는 시스템의 CPU 아키텍처 유형을 나타냅니다. 특정 호스트에서 실행 중인 에이전트나 애플리케이션의 하드웨어 플랫폼을 설명하는 데 유용합니다.
예시)

- **x86_64 (또는 amd64)**
    - 64비트 x86 아키텍처. 대부분의 서버 및 데스크톱 시스템에서 사용됩니다.
- **arm64 (또는 aarch64)**
    - 64비트 ARM 아키텍처. 주로 모바일 기기와 저전력 서버에 사용되며, 최근에는 AWS Graviton 프로세서와 같은 고성능 서버에서도 많이 사용됩니다.

host.name: 애플리케이션이 실행되는 호스트의 이름을 나타내며, 호스트 시스템에서 제공하는 네트워크 호스트 이름을 반영합니다.

예시)

- **localhost.localdomain**
    - 로컬 개발 환경이나 기본 네트워크 설정에서 사용되는 일반적인 기본값입니다. 로컬 컴퓨터나 가상머신(VM)에서 기본값으로 나타날 수 있습니다.
- **host.name**
    - 실제 호스트 이름 또는 네트워크에서 인식되는 호스트의 FQDN(정규화된 도메인 이름)입니다.
    - 예시로, 서버에서 설정된 경우 `webserver01.company.com`과 같이 나타날 수 있으며, 이 값은 모니터링 환경에서 각 호스트를 식별하는 데 매우 유용합니다.

os.description: 애플리케이션이 실행되는 운영 체제입니다.

예시) Linux 4.18.0-553.16.1.el8_10.x86_64

- 여기서 `Linux`는 운영 체제를, `4.18.0-553.16.1.el8_10`는 커널 버전과 패키지 버전을, `x86_64`는 CPU 아키텍처를 나타냅니다.
- 이 예시는 Red Hat Enterprise Linux(RHEL) 또는 CentOS 8과 같은 배포판에서 흔히 볼 수 있는 형식입니

구성요소)

- **운영 체제 이름**: 시스템이 사용하는 OS 이름(`Linux`, `Windows`, `Darwin` 등).
- **커널 버전**: 운영 체제의 커널 버전으로, 보안 패치와 시스템 안정성에 중요한 정보입니다.
- **패키지 버전/배포판 정보**: 일부 리눅스 배포판에서는 커널 버전에 추가적인 패키지 정보나 배포판 버전이 포함되며, 운영 체제 업데이트와 호환성에 관한 정보를 제공합니다.
- **아키텍처 정보**: `x86_64`, `arm64`와 같은 아키텍처 유형이 포함되어 있습니다.

os.type: 애플리케이션이 실행 중인 운영 체제의 유형입니다.

예시)

- **linux**:
    - 모든 리눅스 배포판에 공통으로 사용되는 값입니다. Ubuntu, Red Hat, CentOS, Debian 등 다양한 리눅스 배포판에 해당합니다.
- **windows**:
    - Windows 운영 체제를 나타냅니다. 모든 버전의 Windows (예: Windows 10, Windows Server 2019)에서 이 값을 가집니다.
- **darwin**:
    - macOS 및 iOS를 포함하는 Apple의 운영 체제를 나타냅니다. macOS Mojave, Catalina, Big Sur, Monterey 등에서 `darwin`으로 식별됩니다.

process.command_args: 애플리케이션 또는 프로세스가 시작될 때 사용된 명령줄 인수의 목록을 제공합니다.

예시)

- **Node.js 서버 실행**: `node server.js --port=8080`
    - `process.command_args`: `["node", "server.js", "--port=8080"]`
- **Python 애플리케이션 실행**: `python3 app.py --env=production`
    - `process.command_args`: `["python3", "app.py", "--env=production"]`
- **Java 애플리케이션 실행**: `java -jar myapp.jar -Dconfig.path=/etc/config`
    - `process.command_args`: `["java", "-jar", "myapp.jar", "-Dconfig.path=/etc/config"]`

process.executable.path: 실행 중인 애플리케이션의 실제 실행 파일의 전체 경로입니다.

예시)

- **Node.js 애플리케이션**: `/usr/local/bin/node`
    - Node.js가 `/usr/local/bin` 디렉토리에서 실행 중인 경우, `process.executable.path`는 이 경로를 포함합니다.
- **Python 스크립트**: `/usr/bin/python3`
    - Python이 `/usr/bin`에서 실행 중인 경우, `process.executable.path`는 `/usr/bin/python3`을 표시합니다.
- **Java 애플리케이션**: `/usr/lib/jvm/java-11-openjdk/bin/java`
    - Java가 해당 경로에서 실행될 때 `process.executable.path`는 Java 실행 파일의 위치를 포함하게 됩니다.

process.pid: 현재 프로세스의 프로세스ID를 나타냅니다. PID는 운영 체제에서 각 프로세스를 고유하게 식별하는 번호입니다.

예시) 2012197

process.runtime.description: 애플리케이션이 실행 중인 런타임 환경의 상세 정보입니다. 주로 런타임의 버전 및 빌드 정보가 포함됩니다.

예시)

- **Node.js 런타임**:
    - 예: `Node.js v16.13.0`
    - Node.js의 버전과 빌드에 대한 정보를 제공합니다.
- **Python 런타임**:
    - 예: `CPython 3.9.7`
    - Python 버전과 빌드 정보를 표시합니다. 여기서 `CPython`은 Python의 기본 구현을 의미합니다.
- **Java 런타임**:
    - 예: `OpenJDK Runtime Environment 11.0.11+9`
    - Java OpenJDK의 버전과 빌드 번호 정보를 포함합니다.

service.name: 애플리케이션 또는 서비스의 이름입니다. 여러 서비스가 연동되어 있는 분산 시스템 환경에서 각 서비스의 구분에 핵심적인 역할을 합니다.

예시)

- **웹 애플리케이션**: `service.name: "web-frontend"`
- **백엔드 API**: `service.name: "backend-api"`
- **데이터베이스 서비스**: `service.name: "user-database"`
- **마이크로서비스**: `service.name: "order-processing-service"`

service.namespace: 서비스가 속한 논리적 그룹 또는 네임스페이스입니다.

예시) tomcat

scope.name: 모듈, 라이브러리, 또는 코드 영역의 이름을 정의하며 수집된 트레이스, 메트릭, 로그 데이터가 어떤 코드 범위에서 발생했는지 식별하는 데 사용 가능합니다.

예시)

- **클래스 이름**: `scope.name: "org.springframework.boot.web.servlet.support.ErrorPageFilter"`
    - SpringBoot 애플리케이션에서 오류 처리와 관련된 기능을 수행하는 클래스인 ErrorPageFilter을 나타냄
- **특정 기능 영역**: `scope.name: "authentication-service"`
    - 인증과 관련된 코드를 나타냄.
- **제3자 라이브러리**: `scope.name: "http-client"`
    - 외부 HTTP 요청을 담당하는 라이브러리 또는 모듈

logRecords: 로그 항목을 나타내며, 이벤트의 세부 사항, 심각도, 타임스탬프 및 관련된 컨텍스트 정보를 포함합니다.

timeUnixNano: 로그 이벤트가 발생한 정확한 시점을 나타내는 타임스탬프입니다. (나노초 단위)

observedTimeUnixNano:로그 이벤트가 OpenTelemetry 시스템에 의해 수집된 시점의 타임스탬프를 나타냅니다. (나노초 단위)

severityNumber: 로그 메시지의 심각도를 숫자로 나타냅니다.

예시)1~4, 5~8, 9~12, 14~16, 17~20, 21~24

severityText: 로그 메시지의 심각도를 문자로 나타냅니다.

예시) TRACE, DEBUG, INFO, WARN, ERROR, FATAL

body: 로그 메시지 본문으로, 중요한 이벤트 정보를 담고 있습니다.

예시)class java.lang.String cannot be cast to class java.lang.Integer

attributes: 로그 항목에 대한 추가적인 정보를 제공하는 키-값 쌍
exception.message: 예외가 발생한 경우 해당 예외에 대한 메시지

예시) exception.message: "Timeout occurred while connecting to database"

exception.stacktrace: 오류가 발생한 위치와 호출 스택의 상세 내역입니다.
예시)

```jsx
 exception.stacktrace: |
      at com.example.payment.PaymentService.processPayment(PaymentService.java:42)
      at com.example.payment.PaymentController.handleRequest(PaymentController.java:101)
      at org.springframework.web.method.support.InvocableHandlerMethod.invoke(InvocableHandlerMethod.java:221)
      at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:137)
      at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:110)
      at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895)
      at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808)
      at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)
      at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1060)
      at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:963)
      at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1013)
      at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:917)
      at javax.servlet.http.HttpServlet.service(HttpServlet.java:652)
      at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:891)
      at javax.servlet.http.HttpServlet.service(HttpServlet.java:733)
      at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)
      at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)
```

exception.type: 발생한 예외의 타입입니다.

traceId 및 spanId: OpenTelemetry의 분산 추적을 위해 사용되는 traceId와 spanId입니다. traceId는 전체 요청 흐름을 식별하고, spanId는 특정 작업(스팬)을 식별합니다.

예시)

- **Java(예외 클래스 이름):** `NullPointerException`, `FileNotFoundException`과 같은 예외 클래스가 이에 해당합니다.
- **Python(예외 클래스 이름):** `KeyError`, `ValueError` 등이 있습니다.
- **JavaScript(객체 이름):** `TypeError`, `ReferenceError` 등이 있습니다.