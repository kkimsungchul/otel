# metric

## Java - metric 정리(auto)

### application_ready_time_seconds

- 설명: 애플리케이션이 요청을 처리할 수 있을 준비가 완료된 시간을 나타내는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 8.298 (초 단위로 준비된 시간)

### application_started_time_seconds

- 설명: 애플리케이션의 시작에 소요된 시간을 나타내는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 8.261 (초 단위로 시작된 시간)

### disk_free_bytes

- 설명: 지정된 경로의 사용 가능한 디스크 공간을 나타내는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 7.5841667072e+10 (바이트 단위의 사용 가능한 공간)

### disk_total_bytes

- 설명: 지정된 경로의 전체 디스크 공간을 나타내는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 4.99001602048e+11 (바이트 단위의 전체 공간)

### executor_active_threads

- 설명: 현재 활성화된 작업 실행 스레드의 근사치를 나타내는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 0 (현재 활성화된 스레드 수)

### executor_completed_tasks_total

- 설명: 완료된 작업의 총 수를 카운팅하는 카운터 메트릭입니다.
- 유형: 카운터
- 값: 0 (완료된 작업의 총 수)

### executor_pool_core_threads

- 설명: 스레드 풀의 핵심 스레드 수를 나타내는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 8 (스레드 풀의 핵심 스레드 수)

### executor_pool_max_threads

- 설명: 스레드 풀에서 허용되는 최대 스레드 수를 나타내는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 2.147483647e+09 (스레드 풀에서 허용되는 최대 스레드 수)

### http_server_request_duration_seconds(HTTP 서버 요청의 지속 시간)

- 설명: HTTP 요청의 처리 시간을 히스토그램으로 나타낸 것입니다. 이 메트릭은 요청의 처리 속도를 다양한 시간 구간(버킷)에 따라 분석할 수 있게 합니다.
- 유형: 히스토그램
- 값: 다양한 레이블과 함께 버킷 별 요청 수(http_server_request_duration_seconds_bucket)와 총 합(http_server_request_duration_seconds_sum)이 포함됩니다.

### http_server_requests_active_active(활성 HTTP 서버 요청 수)

- 설명: 현재 활성 상태인 HTTP 요청의 수를 보여주는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 요청의 성공 여부, HTTP 상태 코드, 메서드 등의 레이블과 함께 활성 요청의 수가 포함됩니다.

### http_server_requests_active_duration_seconds(활성 HTTP 서버 요청의 지속 시간)

- 설명: 현재 활성 상태인 HTTP 요청의 처리 시간을 보여주는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 요청의 성공 여부, HTTP 상태 코드, 메서드 등의 레이블과 함께 활성 요청의 지속 시간이 포함됩니다.

### http_server_requests_max_seconds(최대 HTTP 서버 요청 시간)

- 설명: HTTP 요청의 최대 처리 시간을 보여주는 게이지 메트릭입니다. 예를 들어, 404 클라이언트 오류와 성공적인 요청에 대한 최대 시간이 포함됩니다.
- 유형: 게이지
- 값: 오류 유형, 예외, HTTP 상태 코드, URI 등의 레이블과 함께 최대 처리 시간이 포함됩니다.

### http_server_requests_seconds(HTTP 서버 요청 시간)

- 설명: HTTP 요청의 처리 시간을 히스토그램으로 나타낸 메트릭입니다. 다양한 시간 구간(버킷)에 따라 요청의 수와 총 합을 보여줍니다.
- 유형: 히스토그램
- 값: 요청의 성공 여부, HTTP 상태 코드, 메서드 등의 레이블과 함께 버킷 별 요청 수(http_server_requests_seconds_bucket)와 총 합(http_server_requests_seconds_sum)이 포함됩니다.

### jdbc_connections_active(활성 JDBC 연결 수)

- 설명: 현재 활성 상태인 JDBC 연결의 수를 보여주는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 데이터 소스 이름과 함께 현재 활성 JDBC 연결 수가 포함됩니다.

