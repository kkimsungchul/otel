from sqlalchemy import create_engine, Column, Integer, String, DateTime
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from opentelemetry.instrumentation.sqlalchemy import SQLAlchemyInstrumentor

# SQLAlchemy Base
Base = declarative_base()

# SQLAlchemy Models
class log_api(Base):
    __tablename__ = 'log_api'
    seq = Column(Integer, primary_key=True, autoincrement=True)
    user_ip = Column(String(15))
    user_id = Column(String(30))
    start_time = Column(DateTime)
    end_time = Column(DateTime)
    call_url = Column(String(100))
    call_url_parameter = Column(String(300))

class board(Base):
    __tablename__ = 'board'
    seq = Column(Integer, primary_key=True, autoincrement=True)
    title = Column(String(30))
    content = Column(String(2000))
    create_date = Column(DateTime)
    update_date = Column(DateTime)

# SQLite 엔진 생성
engine = create_engine('sqlite:///example.db')
SQLAlchemyInstrumentor().instrument(engine=engine)

# 데이터베이스 테이블 생성
Base.metadata.create_all(engine)

# 세션 생성
Session = sessionmaker(bind=engine)
session = Session()
