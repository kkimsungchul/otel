# Jaeger(예거) 사용법

# 목차



## 참고 링크
- URL : httpstwofootdog.tistory.com67
- URL : https://blog.advenoh.pe.kr/cloud/Jaeger%EC%97%90-%EB%8C%80%ED%95%9C-%EC%86%8C%EA%B0%9C/
- URL :https://afsdzvcx123.tistory.com/entry/%EC%9D%B8%ED%94%84%EB%9D%BC-Jaeger-OpenTelemetry-Grafana-%EC%97%B0%EB%8F%99
- URL : https://tommypagy.tistory.com/618
- 
## 다운로드 링크
Binaries 파일로 다운로드
- URL : https://www.jaegertracing.io/download/
- Windows : https://github.com/jaegertracing/jaeger/releases/download/v1.57.0/jaeger-1.57.0-windows-amd64.tar.gz



## 실행
```shell
cd C:\Users\sung\Desktop\otel\jaeger-1.57.0-windows-amd64
jaeger-all-in-one.exe 
```

## 접속
- URL : http://localhost:16686

## OpenTelemetry 연동
참고 링크 : https://www.jaegertracing.io/docs/1.21/opentelemetry/

## 오류
```text
* error decoding 'exporters': unknown type: "jaeger" for id: "jaeger" (valid values: [logging otlp prometheus prometheusremotewrite debug nop otlphttp file kafka opencensus zipkin])
2024/06/04 01:07:29 collector server run finished with error: failed to get config: cannot unmarshal the configuration: 1 error(s) decoding:
```
jaeger에서 gRPC를 지원해주면서 OpenTelemetry Collector는 OpenTelemetry SDK와 Jaeger 백엔드 사이에 배포할 필요가 없어짐

- 2023년 11월

    https://stackoverflow.com/questions/77475771/error-when-running-otel-collector-with-jaeger-in-docker-containers
  
- 2023년 9월

    https://github.com/open-telemetry/opentelemetry-collector-contrib/issues/26685

- jaeger 릴리즈 노트

    https://www.jaegertracing.io/docs/1.38/apis/#opentelemetry-protocol-stable

- OpenTelemetry 릴리즈 노트

    https://github.com/open-telemetry/opentelemetry-collector-releases/releases/tag/v0.85.0
    https://github.com/open-telemetry/opentelemetry-collector-releases/pull/396
    https://github.com/open-telemetry/opentelemetry.io/pull/3273