### jdbc_connections_idle(유휴 JDBC 연결 수)

- 설명: 현재 연결되어 있으나 사용되지 않고 있는 JDBC 연결의 수를 보여주는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 데이터 소스 이름과 함께 현재 유휴 JDBC 연결 수가 포함됩니다.

### jdbc_connections_max(최대 JDBC 연결 수)

- 설명: 동시에 할당할 수 있는 최대 JDBC 연결 수를 보여주는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 데이터 소스 이름과 함께 최대 JDBC 연결 수가 포함됩니다.

### jdbc_connections_min(최소 JDBC 연결 수)

- 설명: 풀 내의 최소 유휴 JDBC 연결 수를 보여주는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 데이터 소스 이름과 함께 최소 JDBC 연결 수가 포함됩니다.

### jvm_buffer_count_buffers(JVM 버퍼 수)

- 설명: JVM 버퍼 풀 내에 있는 버퍼의 수를 보여주는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 버퍼 유형(ID), 인스턴스 이름과 함께 버퍼의 현재 수가 포함됩니다.

### jvm_buffer_memory_used_bytes(JVM 버퍼 메모리 사용량)

- 설명: JVM이 사용하는 각 버퍼 풀의 메모리 사용량을 보여주는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 버퍼 유형(ID), 인스턴스 이름과 함께 현재 메모리 사용량이 포함됩니다.

### jvm_buffer_total_capacity_bytes(JVM 버퍼 총 용량)

- 설명: JVM 버퍼 풀의 총 용량을 보여주는 게이지 메트릭입니다.
- 유형: 게이지
- 값: 버퍼 유형(ID), 인스턴스 이름과 함께 총 용량이 포함됩니다.

### jvm_classes_loaded

- 설명: 현재 Java 가상 머신에서 로드된 클래스의 수입니다.
- 유형: gauge (게이지)
- 값: 16740 (현재 로드된 클래스 수)

### jvm_classes_unloaded_total

- 설명: Java 가상 머신이 시작된 후 언로드된 총 클래스의 수입니다.
- 유형: counter (카운터)
- 값: 0 (언로드된 클래스의 총 수)

### jvm_compilation_time_milliseconds_total

- 설명: 컴파일에 소요된 누적 경과 시간의 근사치입니다.
- 유형: counter (카운터)
- 값: 21104 (컴파일에 소요된 총 시간)

### jvm_gc_live_data_size_bytes

- 설명: 가비지 컬렉션 후 재생성된 긴 생명 주기 힙 메모리 풀의 크기입니다.
- 유형: gauge (게이지)
- 값: 0 (재생성된 긴 생명 주기 힙 메모리 풀의 크기)

### jvm_gc_max_data_size_bytes

- 설명: 긴 생명 주기 힙 메모리 풀의 최대 크기입니다.
- 유형: gauge (게이지)
- 값: 8.581545984e+09 (최대 긴 생명 주기 힙 메모리 풀의 크기)

### jvm_gc_overhead

- 설명: GC 활동으로 인해 사용된 CPU 시간의 근사치입니다.
- 유형: gauge (게이지)
- 값: 0.00021302989657840556 (CPU 시간에서 GC 활동으로 사용된 비율)

### jvm_info

- 설명: JVM의 버전 정보입니다.
- 유형: gauge (게이지)
- 값: 1 (JVM의 버전 정보)

### jvm_memory_committed_bytes

- 설명: JVM이 사용할 수 있도록 커밋된 메모리 양입니다.
- 유형: gauge (게이지)
- 값: 다양한 힙 및 non-heap 영역에 대한 커밋된 메모리의 크기가 포함됩니다.

### jvm_memory_max_bytes

- 설명: 메모리 관리에 사용할 수 있는 최대 메모리 양입니다.
- 유형: gauge (게이지)
- 값: 다양한 힙 및 non-heap 영역에 대한 최대 메모리의 크기가 포함됩니다.

