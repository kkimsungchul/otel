# Mac에서 Otel 테스트 환경 구성하기(test applicatoin)
## 목차
- [Otel 환경 구성](#otel-환경-구성)
- [Otel Collector](#otel-collector)

---
## Otel 환경 구성
### git 코드 내려받기
```shell
git clone https://github.com/kkimsungchul/otel.git
```
## 환경 구성
- java 17
- python 3.x
- ※ ~user-path는 본인이 사용할 경로를 설정
---
## Otel Collector

### collector 설치
```shell
curl --proto '=https' --tlsv1.2 -fOL https://github.com/open-telemetry/opentelemetry-collector-releases/releases/download/v0.100.0/otelcol_0.100.0_darwin_arm64.tar.gz
tar -xvf otelcol_0.100.0_darwin_arm64.tar.gz
```
### collector 권한부여
```shell
chmod +x /Users/jihyoun/Otel/otelcol
```

### collector 경로로 이동
```shell
cd 2024/otel/otel-collector
```

### customconfig.yaml 파일 검증
```shell
./otelcol validate --config=customconfig.yaml
```

### collector 실행
```shell
./otelcol --config=customconfig.yaml
```
