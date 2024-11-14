# trace

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

scopeSpans.scope: 트레이스를 생성하는 데 사용되는 스코프의 이름을 포함하며, 트레이스가 어떤 작업, 서비스, 컴포넌트, 기능과 관련된 것인지에 대한 정보를 제공합니다.

scope.name: 트레이스 또는 스팬이 속한 스코프의 이름입니다.

예시) io.opentelemetry.tomcat-10.0

scope.version: 해당 스코프의 버전 정보입니다.

예시) 2.3.0-alpha

traceId: 스팬이 속한 트레이스를 식별하는 고유한 ID로 
동일한 `traceId`를 가진 여러 스팬이 하나의 트레이스에 속합니다.

예시) af8abbb23ff2970f42a75a229541bc76

 spanID: 스팬의 고유한 ID로, 트레이스 내에서 각 스팬을 식별하는 데 사용됩니다.

예시) 6350fb72f9fb1366

ParentSpanID: 현재 스팬의 부모 스팬 ID로, 요청의 흐름 추적 시 사용합니다.

예시) b440dbf1c2b76f90

flags: 트레이스와 관련된 스팬의 상태를 나타내는 데 사용되며, 비트 마스크 형태로 저장됩니다.

예시)

- **`0`**: **샘플링되지 않은 트레이스** (디폴트)
- **`1`**: **샘플링된 트레이스**
- **`2`**: 트레이스 플래그의 다른 속성을 나타낼 수 있음 (일부 구현에서 다르게 사용할 수 있음)

name: 해당 스팬의 이름으로, 보통 HTTP 요청을 나타낸다.

예시)

- **HTTP 요청에 대한 스팬 이름**: `"GET /api/v1/resource"`
    - 여기서는 HTTP 요청을 나타내며, `GET` 메서드로 `/api/v1/resource`라는 경로에 대한 요청이 수행된 것을 추적하는 스팬 이름입니다.
- **데이터베이스 쿼리에 대한 스팬 이름**: `"SELECT * FROM users"`
    - 데이터베이스에서 실행된 SQL 쿼리를 추적하는 스팬 이름입니다.
- **파일 읽기 작업에 대한 스팬 이름**: `"Read File /path/to/file"`
    - 파일 시스템에서 파일을 읽는 작업에 대한 스팬 이름입니다.
- **인증 요청에 대한 스팬 이름**: `"POST /login"`
    - 사용자 인증을 위한 HTTP `POST` 요청을 추적하는 스팬 이름입니

kind: 스팬의 종류를 나타내며, 트레이스 내에서 해당 스팬이 수행하는 작업의 성격을 정의합니다. 주로 `CLIENT`, `SERVER`, `CONSUMER`, `PRODUCER`와 같은 값으로 구분됩니다.

예시)

### `kind`의 값

| 값 | 설명 | 예시 |
| --- | --- | --- |
| `1` | **CLIENT**: 클라이언트의 요청을 나타냄 | 클라이언트가 HTTP 요청을 보내는 경우 |
| `2` | **SERVER**: 서버의 응답을 나타냄 | 서버가 HTTP 요청을 처리하는 경우 |
| `3` | **PRODUCER**: 메시지 큐에 메시지를 보내는 작업 | 메시지를 큐로 보내는 생산자 작업 |
| `4` | **CONSUMER**: 메시지 큐에서 메시지를 처리하는 작업 | 큐에서 메시지를 받아 처리하는 소비자 작업 |

startTimeUnixNano: 스팬이 시작된 시간을 나타냅니다. (나노초 단위)
예시) 1731326300253265711

endTimeUnixNano: 스팬이 종료된 시간을 나타냅니다. (나노초 단위)

예시) 1731326300260768476

attributes: 트레이스 항목에 대한 추가적인 정보를 제공하는 키-값 쌍

user_agent.original: 클라이언트가 웹 서버에 요청을 보낼 때 자신을 식별하는 정보를 담고 있는 문자열로, 웹 브라우저, 운영 체제, 장치 정보 등을 포함합니다.

예시) Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36

network.protocol.version: 네트워크 요청이 사용하고 있는 프로토콜의 버전을 기록하며, HTTP(S), TCP, gRPC 등의 프로토콜의 버전을 기록합니다.

예시) 1.1

network.peer.port: 네트워크 요청의 목표 포트로, 네트워크 통신에서 상대방(서버)의 포트 번호를 기록합니다.

예시) 62531

url.scheme: 네트워크 요청에 사용된 URL의 스키마를 나타내는 속성입니다. 클라이언트가 서버에 요청을 보낼 때 사용된 프로토콜을 정의합니다.

예시) http, https, ftp, grpc ,..

