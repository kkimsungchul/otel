from flask import Flask, request, jsonify, Response
from sqlalchemy import create_engine, Column, Integer, String, DateTime
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, scoped_session
from datetime import datetime
import secrets, string

# Flask 애플리케이션 초기화
app = Flask(__name__)

# SQLAlchemy 설정
engine = create_engine('sqlite:///example.db')
Base = declarative_base()
def get_random_string(length=12):
    characters = string.ascii_letters + string.digits
    return ''.join(secrets.choice(characters) for _ in range(length))

class LogAPI(Base):
    __tablename__ = 'log_api'
    seq = Column(Integer, primary_key=True, autoincrement=True)
    user_ip = Column(String(15))
    user_id = Column(String(30))
    start_time = Column(DateTime)
    end_time = Column(DateTime)
    call_url = Column(String(100))
    call_url_parameter = Column(String(300))

class Board(Base):
    __tablename__ = 'board'
    seq = Column(Integer, primary_key=True, autoincrement=True)
    title = Column(String(30))
    content = Column(String(2000))
    create_date = Column(DateTime)
    update_date = Column(DateTime)

Base.metadata.create_all(engine)
Session = sessionmaker(bind=engine)
session = scoped_session(Session)

def get_client_ip():
    return request.remote_addr or "127.0.0.1"

@app.route('/board/<int:num_items>/', methods=['GET'])
def get_data(num_items):
    start_time = datetime.now()

    BoardData = session.query(Board).limit(num_items).all()

    data_list = [
        {
            "seq": item.seq,
            "title": item.title,
            "content": item.content,
            "create_date": item.create_date,
            "update_date": item.update_date,
        }
        for item in BoardData
    ]


    end_time = datetime.now()
    duration = (end_time - start_time).total_seconds() * 1000
    client_ip = get_client_ip()

    LogData = LogAPI(
        user_ip=client_ip,
        user_id=get_random_string(30),
        start_time=start_time,
        end_time=end_time,
        call_url=request.path,
        call_url_parameter=str(num_items)
    )

    session.add(LogData)
    session.commit()

    return jsonify(data_list) if num_items <= 100 else f"Successfully fetched {num_items} data items in {duration:.2f} ms"

@app.route('/board/', methods=['GET'])
def get_data_all():

    BoardData = session.query(Board).all()
    data_list = [
        {
            "seq": item.seq,
            "title": item.title,
            "content": item.content,
            "create_date": item.create_date,
            "update_date": item.update_date,
        }
        for item in BoardData
    ]

    return jsonify(data_list)

@app.route('/log/', methods=['GET'])
def log_data():
    LogData = session.query(LogAPI).all()

    data_list = [
        {
            "seq": item.seq,
            "user_ip": item.user_ip,
            "user_id": item.user_id,
            "start_time": item.start_time,
            "end_time": item.end_time,
            "call_url": item.call_url,
            "call_url_parameter": item.call_url_parameter,
        }
        for item in LogData
    ]

    return jsonify(data_list)

@app.route('/user/')
def get_user():
    try:
        raise Exception('get_user error')
    except Exception as e:  # 예외가 발생했을 때 실행됨
        return e  # JSON 형식으로 에러 메시지와 500 상태 코드 반환
    return 'error~~'
@app.route('/')
def home():
    return "Welcome to my Flask app!"

if __name__ == '__main__':
    app.run(port=5001)