### jvm_memory_usage_after_gc

- 설명: 마지막 GC 이벤트 후 사용된 긴 생명 주기 힙 풀의 사용률입니다.
- 유형: gauge (게이지)
- 값: 0.003927191681176686 (GC 이후의 메모리 사용률)

### jvm_memory_used_bytes

- 설명: 사용된 메모리의 양입니다.
- 유형: gauge (게이지)
- 값: 다양한 힙 및 non-heap 영역에 대한 사용된 메모리의 크기가 포함됩니다.

### jvm_threads_daemon

- 설명: 현재 활성화된 데몬 스레드 수입니다.
- 유형: gauge (게이지)
- 값: 38 (현재 데몬 스레드 수)

### jvm_threads_live

- 설명: 현재 활성화된 모든 스레드(데몬 및 일반 스레드 모두 포함)의 수입니다.
- 유형: gauge (게이지)
- 값: 42 (현재 활성화된 스레드 수)

### jvm_threads_peak

- 설명: JVM 시작 이후 또는 피크가 재설정된 후의 최대 활성화 스레드 수입니다.
- 유형: gauge (게이지)
- 값: 43 (최대 활성화 스레드 수)

### jvm_threads_started_total

- 설명: JVM에서 시작된 총 응용 프로그램 스레드 수입니다.
- 유형: counter (카운터)
- 값: 46 (시작된 총 스레드 수)

### jvm_threads_states

- 설명: 다양한 스레드 상태별 현재 스레드 수입니다 (blocked, new, runnable, terminated, timed-waiting, waiting).
- 유형: gauge (게이지)
- 값: 각 상태에 따른 현재 스레드 수가 포함됩니다.

### logback_events_total

- 설명: 로그 레벨에 따라 활성화된 로그 이벤트의 총 수입니다.
- 유형: counter (카운터)
- 값: 각 로그 레벨별 활성화된 로그 이벤트의 총 수가 포함됩니다.

### otlp_exporter_exported_total

- 설명: OTLP(OpenTelemetry) 익스포터에 의해 내보낸 총 이벤트 수입니다.
- 유형: counter (카운터)
- 값: 성공적으로 내보낸 로그 및 span의 총 수가 포함됩니다.

### otlp_exporter_seen_total

- 설명: OTLP(OpenTelemetry) 익스포터에서 본 총 이벤트 수입니다.
- 유형: counter (카운터)
- 값: 전송된 로그 및 span의 총 수가 포함됩니다.

### process_cpu_time_nanoseconds_total

- 설명: Java 가상 머신 프로세스에서 사용된 CPU 시간입니다.
- 유형: counter (카운터)
- 값: 3.0203125e+10 (사용된 CPU 시간)

### process_cpu_usage

- 설명: Java 가상 머신 프로세스의 최근 CPU 사용률입니다.
- 유형: gauge (게이지)
- 값: 0.06963136597800318 (최근 CPU 사용률)

### process_start_time_seconds

- 설명: 프로세스의 시작 시간입니다 (Unix epoch 기준).
- 유형: gauge (게이지)
- 값: 1.719239489886e+09 (프로세스 시작 시간)

### process_uptime_seconds

- 설명: Java 가상 머신의 실행 시간(업타임)입니다.
- 유형: gauge (게이지)
- 값: 64.76 (업타임, 초 단위)

### processedLogs_total

- 설명: BatchLogRecordProcessor에서 처리된 로그의 총 수입니다.
- 유형: counter (카운터)
- 값: 이 값은 'dropped=true'로 표시될 수 있으며, 이 경우 처리된 로그가 고속 처리로 인해 삭제된 것을 나타냅니다.

### processedLogs_total

- 설명: BatchLogRecordProcessor에 의해 처리된 로그의 총 수입니다. dropped=false인 경우 처리된 모든 로그가 유효하게 처리되었음을 나타냅니다.
- 유형: counter
- 값: 26

