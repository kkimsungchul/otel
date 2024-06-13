## 개발환경
- python 3.12
- sqlite3 database

## 가상환경 생성 및 라이브러리 설치 (python 3.12)
### 개별 패키지 설치를 통한 환경 설정
```
pip install virtualenv
virtualenv venv --python=python3.12
venv\Scripts\activate
pip install django
pip install psutil
pip install opentelemetry-instrumentation-django
pip install opentelemetry-exporter-otlp-proto-grpc
python manage.py runserver
```
### requirements.txt 파일을 사용하여 의존성 관리
```
pip install virtualenv
virtualenv venv --python=python3.12
venv\Scripts\activate
pip install -r requirements.txt
python manage.py runserver
```


## Django 마이그레이션
### 마이그레이션 생성
- python manage.py makemigrations djangotest
### 마이그레이션 적용
- python manage.py migrate djangotest
### 마이그레이션 확인
- python manage.py showmigrations

## Django 앱 실행
- python manage.py runserver

## DB 적재
- python manage.py boards

## url
## 내부 접속 시
### boards api
localhost:8000/boards/{가져올 게시글 개수}
### log_api
localhost:8000/save/

## 외부 접속 시
### boards api
192.168.0.40:8000/boards/{가져올 게시글 개수}
### log_api
192.168.0.40:8000/save/


## batch 파일 생성

## nginx 설정
### hosts 파일 수정
- 192.168.0.52 java.sdk.otel-app.co.kr
- 127.0.0.1 python.sdk.otel-app.co.kr

### confing 파일 수정
- C:\Users\HP\Downloads\nginx-1.26.1\conf\nginx.conf

https://github.com/dynatrace-oss/opentelemetry-metric-python/blob/main/example/basic_example.py

---
현재는 ip_address = request.META["HTTP_X_REAL_IP"] 로 IP를 가져오도록 하고있음

다른 환경에서 안될 경우에는 아래의 내용을 참고 해서 적용
pip install django-ipware
```python
from ipware.ip import get_ip

def get_client_ip(request):
    ip = get_ip(request)
    if ip is not None:
        print(ip)
    else:
        print ("없음")
```