thread.name: 네트워크 요청이나 시스템 활동 중 사용된 스레드의 이름을 나타냅니다. 애플리케이션의 멀티스레드 환경에서 각 스레드가 수행하는 작업을 추적하고 분석하는 데 사용됩니다.

예시)

- JAVA 기반 애플리케이션(예: Tomcat)
    - http-nio-8080-exec-1

network.peer.address: 네트워크 요청의 대상 서버 주소를 나타내는 속성입니다. 클라이언트가 연결하려고 하는 서버의 IP주소 또는 호스트명을 기록하는 데 사용됩니다.

예시) 100.114.155.127

server.address: 네트워크 요청을 처리하는 서버의 IP 주소를 나타내는 속성입니다.

예시) 100.83.227.59

client.address: 네트워크 요청을 보낸 클라이언트의 IP 주소를 나타내는 속성입니다.

예시) 100.114.155.127

url.path: HTTP 요청에서 요청된 리소스의 경로를 나타내는 속성입니다. 도메인 이름이나 프로토콜을 제외한 부분으로, 서버가 요청을 처리하기 위한 특정 리소스나 경로를 지정합니다.

예시) /error/test05

error.type: 요청 처리 중 발생한 에러의 유형을 나타내는 속성입니다. HTTP 응답 코드 또는 애플리케이션 내에서 정의된 에러 코드를 사용할 수 있습니다.

예시) 

- 서버 오류
    - 500 Internal Server Error
    - 404 Not Found
    - 403 Forbidden
    - 400 Bad Request
    - 408 Request Timeout
- 애플리케이션 오류
    - USER_NOT_FOUND: 사용자가 존재하지 않음
    - INVALID_INPUT: 잘못된 입력
    - DATABASE_CONNECTION_FAILED: 데이터베이스 연결 실패

http.request.method: 클라이언트가 서버에 요청을 보낼 때 사용하는 HTTP 메서드를 나타냅니다.

예시) GET, POST, PUT, DELETE

http.route: HTTP 요청에 대한 경로를 나타내며, 클라이언트가 요청한 리소스의 구체적인 URL 경로를 추적하는 데 사용됩니다.

예시) /error/test05

server.port: 서버에서 리스닝하고 있는 포트 번호입니다. 서버가 클라이언트의 요청을 처리할 때, 클라이언트는 특정 포트 번호를 통해 서버에 연결합니다.

예시) 14040

thread.id: 서버나 애플리케이션 내에서 특정 작업을 수행하는 스레드의 ID를 나타냅니다. 트레이스에서 해당 작업을 처리한 스레드를 식별할 수 있습니다.

예시) 51

http.response.status_code: 서버가 클라이언트에게 응답을 보낼 때 사용하는 HTTP 응답 상태 코드입니다.

예시) 200, 500

exception.message: 예외가 발생한 경우 해당 예외에 대한 메시지입니다.

예시) JDBC exception executing SQL [select etd1_0.error_domain,etd1_0.error_test from error_test_domain etd1_0] [ERROR: relation \"error_test_domain\" does not exist\n Position: 51] [n/a]

exception.stacktrace: 오류가 발생한 위치와 호출 스택의 상세 내역입니다.

예시)

```bash
org.hibernate.exception.SQLGrammarException: JDBC exception executing SQL [select etd1_0.error_domain,etd1_0.error_test from error_test_domain etd1_0] [ERROR: relation \"error_test_domain\" does not exist\n Position: 51] [n/a]\r\n\tat org.hibernate.exception.internal.SQLStateConversionDelegate.convert(SQLStateConversionDelegate.java:91)\r\n\tat org.hibernate.exception.internal.StandardSQLExceptionConverter.convert(StandardSQLExceptionConverter.java:58)\r\n\tat org.hibernate.engine.jdbc.spi.SqlExceptionHelper.convert(SqlExceptionHelper.java:108)\r\n\tat org.hibernate.engine.jdbc.spi.SqlExceptionHelper.convert(SqlExceptionHelper.java:94)\r\n\... 109 more\r\n"
```

exception.type: 발생한 예외의 타입입니다.

예시) org.hibernate.exception.SQLGrammarExceptio

status: Span의 상태 코드를 나타내는 필드입니다. 스팬이 성공적으로 완료되었는지, 오류가 발생했는지 등을 알려줍니다.

예시)

- **`0: OK`**
    - 스팬이 성공적으로 완료된 경우
- **`1: UNSET`**
    - 상태 코드가 설정되지 않은 기본 상태. 상태 코드가 명시되지 않으면 `UNSET`으로 간주됩니다.
- **`2: ERROR`**
    - 스팬의 처리가 실패한 경우. 예를 들어, 예외나 오류가 발생한 경우에 사용됩니다.