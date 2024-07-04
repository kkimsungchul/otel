
# 프로메테우스란?
Prometheus는 오픈 소스 시스템 모니터링 및 경고 도구입니다. Prometheus는 SoundCloud에서 시작되었으며, 현재는 CNCF(Cloud Native Computing Foundation)의 프로젝트로 관리되고 있습니다. Prometheus는 다음과 같은 주요 기능과 특징을 가지고 있습니다:

### 프로메테우스 구성 요소
- Prometheus 서버: 데이터를 수집하고 저장하며, 사용자가 쿼리를 통해 데이터를 조회할 수 있게 합니다.

- Pushgateway: 짧은 수명을 가지는 잡(job) 또는 배치 작업(batch job)에서 메트릭 데이터를 푸시할 수 있게 합니다.
- 클라이언트 라이브러리: 애플리케이션에서 메트릭 데이터를 수집하고 Prometheus 서버에 제공하는 데 사용됩니다.
다양한 언어를 지원합니다: Go, Java, Python, Ruby 등.

- Exporters: 기존의 모니터링 시스템이나 서비스로부터 메트릭을 수집하여 Prometheus 형식으로 변환하는 데 사용됩니다. 
``` Node Exporter, Blackbox Exporter, MySQL Exporter ```   

- Alertmanager: 알림을 관리하고 라우팅합니다.
```이메일, Slack, PagerDuty```

### 사용 사례
- 시스템 모니터링: CPU, 메모리, 디스크 I/O와 같은 시스템 리소스를 모니터링합니다.
- 애플리케이션 모니터링: 애플리케이션의 성능 메트릭과 오류를 모니터링합니다.
- 인프라스트럭처 모니터링: 클라우드 서비스, 컨테이너, 오케스트레이션 시스템(Kubernetes) 등을 모니터링합니다.


# Prometheus(프로메테우스) 사용 방법

### 설치 파일 다운로드
	https://prometheus.io/download/

### prometheus.yml 설정
※ 프로메테우스 다운로드 후 압축을 푼다음, exe 파일이 있는 경로에 yml파일을 생성

```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: "prometheus"
    static_configs:
    - targets: ["localhost:9090"]

  - job_name: dice-service          # 서비스 명
    scrape_interval: 5s
    static_configs:
      - targets: ["localhost:9464"]  # 로컬에서 실행 중인 서비스의 포트
```

### 실행
	prometheus.exe

### 접속
	http://localhost:9090/

### 데이터 저장 용량
Prometheus가 로컬 디스크에 얼마나 많은 데이터를 저장할 수 있는지는 사용 가능한 디스크 용량과 설정에 따라 달라집니다. 실제로 Prometheus의 데이터 저장 용량에는 다음 요소들이 영향을 미칩니다

- 스크레이프 주기: 얼마나 자주 메트릭 데이터를 수집하는지
- 타겟 수: 모니터링되는 타겟의 수와 각 타겟에서 수집되는 메트릭의 수
- 데이터 압축: Prometheus는 효율적인 압축 알고리즘을 사용하여 데이터 저장 


### 추가 설정 옵션 (prometheus 실행 시 전달할 수 있는 명령어)
이 설정은 Prometheus가 30일 동안 최대 100GB의 데이터를 저장하도록 설정합니다. 실제로 이러한 설정 값은 모니터링 요구 사항에 맞게 조정해야 합니다.
```
--storage.tsdb.path=/path/to/prometheus/data
--storage.tsdb.retention.time=30d
--storage.tsdb.retention.size=100GB
--storage.tsdb.min-block-duration=2h
--storage.tsdb.max-block-duration=36h
```

## 참고 자료
- Prometheus 공식 웹사이트

https://prometheus.io/docs/introduction/overview/

- Prometheus GitHub 리포지토리

https://github.com/prometheus/prometheus



