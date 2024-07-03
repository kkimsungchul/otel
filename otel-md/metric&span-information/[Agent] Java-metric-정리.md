## Java - metric 정리(agent)

#### db_client_connections_create_time_milliseconds
- 설명: 새로운 데이터베이스 클라이언트 연결을 생성하는 데 소요된 시간을 측정하는 히스토그램 메트릭입니다.
- 유형: 히스토그램
- 값: 버킷 범위와 생성 시간에 대한 데이터 포인트들

#### db_client_connections_idle_min
- 설명: 데이터베이스 클라이언트 풀에서 허용된 최소 유휴 연결 수를 나타내는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 허용된 최소 유휴 연결 수

#### db_client_connections_max
- 설명: 데이터베이스 클라이언트 풀에서 허용된 최대 연결 수를 나타내는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 허용된 최대 연결 수

#### db_client_connections_pending_requests
- 설명: 데이터베이스 클라이언트 풀에서 대기 중인 연결 요청의 누적 수를 나타내는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 대기 중인 연결 요청 수

#### db_client_connections_usage
- 설명: 데이터베이스 클라이언트 풀에서 현재 사용 중인 연결 수를 나타내는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 현재 사용 중인 연결 수, idle 상태와 used 상태로 구분될 수 있습니다.

#### db_client_connections_use_time_milliseconds
- 설명: 데이터베이스 클라이언트 연결을 사용한 후 반환까지 걸린 시간을 측정하는 히스토그램 메트릭입니다.
- 유형: 히스토그램
- 값: 버킷 범위와 사용 시간에 대한 데이터 포인트들

#### db_client_connections_wait_time_milliseconds
- 설명: 데이터베이스 클라이언트 풀에서 연결을 얻기까지 대기한 시간을 측정하는 히스토그램 메트릭입니다.
- 유형: 히스토그램
- 값: 버킷 범위와 대기 시간에 대한 데이터 포인트들

#### http_server_request_duration_seconds
- 설명: HTTP 서버 요청의 처리 시간을 측정하는 히스토그램 메트릭입니다.
- 유형: 히스토그램
- 값: 버킷 범위와 요청 처리 시간에 대한 데이터 포인트들, 다양한 레이블 포함

#### jvm_class_count
- 설명: 현재 로드된 클래스의 수를 측정합니다.
- 유형: gauge (순간적인 값)
- 값: 19,788 (현재 로드된 클래스 수)

#### jvm_class_loaded_total
- 설명: JVM 시작 이후 로드된 총 클래스의 수를 측정합니다.
- 유형: counter (증가하는 값)
- 값: 19,789 (JVM 시작 이후 총 클래스 로드 수)

#### jvm_class_unloaded_total
- 설명: JVM 시작 이후 언로드된 총 클래스의 수를 측정합니다.
- 유형: counter (증가하는 값)
- 값: 1 (JVM 시작 이후 총 클래스 언로드 수)

#### jvm_cpu_count
- 설명: JVM이 사용할 수 있는 프로세서(코어)의 수를 측정합니다.
- 유형: gauge (순간적인 값)
- 값: 8 (사용 가능한 프로세서 수)

#### jvm_cpu_recent_utilization_ratio
- 설명: JVM에서 보고된 최근 CPU 사용률을 측정합니다.
- 유형: gauge (순간적인 값)
- 값: 0.003912467693658763 (최근 CPU 사용률 비율)

#### jvm_cpu_time_seconds_total
- 설명: JVM 프로세스가 사용한 CPU 시간을 측정합니다.
- 유형: counter (증가하는 값)
- 값: 83.203125 (초 단위의 CPU 사용 시간)

#### jvm_gc_duration_seconds
- 설명: JVM 가비지 컬렉션 작업의 지속 시간을 히스토그램으로 측정합니다.
- 유형: histogram (범위별 버킷)
- 값: 다양한 버킷과 총 합, 카운트 정보가 포함됩니다.

#### jvm_memory_committed_bytes
- 설명: 각 메모리 풀(pool)에 대해 할당된 메모리의 양을 측정합니다.
- 유형: gauge (순간적인 값)
- 값: 각 메모리 풀(pool)별 할당된 바이트 수

#### jvm_memory_limit_bytes
- 설명: 각 메모리 풀(pool)에 대한 최대 메모리 제한을 측정합니다.
- 유형: gauge (순간적인 값)
- 값: 각 메모리 풀(pool)별 최대 제한 바이트 수

#### jvm_memory_used_after_last_gc_bytes
- 설명: 가장 최근의 가비지 컬렉션 이후 각 메모리 풀(pool)의 사용된 메모리 양을 측정합니다.
- 유형: gauge (순간적인 값)
- 값: 각 메모리 풀(pool)별 사용된 바이트 수

#### jvm_memory_used_bytes
- 설명: 각 메모리 풀(pool)의 현재 사용 중인 메모리 양을 측정합니다.
- 유형: gauge (순간적인 값)
- 값: 각 메모리 풀(pool)별 사용 중인 바이트 수

#### jvm_thread_count
- 설명: JVM에서 실행 중인 스레드의 수를 측정합니다.
- 유형: gauge (순간적인 값)
- 값: 각 상태(데몬 여부와 상태)에 따른 스레드 수

#### otlp_exporter_exported_total
- 설명: OpenTelemetry에서 내보낸 로그 및 스팬의 총 수를 측정합니다.
- 유형: counter (증가하는 값)
- 값: 성공 여부와 유형(type)에 따른 내보낸 로그 및 스팬 수

#### otlp_exporter_seen_total
- 설명: OpenTelemetry에서 보유한 로그 및 스팬의 총 수를 측정합니다.
- 유형: counter (증가하는 값)
- 값: 유형(type)에 따른 보유한 로그 및 스팬 수

#### processedLogs_total
- 설명: BatchLogRecordProcessor에서 처리된 로그의 총 수를 측정합니다. 높은 처리량으로 인해 버려진 경우에는 dropped=true로 표시됩니다.
- 유형: counter (증가하는 값)
- 값: 처리된 로그 수와 해당 로그의 처리 여부(dropped 여부)

#### processedSpans_total
- 설명: BatchSpanProcessor에서 처리된 스팬의 총 수를 측정합니다. 높은 처리량으로 인해 버려진 경우에는 dropped=true로 표시됩니다.
- 유형: counter (증가하는 값)
- 값: 처리된 스팬 수와 해당 스팬의 처리 여부(dropped 여부)

#### queueSize_ratio
- 설명: BatchLogRecordProcessor 및 BatchSpanProcessor에서의 대기열 크기 비율을 측정합니다.
- 유형: gauge (순간적인 값)
- 값: 각 처리기 유형에 따른 대기열 크기 비율

#### target_info
- 설명: 대상 시스템의 메타데이터 정보를 제공합니다. 호스트, 인스턴스, OS, 프로세스 관련 정보 등이 포함됩니다.
- 유형: gauge (순간적인 값)
- 값: 대상 시스템에 대한 다양한 메타데이터 정보