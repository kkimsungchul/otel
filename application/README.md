

### Data Flow Overview
![data-flow-overview.png](/application/data-flow-overview.png)
![image_OTel](https://github.com/kkimsungchul/otel/assets/70503803/2bb8eaf5-eef4-4686-90a1-61787d7224b1)

## 실행 방법
- nginx, spring, django, flask, collector
  - all_run_server.bat
- flask
  - flask_run_server.bat
- prometheus, jaeger, grafana
  - dashboard_run.bat
- Jmeter
  - Jmeter_run.bat

※ dashboard_run.bat에서는 모니터링 툴의 경로가 절대경로로 잡혀 있어서, 각자 서버 환경에 맞게 수정해줘야 합니다.
ex) C:/jaeger-1.58.0-windows-amd64/jaeger-all-in-one.exe


## 종료 방법
- spring , django , collector , prometheus , jaeger
    - 실행중인 CMD창을 닫으면 종료
- nginx
    - 윈도우 작업 관리자에서 종료


## 접속 방법
---
### nginx 접속 확인
#### nginx
- http://localhost

---
### 테스트 어플리케이션
#### spring - sdk
- [nginx에 등록된 url] http://localhost/java/board/10
- [default url] http://localhost:8080/board/10

#### spring - agent
- [nginx에 등록된 url] http://localhost/agent-java/board/10
- [default url] http://localhost:5050/board/10

#### spring - auto
- [nginx에 등록된 url] http://localhost/auto-java/board/10
- [default url] http://localhost:6060/board/10

#### django - sdk
- [nginx에 등록된 url] http://localhost/django/board/10
- [default url] http://localhost:8000/board/10

#### django - auto
- [nginx에 등록된 url] http://localhost/auto-django/board/10
- [default url] http://localhost:8001/board/10
 
#### flask - sdk
- [nginx에 등록된 url] http://localhost/flask/board/10
- [default url] http://localhost:5000/board/10

#### flask - auto
- [nginx에 등록된 url] http://localhost/auto-flask/board/10
- [default url] http://localhost:5001/board/10

---
### 오픈텔레메트리 콜렉터
#### collector
- [nginx에 등록된 url] http://localhost/otlp/metrics
- [default url] http://localhost:9464/metrics

--- 
### 오픈텔레메트리 모니터링 백엔드 시스템
#### prometheus
- [nginx에 등록된 url] http://localhost/prometheus/graph
- [default url] http://localhost:9090/graph

#### jaeger
- [default url] http://localhost:16686/search

#### loki
- [default url] http://localhost:3100 

---
### 통합 모니터링
#### grafana
- [default url] http://localhost:3000 
