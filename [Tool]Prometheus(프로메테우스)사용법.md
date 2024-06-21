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