### processedSpans_total

- 설명: BatchSpanProcessor에 의해 처리된 span의 총 수입니다. dropped=false인 경우 처리된 모든 span이 유효하게 처리되었음을 나타냅니다.
- 유형: counter
- 값: 21

### queueSize_ratio

- 설명: 각 프로세서 유형(BatchLogRecordProcessor 및 BatchSpanProcessor)의 큐 크기입니다. 큐 크기는 현재 0으로 나타내며, 이는 현재 대기 중인 항목이 없음을 의미합니다.
- 유형: gauge
- 값: 0

### spring_data_repository_invocations_max_seconds

- 설명: Spring Data 리포지토리 메서드 호출의 최대 지속 시간입니다. 메서드가 성공적으로 완료된 경우, 해당 메서드의 최대 실행 시간이 기록됩니다.
- 유형: gauge
- 값: 0.1156656 (findAll 메서드) 및 0.0509535 (save 메서드)

### spring_data_repository_invocations_seconds

- 설명: Spring Data 리포지토리 메서드 호출의 지속 시간 분포입니다. 메서드 호출의 성공 여부와 상관없이 호출된 메서드의 수와 총 실행 시간(sum)이 기록됩니다.
- 유형: histogram
- 값: findAll 메서드의 호출 수(count)는 3이며, 총 실행 시간(sum)은 0.1227282입니다. save 메서드의 호출 수(count)는 6이며, 총 실행 시간(sum)은 0.054681900000000006입니다.

### system_cpu_count

- 설명: Java 가상 머신에서 사용 가능한 프로세서(코어)의 수입니다.
- 유형: gauge
- 값: 8

### system_cpu_usage

- 설명: 시스템에서 실행 중인 응용 프로그램의 최근 CPU 사용률입니다.
- 유형: gauge
- 값: 0 (현재 0%의 CPU 사용률)

### target_info

- 설명: 애플리케이션의 타겟 메타데이터 정보입니다. 호스트 이름, 운영 체제 정보, 프로세스 정보, 서비스 버전 및 텔레메트리 관련 SDK 정보가 포함됩니다.
- 유형: gauge
- 값: deployment_environment=dev, env=dev, host_arch=amd64, host_name=sung-PC, os_description=Windows 10 10.0, os_type=windows, process_command_line=Java 실행 명령어,
process_executable_path=Java 실행 경로, process_pid=프로세스 ID, process_runtime_description=Java 실행 환경 설명, process_runtime_name=Java 실행 환경 이름,
process_runtime_version=Java 실행 환경 버전, service_version=서비스 버전, telemetry_distro_name=오픈텔레메트리 스프링 부트 스타터,
telemetry_distro_version=스프링 부트 스타터 버전, telemetry_sdk_language=자바, telemetry_sdk_name=오픈텔레메트리 SDK 이름, telemetry_sdk_version=오픈텔레메트리 SDK 버전

### tomcat_sessions_active_current

- 설명: 현재 활성화된 Tomcat 세션 수입니다.
- 유형: gauge
- 값: 0

### tomcat_sessions_active_max

- 설명: 최대 동시 활성화 가능한 Tomcat 세션 수입니다.
- 유형: gauge
- 값: 0

### tomcat_sessions_alive_max_seconds

- 설명: Tomcat 세션이 최대로 유지된 시간(초)입니다.
- 유형: gauge
- 값: 0

### tomcat_sessions_created_total

- 설명: 생성된 Tomcat 세션의 총 수입니다.
- 유형: counter
- 값: 0

### tomcat_sessions_expired_total

- 설명: 만료된 Tomcat 세션의 총 수입니다.
- 유형: counter
- 값: 0

### tomcat_sessions_rejected_total

- 설명: 거부된 Tomcat 세션의 총 수입니다.
- 유형: counter
- 값: 0