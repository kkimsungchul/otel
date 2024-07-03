## Python - metric 정리

#### http_server_active_requests
- 설명: 현재 서버에서 처리 중인 활성 HTTP 요청 수를 나타내며, 서버의 실시간 부하 상태를 파악하는 데 유용합니다.
- 유형: Gauge (지속적인 값)
- 값: 요청 수

#### http_server_duration_milliseconds_bucket
- 설명: HTTP 요청의 응답 시간을 밀리초 단위로 측정하여 히스토그램 버킷에 저장하며, 응답 시간의 분포를 분석하는 데 사용됩니다.
- 유형: Histogram (히스토그램 버킷)
- 값: 밀리초 단위의 응답 시간 버킷

#### process_runtime_cpython_context_switches_total
- 설명: 프로세스가 다른 작업으로 문맥 교환된 총 횟수를 누적하여 표시하며, 시스템의 문맥 전환 빈도를 모니터링하는 데 유용합니다.
- 유형: Counter (누적 값)
- 값: 교환 횟수

#### process_runtime_cpython_cpu_time_seconds_total
- 설명: 프로세스가 CPU에서 실행된 총 시간을 초 단위로 누적하여 표시하며, CPU 사용량을 모니터링하는 데 유용합니다.
- 유형: Counter (누적 값)
- 값: 초 단위 시간

#### process_runtime_cpython_cpu_utilization_ratio
- 설명: 프로세스의 CPU 사용 비율을 백분율로 표시하며, 0에서 1까지의 값으로 나타내어 CPU 자원의 활용도를 측정합니다.
- 유형: Gauge (지속적인 값)
- 값: 백분율 비율 (0-1)

#### process_runtime_cpython_gc_count_bytes_total
- 설명: 프로세스에서 가비지 컬렉션에 의해 해제된 총 메모리 바이트 수를 누적하여 표시하며, 메모리 관리 효율성을 평가합니다.
- 유형: Counter (누적 값)
- 값: 바이트 수

#### process_runtime_cpython_memory_bytes
- 설명: 현재 프로세스가 사용하는 총 메모리 크기를 바이트 단위로 표시하며, 메모리 사용 현황을 모니터링하는 데 유용합니다.
- 유형: Gauge (지속적인 값)
- 값: 바이트 단위 메모리 크기

#### process_runtime_cpython_thread_count
- 설명: 현재 프로세스에서 실행 중인 스레드의 총 수를 표시하며, 멀티스레딩 환경에서 스레드 사용 현황을 모니터링합니다.
- 유형: Gauge (지속적인 값)
- 값: 스레드 수

#### system_cpu_time_seconds_total
- 설명: 시스템 전체에서 CPU가 작업을 수행한 총 시간을 초 단위로 누적하여 표시하며, 시스템의 CPU 활용도를 평가합니다.
- 유형: Counter (누적 값)
- 값: 초 단위 시간

#### system_cpu_utilization_ratio
- 설명: 시스템의 CPU 사용 비율을 백분율로 표시하며, 0에서 1까지의 값으로 시스템 CPU 자원의 활용도를 측정합니다.
- 유형: Gauge (지속적인 값)
- 값: 백분율 비율 (0-1)

#### system_disk_io_bytes_total
- 설명: 시스템 전체에서 디스크 I/O 작업으로 처리된 총 바이트 수를 누적하여 표시하며, 디스크 사용량을 모니터링합니다.
- 유형: Counter (누적 값)
- 값: 바이트 수

#### system_disk_operations_total
- 설명: 시스템 전체에서 수행된 디스크 I/O 작업의 총 횟수를 누적하여 표시하며, 디스크 작업 빈도를 모니터링합니다.
- 유형: Counter (누적 값)
- 값: 작업 횟수

#### system_disk_time_seconds_total
- 설명: 시스템 전체에서 디스크 I/O 작업에 소요된 총 시간을 초 단위로 누적하여 표시하며, 디스크 작업 효율성을 평가합니다.
- 유형: Counter (누적 값)
- 값: 초 단위 시간

#### system_memory_usage_bytes
- 설명: 시스템 전체에서 현재 사용 중인 메모리의 총 크기를 바이트 단위로 표시하며, 메모리 사용 현황을 모니터링합니다.
- 유형: Gauge (지속적인 값)
- 값: 바이트 단위 메모리 크기

#### system_memory_utilization_ratio
- 설명: 시스템 메모리 사용 비율을 백분율로 표시하며, 0에서 1까지의 값으로 시스템 메모리 자원의 활용도를 측정합니다.
- 유형: Gauge (지속적인 값)
- 값: 백분율 비율 (0-1)

#### system_network_connections
- 설명: 현재 시스템에서 활성화된 네트워크 연결의 총 수를 표시하며, 네트워크 연결 상태를 모니터링합니다.
- 유형: Gauge (지속적인 값)
- 값: 연결 수
#### system_network_dropped_packets_total
- 설명: 시스템 전체에서 네트워크에서 드롭된 총 패킷 수를 누적하여 표시하며, 네트워크 성능 문제를 식별하는 데 유용합니다.
- 유형: Counter (누적 값)
- 값: 드롭된 패킷 수
#### system_network_errors_total
- 설명: 시스템 전체에서 발생한 총 네트워크 오류 수를 누적하여 표시하며, 네트워크 안정성을 평가하는 데 유용합니다.
- 유형: Counter (누적 값)
- 값: 오류 수

#### system_network_io_bytes_total
- 설명: 시스템 전체에서 전송된 총 네트워크 I/O 바이트 수를 누적하여 표시하며, 네트워크 트래픽을 모니터링합니다.
- 유형: Counter (누적 값)
- 값: 바이트 수

#### system_network_packets_total
- 설명: 시스템 전체에서 처리된 총 네트워크 패킷 수를 누적하여 표시하며, 네트워크 활동을 모니터링합니다.
- 유형: Counter (누적 값)
- 값: 패킷 수

#### system_swap_usage_pages
- 설명: 현재 시스템에서 사용 중인 스왑 페이지의 총 수를 표시하며, 스왑 메모리 사용 현황을 모니터링합니다.
- 유형: Gauge (지속적인 값)
- 값: 페이지 수

#### system_swap_utilization_ratio
- 설명: 시스템 스왑 메모리 사용 비율을 백분율로 표시하며, 0에서 1까지의 값으로 스왑 메모리 자원의 활용도를 측정합니다.
- 유형: Gauge (지속적인 값)
- 값: 백분율 비율 (0-1)

#### system_thread_count
- 설명: 현재 시스템에서 실행 중인 총 스레드 수를 표시하며, 멀티스레딩 환경에서 스레드 사용 현황을 모니터링합니다.
- 유형: Gauge (지속적인 값)
- 값: 스레드 수