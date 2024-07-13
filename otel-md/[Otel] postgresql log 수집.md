
## collector에서 log 파일 읽기
- github url : https://github.com/open-telemetry/opentelemetry-collector-contrib/tree/main/receiver/filelogreceiver


### postgresql config 수정
- 경로 : C:\Program Files\PostgreSQL\16\data\postgresql.conf
- 인코딩, 로깅 타입 수정
```shell
client_encoding = 'UTF8'

# These settings are initialized by initdb, but they can be changed.
lc_messages = 'ko_KR.UTF-8'	# locale for system error message
					# strings
lc_monetary = 'ko_KR.UTF-8'	# locale for monetary formatting
lc_numeric = 'ko_KR.UTF-8'		# locale for number formatting
lc_time = 'ko_KR.UTF-8'		# locale for time formatting


# logging
log_statement = 'all'                    # 모든 SQL 문을 로그에 기록
log_min_duration_statement = 0           # 실행 시간과 관계없이 모든 쿼리 기록 (0은 모든 쿼리를 의미)
logging_collector = on                   # 로그 수집기 활성화
#log_destination = 'csvlog'               # CSV 형식으로 로그 저장 (분석하기 쉬움)
log_destination = 'stderr'               # 'csvlog' 대신 'stderr' 사용
log_directory = 'log'                    # 로그 파일을 저장할 디렉토리
log_filename = 'postgresql-%Y-%m-%d_%H%M%S.log'  # 로그 파일 이름 형식
log_rotation_age = 1d                    # 1일마다 새 로그 파일 생성
log_rotation_size = 100MB                # 100MB마다 새 로그 파일 생성
log_min_duration_statement = 1000  # 1초 이상 걸린 쿼리만 로그에 기록 (밀리초 단위)
```

### collector 수정
```yaml
  filelog:
    include: [ 'C:\Program Files\PostgreSQL\16\data\log\*.log' ]
    operators:
      - type: regex_parser
        regex: '^(?P<timestamp>\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}\.\d{3}) (?P<timezone>\w+) \[(?P<pid>\d+)\] (?P<level>\w+):  (?P<message>.*)$'
        timestamp:
          parse_from: attributes.timestamp
          layout: '%Y-%m-%d %H:%M:%S.%f'
        severity:
          parse_from: attributes.level
          mapping:
            debug: DEBUG
            info: INFO
            notice: INFO
            warning: WARN
            error: ERROR
            log: INFO
            fatal: FATAL
            panic: FATAL
```
### 옵션 설명

- include : PostgreSQL 로그 파일의 경로를 지정, 와일드카드를 사용하여 모든 .log 파일을 포함
- regex : PostgreSQL 로그 형식에 맞는 정규표현식 사용 이는 타임스탬프, 시간대, PID, 로그 레벨, 메시지포함
- timestamp: 로그의 타임스탬프 파싱 방법을 지정
- severity: PostgreSQL의 로그 레벨을 OpenTelemetry의 로그 레벨에 매핑


### 실제 파싱된 로그
```json
{
  "resourceLogs":[
    {
      "resource":{
      },
      "scopeLogs":[
        {
          "scope":{
          },
          "logRecords":[
            {
              "observedTimeUnixNano":"1720874197630963200",
              "body":{
                "stringValue":"2024-07-13 21:36:37.521 KST [116576] 로그: 명령 구문:"
              },
              "attributes":[
                {
                  "key":"log.file.name",
                  "value":{
                    "stringValue":"postgresql-2024-07-13_213019.log"
                  }
                }
              ],
              "traceId":"",
              "spanId":""
            },
            {
              "observedTimeUnixNano":"1720874197633049100",
              "body":{
                "stringValue":"2024-07-13 21:36:37.522 KST [116576] 로그: 명령 구문:"
              },
              "attributes":[
                {
                  "key":"log.file.name",
                  "value":{
                    "stringValue":"postgresql-2024-07-13_213019.log"
                  }
                }
              ],
              "traceId":"",
              "spanId":""
            },
            {
              "observedTimeUnixNano":"1720874197637230900",
              "body":{
                "stringValue":"2024-07-13 21:36:37.522 KST [116576] 로그: 명령 구문: SELECT * FROM PUBLIC.user WHERE PUBLIC.user.NAME='sungchu11l' LIMIT 123451"
              },
              "attributes":[
                {
                  "key":"log.file.name",
                  "value":{
                    "stringValue":"postgresql-2024-07-13_213019.log"
                  }
                }
              ],
              "traceId":"",
              "spanId":""
            }
          ]
        }
      ]
    }
  ]
}
```

### 주의사항
- 로그가 한줄씩 처리가되면 정상적으로 수집이 되나, exception 또는 error같이 여러줄에 걸쳐 출력되는 로그일 경우 오류가 발생함
- 이럴경우 공식 문서에 있는 multiline-logs-parsing 부분을 참고하여 콜렉터 설정을 변경해야 함
- url : https://github.com/open-telemetry/opentelemetry-collector-contrib/tree/main/receiver/filelogreceiver#example---multiline-logs-parsing
- 설정 예시
```yaml
receivers:
  filelog:
    include:
      - /var/log/example/multiline.log
    multiline:
      line_start_pattern: ^Exception
```

### 수집이 안될 경우 확인
- 파일 경로가 정확한지 확인
- 백슬래시를 사용할 경우 이스케이프 처리가 필요할 수 있음
- PostgreSQL 로그 설정에 따라 로그 형식이 약간 다를 수 있음, 그에 맞게 정규식 수정이 필요함
- 로그 파일에 대한 읽기 권한이 OpenTelemetry 콜렉터 프로세스에 있는지 확인

