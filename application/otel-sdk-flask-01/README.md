## 개발환경
- python 3.12
- sqlite3 database

## 가상환경 생성 및 라이브러리 설치 (python 3.12)
### 개별 패키지 설치를 통한 환경 설정
```
pip install virtualenv
virtualenv venv --python=python3.12
venv\Scripts\activate
pip install Flask
pip install Flask-SQLAlchemy Flask-Migrate
pip install opentelemetry-api opentelemetry-sdk opentelemetry-instrumentation-flask opentelemetry-exporter-otlp
pip install pytz
pip install psutil
```
### requirements.txt 파일을 사용하여 의존성 관리
```
pip install virtualenv
virtualenv venv --python=python3.12
venv\Scripts\activate
pip install -r requirements.txt
python manage.py runserver
```