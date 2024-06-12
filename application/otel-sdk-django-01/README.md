## 개발환경
- python 3.12
- sqlite3 database

## 가상환경 생성 및 라이브러리 설치
### python version 3.12
```
pip install virtualenv
virtualenv venv --python=python3.12
venv\Scripts\activate
pip install django
pip install opentelemetry-instrumentation-django
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

## grpc 프로토콜 설정
- cd djangotest
- python -m grpc_tools.protoc -Ipb --python_out=pb --grpc_python_out=pb pb/demo.proto
- demo_pb2_grpc.py의 import demo_pb2 as demo__pb2를 from . import demo_pb2 as demo__pb2