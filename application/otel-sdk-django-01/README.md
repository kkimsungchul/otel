## 개발환경
- python 3.12
- sqlite3 database

## 로그 조회
localhost:8080

## 가상환경 생성 및 라이브러리 설치
### python version 3.12
- pip install virtualenv
- virtualenv venv --python=python3.12
- venv\Scripts\activate
- pip install django

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