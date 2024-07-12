## 쿼리 실행 로그 가져오기

### github readme.md
- url :https://github.com/open-telemetry/opentelemetry-collector-contrib/tree/main/receiver/sqlqueryreceiver


## postgresql 설정 변경
- 경로 : C:\Program Files\PostgreSQL\16\data\postgresql.conf

```yaml
shared_preload_libraries = 'pg_stat_statements'
```

## postgresql 서비스 재시작
1. 윈도우 실행에서 services.msc 입력
2. 서비스 관리자에서 postgresql-x64-16 를 찾은 후
3. 중지 클릭 후 실행 클릭


## 확장프로그램 설치
```sql
CREATE EXTENSION pg_stat_statements;
```

## 테스트를 위한 데이터 생성
```sql
DO $$
BEGIN
    FOR i IN 1..10000000 LOOP
        INSERT INTO PUBLIC.user (password, name, address, create_date, modify_date)
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

## 테스트를 위한 데이터 조회
```
select * from public.user;
```

## 쿼리 실행 로그 확인
```sql
SELECT query, calls, total_exec_time, rows, mean_exec_time
FROM pg_stat_statements
ORDER BY total_exec_time DESC
```

## 콜렉터 config 설정 변경
```yaml
receivers:
  sqlquery:
    driver: postgres
    datasource: "host=localhost port=5432 user=postgres password=postgres sslmode=disable"
    queries:
      - sql: "SELECT query, calls, total_exec_time, rows, mean_exec_time FROM pg_stat_statements"
        #tracking_start_value: "10000"
        #tracking_column: log_id
        logs:
          - body_column: query
#      - sql: "select count(*) as count, genre from movie group by genre"
#        metrics:
#          - metric_name: movie.genres
#            value_column: "count"

service:
  pipelines:
    logs/dev:
      receivers: [otlp,windowseventlog,sqlquery]
```

## 콜렉터 실행
```shell
otelcol-contrib.exe --config customconfig.yaml
```


---
log_statement = 'all'                    # 모든 SQL 문을 로그에 기록
log_min_duration_statement = 0           # 실행 시간과 관계없이 모든 쿼리 기록 (0은 모든 쿼리를 의미)
logging_collector = on                   # 로그 수집기 활성화
log_destination = 'csvlog'               # CSV 형식으로 로그 저장 (분석하기 쉬움)
log_directory = 'log'                    # 로그 파일을 저장할 디렉토리
log_filename = 'postgresql-%Y-%m-%d_%H%M%S.log'  # 로그 파일 이름 형식
log_rotation_age = 1d                    # 1일마다 새 로그 파일 생성
log_rotation_size = 100MB                # 100MB마다 새 로그 파일 생성