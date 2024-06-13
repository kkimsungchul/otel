

### 실행 방법
- nginx ,spring , django , collector
  - all_run_server.bat
- prometheus
  - prometheus.exe

※ 프로메테우스는 현재 프로젝트에 포함되어 있지 않으며 별도로 실행해야 합니다.

### 종료 방법
- spring , django , collector , prometheus 
    - 실행중인 CMD창을 닫으면 종료
- nginx
    - 윈도우 작업 관리자에서 종료

### 접속 방법
#### nginx
- http://localhost

#### spring
- http://localhost/java/board/2
- http://localhost:8080/board/2

### django
- http://localhost/python/boards/10/
- http://localhost:8000/boards/10/

#### collector
- http://localhost/otlp/metrics
- http://localhost:9464/metrics

#### prometheus
- http://localhost/prometheus/graph
- http://localhost:9090/graph