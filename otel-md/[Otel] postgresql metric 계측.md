## OpenTelemetry 로 데이터베이스 계측
- Metric만 가능 (trace, log 기능은 없음)
- 아직 실험적 기능임

### github readme.md
- https://github.com/open-telemetry/opentelemetry-collector-contrib/tree/main/receiver/postgresqlreceiver

### postgresql 설치
1. 다운로드 : https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
    ※ windows 16.3  버전으로 진행하였음
2. 설치
    관리자 계정(postgres)의 비밀번호 postgres로 설정
    port : 5432 (default)

### postgresql 접속
1. 윈도우에서 sql shell 실행 
2. 아래와 같이 나오는데 엔터를 계속누르고 마지막에 postgres 비밀번호만 입력
```text
    Server [localhost]:
    Database [postgres]:
    Port [5432]:
    Username [postgres]:
    postgres 사용자의 암호:
```

### 데이터베이스 생성 및 계정 생성
1. 데이터베이스 생성
```sql
CREATE DATABASE otel;
```


2. 계정 생성
```sql
CREATE USER "otel-user" WITH PASSWORD 'otel-user';
```
3. 권한 부여
```sql
GRANT ALL PRIVILEGES ON DATABASE otel TO "otel-user";
```

4. otel-user에 관리자 권한 부여
- 테스트를 위해서 관리자 권한을 부여한것이며, 실제 운영환경에서는 하지말것
```sql
ALTER USER "otel-user" WITH SUPERUSER;
```
5. 테이블 생성
```sql
CREATE TABLE "user" (
    id SERIAL PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modify_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```
6. 샘플 데이터 추가
```sql
DO $$
BEGIN
    FOR i IN 1..100 LOOP
        INSERT INTO "user" (password, name, address, create_date, modify_date)
        VALUES (
            'password' || i,
            'name' || i,
            'address' || i,
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP
        );
    END LOOP;
END $$;
```

### postgresql 확장프로그램 설치
```sql
CREATE EXTENSION pg_stat_statements;
```

## postgresql 설정 변경
- 경로 : C:\Program Files\PostgreSQL\16\data\postgresql.conf
- 파일 제일 하단에 아래와 같이 추가 후 저장
```shell
# otel
shared_preload_libraries = 'pg_stat_statements'
pg_stat_statements.track = all
pg_stat_statements.max = 10000
track_activity_query_size = 2048
```

## postgresql 서비스 재시작
1. 윈도우 실행에서 services.msc 입력
2. 서비스 관리자에서 postgresql-x64-16 를 찾은 후 
3. 중지 클릭 후 실행 클릭


---

### 콜렉터 컨트리뷰터 다운로드
### 설치파일
- url : https://github.com/open-telemetry/opentelemetry-collector-releases/releases/download/v0.104.0/otelcol-contrib_0.104.0_windows_x64.msi
### 실행파일
- url : https://github.com/open-telemetry/opentelemetry-collector-releases/releases/download/v0.104.0/otelcol-contrib_0.104.0_windows_amd64.tar.gz

### 설정파일 수정
postgresql 부분만 추가
```yaml
receivers:
  otlp:
    protocols:
      grpc:
        endpoint: 127.0.0.1:9999
      http:
        endpoint: 127.0.0.1:4318
  postgresql:
    endpoint: 127.0.0.1:5432
    transport: tcp
    username: otel-user
    password: otel-user
    databases:
      - otel
    tls:
      insecure: true
    collection_interval: 10s

service:
  pipelines:
    logs/dev:
      receivers: [otlp]
      exporters: [file]
      processors: [batch]
    traces:
      receivers: [otlp]
      exporters: [otlp/jaeger,file]
      processors: [batch]
    metrics:
      receivers: [postgresql,otlp]
      exporters: [prometheus]
      processors: [batch]
```

### 서비스로 실행 방법
```shell
otelcol-contrib.exe --config config.yaml --windows-service install
sc start otelcol-contrib
```

### 기존 콜렉터 실행 방법
```shell
otelcol-contrib.exe --config config.yaml
```

