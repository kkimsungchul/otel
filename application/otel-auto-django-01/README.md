## 개발환경
- python 3.12
- sqlite3 database

## 가상환경 생성 및 라이브러리 설치 (python 3.12)
### 1. 개별 패키지 설치를 통한 환경 설정 (django)
``` 
pip install virtualenv
virtualenv venv --python=python3.12
venv\Scripts\activate
pip install django
python.exe -m pip install --upgrade pip
python -m django startproject otelautodjango01
```
### 2. django migration
```
python manage.py makemigrations otelautodjango01
python manage.py migrate otelautodjango01
```
### 3. db 적재
```
python manage.py boards
```
### 3. 개별 패키지 설치를 통한 환경 설정 (opentelementry)
```
pip install opentelemetry-api
pip install opentelemetry-sdk
pip install opentelemetry-instrumentation
opentelemetry-bootstrap -a install
```
### 4. 실행 명령어
```
opentelemetry-instrument --traces_exporter console --metrics_exporter none python manage.py runserver
```