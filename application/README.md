

### Data Flow Overview
![data-falow-overview.png](/application/data-falow-overview.png)

### 실행 방법
- nginx ,spring , django , collector
  - all_run_server.bat
- prometheus
  - prometheus.exe
- jaeger
  - jaeger-all-in-one.exe 

※ 프로메테우스와 예거는 현재 프로젝트에 포함되어 있지 않으며 별도로 실행해야 합니다.


### 종료 방법
- spring , django , collector , prometheus , jaeger
    - 실행중인 CMD창을 닫으면 종료
- nginx
    - 윈도우 작업 관리자에서 종료

### 접속 방법
#### nginx
- http://localhost

#### spring - sdk
- http://localhost/java/board/10
- http://localhost:8080/board/10

#### spring - agent
- http://localhost/agent-java/board/10
- http://localhost:5050/board/10

#### spring - auto
- http://localhost/auto-java/board/10
- http://localhost:6060/board/10

### django - sdk
- http://localhost/python/board/10/
- http://localhost:8000/board/10/

### django - auto
- http://localhost/auto-python/board/10/
- http://localhost:8001/board/10/

#### collector
- http://localhost/otlp/metrics
- http://localhost:9464/metrics

#### prometheus
- http://localhost/prometheus/graph
- http://localhost:9090/graph

#### jaeger
- http://localhost:16686/search

#### grafana
- http://localhost:3000 

#### loki
- http://localhost:3100 