### Metric 확인
- url : http://localhost:9464/metrics
```text
# HELP postgresql_bgwriter_buffers_allocated_total Number of buffers allocated.
# TYPE postgresql_bgwriter_buffers_allocated_total counter
postgresql_bgwriter_buffers_allocated_total 3096
# HELP postgresql_bgwriter_buffers_writes_total Number of buffers written.
# TYPE postgresql_bgwriter_buffers_writes_total counter
postgresql_bgwriter_buffers_writes_total{source="backend"} 344
postgresql_bgwriter_buffers_writes_total{source="backend_fsync"} 0
postgresql_bgwriter_buffers_writes_total{source="bgwriter"} 0
postgresql_bgwriter_buffers_writes_total{source="checkpoints"} 1238
# HELP postgresql_bgwriter_checkpoint_count_total The number of checkpoints performed.
# TYPE postgresql_bgwriter_checkpoint_count_total counter
postgresql_bgwriter_checkpoint_count_total{type="requested"} 1
postgresql_bgwriter_checkpoint_count_total{type="scheduled"} 9
# HELP postgresql_bgwriter_duration_milliseconds_total Total time spent writing and syncing files to disk by checkpoints.
# TYPE postgresql_bgwriter_duration_milliseconds_total counter
postgresql_bgwriter_duration_milliseconds_total{type="sync"} 722
postgresql_bgwriter_duration_milliseconds_total{type="write"} 124674
# HELP postgresql_bgwriter_maxwritten_total Number of times the background writer stopped a cleaning scan because it had written too many buffers.
# TYPE postgresql_bgwriter_maxwritten_total counter
postgresql_bgwriter_maxwritten_total 0
# HELP postgresql_commits_total The number of commits.
# TYPE postgresql_commits_total counter
postgresql_commits_total 344
# HELP postgresql_connection_max Configured maximum number of client connections allowed
# TYPE postgresql_connection_max gauge
postgresql_connection_max 100
# HELP postgresql_database_count Number of user databases.
# TYPE postgresql_database_count gauge
postgresql_database_count 1
# HELP postgresql_db_size_bytes The database disk usage.
# TYPE postgresql_db_size_bytes gauge
postgresql_db_size_bytes 8.081891e+06
# HELP postgresql_rollbacks_total The number of rollbacks.
# TYPE postgresql_rollbacks_total counter
postgresql_rollbacks_total 0
# HELP postgresql_table_count Number of user tables in a database.
# TYPE postgresql_table_count gauge
postgresql_table_count 0
```

### 매트릭 설명
```
postgresql_bgwriter_buffers_allocated_total
설명: 할당된 버퍼의 총 수.
타입: Counter (계수기)
값: 3096

postgresql_bgwriter_buffers_writes_total
설명: 작성된 버퍼의 총 수.
타입: Counter (계수기)
세부값:
source="backend": 344
source="backend_fsync": 0
source="bgwriter": 0
source="checkpoints": 1238

ostgresql_bgwriter_checkpoint_count_total
설명: 수행된 체크포인트의 수.
타입: Counter (계수기)
세부값:
type="requested": 1
type="scheduled": 9

postgresql_bgwriter_duration_milliseconds_total
설명: 체크포인트에서 파일을 디스크에 기록하고 동기화하는 데 걸린 총 시간 (밀리초 단위).
타입: Counter (계수기)
세부값:
type="sync": 722
type="write": 124674

postgresql_bgwriter_maxwritten_total
설명: 백그라운드 작성자가 너무 많은 버퍼를 작성하여 클리닝 스캔을 중단한 횟수.
타입: Counter (계수기)
값: 0

postgresql_commits_total
설명: 커밋된 트랜잭션의 총 수.
타입: Counter (계수기)
값: 344

postgresql_connection_max
설명: 허용되는 최대 클라이언트 연결 수.
타입: Gauge (게이지)
값: 100

postgresql_database_count
설명: 사용자 데이터베이스의 수.
타입: Gauge (게이지)
값: 1

postgresql_db_size_bytes
설명: 데이터베이스 디스크 사용량 (바이트 단위).
타입: Gauge (게이지)
값: 8,081,891 (약 8.08 MB)

postgresql_rollbacks_total
설명: 롤백된 트랜잭션의 총 수.
타입: Counter (계수기)
값: 0

postgresql_table_count
설명: 데이터베이스 내 사용자 테이블의 수.
타입: Gauge (게이지)
값: 0
각 메트릭의 주요 용도
Buffer 관련 메트릭 (postgresql_bgwriter_buffers_allocated_total, postgresql_bgwriter_buffers_writes_total 등): 데이터베이스 시스템의 버퍼 할당 및 쓰기 성능을 모니터링하여, 백그라운드 쓰기 작업과 관련된 성능 문제를 파악할 수 있습니다.
Checkpoint 관련 메트릭 (postgresql_bgwriter_checkpoint_count_total, postgresql_bgwriter_duration_milliseconds_total 등): 체크포인트의 빈도와 성능을 모니터링하여, 데이터베이스의 안정성 및 데이터 무결성을 유지하는 데 도움이 됩니다.
트랜잭션 메트릭 (postgresql_commits_total, postgresql_rollbacks_total): 트랜잭션 커밋 및 롤백 횟수를 모니터링하여, 데이터베이스 트랜잭션의 성공 및 실패 비율을 파악할 수 있습니다.
시스템 용량 및 사용량 메트릭 (postgresql_connection_max, postgresql_database_count, postgresql_db_size_bytes, postgresql_table_count): 시스템의 용량 및 사용량을 모니터링하여, 리소스 관리 및 확장 계획에 도움이 됩니다.
```
