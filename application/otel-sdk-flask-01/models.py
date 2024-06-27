from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()

class LogAPI(db.Model):
    __tablename__ = 'log_api'
    seq = db.Column(db.Integer, primary_key=True, autoincrement=True)
    user_ip = db.Column(db.String(15))
    user_id = db.Column(db.String(30))
    start_time = db.Column(db.DateTime)
    end_time = db.Column(db.DateTime)
    call_url = db.Column(db.String(100))
    call_url_parameter = db.Column(db.String(300))

class Board(db.Model):
    __tablename__ = 'board'
    seq = db.Column(db.Integer, primary_key=True, autoincrement=True)
    title = db.Column(db.String(30))
    content = db.Column(db.String(2000))
    create_date = db.Column(db.DateTime)
    update_date = db.Column(db.DateTime)
