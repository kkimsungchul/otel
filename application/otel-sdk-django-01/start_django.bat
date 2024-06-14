@echo off
REM Create a virtual environment
echo ## python -m venv venv
python -m venv venv

REM Activate the virtual environment
echo ## call venv\Scripts\activate
call venv\Scripts\activate

REM Install the waitress
echo ## pip install waitress
pip install waitress

REM Install the requirements
echo ## pip install -r requirements.txt
pip install -r requirements.txt

REM Make migrations for the djangotest app
echo ## python manage.py makemigrations djangotest
python manage.py makemigrations djangotest

REM Apply the migrations for the djangotest app
echo ## python manage.py migrate djangotest
python manage.py migrate djangotest

REM Show all migrations
echo ## python manage.py showmigrations
python manage.py showmigrations

REM Custom command to handle boards
echo ## python manage.py boards
python manage.py boards

REM Run the Django development server
REM echo ## python manage.py runserver
REM python manage.py runserver

REM Run the Django with waitress
echo ## waitress-serve --port=8000 djangotest.wsgi:application
waitress-serve --port=8000 djangotest.wsgi:application

REM Deactivate the virtual environment
echo ## call venv\Scripts\deactivate
REM call venv\Scripts\deactivate