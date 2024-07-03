@echo off
REM Create a virtual environment
echo ## python -m venv venv
python -m venv venv

REM Activate the virtual environment
echo ## call venv\Scripts\activate
call venv\Scripts\activate

REM Install the requirements
echo ## pip install -r requirements.txt
pip install -r requirements.txt

REM Apply the migrations for the otelsdkdjango01 app
echo ## python manage.py migrate
python manage.py migrate

REM Show all migrations
echo ## python manage.py showmigrations
python manage.py showmigrations

REM Custom command to handle boards
echo ## python manage.py boards
python manage.py boards

REM Run the Django development server
echo ## python manage.py runserver
python manage.py runserver

REM Deactivate the virtual environment
echo ## call venv\Scripts\deactivate
REM call venv\Scripts\deactivate