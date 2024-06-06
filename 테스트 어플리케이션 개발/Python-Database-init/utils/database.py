import sqlite3
from datetime import datetime

# 데이터베이스 파일명
DATABASE_PATH = "python_sqlite3.db"

# 데이터베이스 연결
def connect_to_database():
    conn = sqlite3.connect(DATABASE_PATH, check_same_thread=False)
    cursor = conn.cursor()
    return conn, cursor


# 데이터베이스 연결 종료
def close_database_connection(cursor, conn):
    cursor.close()
    conn.commit()
    conn.close()

# 데이터베이스 테이블 생성
def database_create():
    conn, cursor = connect_to_database()
    create_scan_name_table_query = '''
        CREATE TABLE IF NOT EXISTS log_api (
            seq INTEGER PRIMARY KEY AUTOINCREMENT,
            user_ip VARCHAR(15),
            user_id VARCHAR(30),
            start_time DATE DEFAULT (datetime('now', 'localtime')),
            end_time DATE DEFAULT (datetime('now', 'localtime')),
            call_url VARCHAR(100),
            call_url_parameter VARCHAR(300)
        )
    '''
    cursor.execute(create_scan_name_table_query)

    create_scan_name_table_query = '''
        CREATE TABLE IF NOT EXISTS  board (
            seq INTEGER PRIMARY KEY AUTOINCREMENT,
            title VARCHAR(30),
            content VARCHAR(2000),
            create_date DATETIME DEFAULT (datetime('now', 'localtime')),
            update_date DATETIME DEFAULT (datetime('now', 'localtime'))
        )
    '''
    cursor.execute(create_scan_name_table_query)
    close_database_connection(cursor, conn)

# 데이터베이스 초기 데이터 생성
def database_data_insert():
    conn, cursor = connect_to_database()

    # 1000만건  insert 시 데이터베이스 용량이 7.4기가
    for i in range(100):
        title = f"제목{i}"
        content = f"내용{i}"
        insertQuery = 'INSERT INTO board(title , content) VALUES (?,?)'
        cursor.execute(insertQuery, (title, content))

    cursor.fetchall()
    close_database_connection(cursor, conn)


# log insert
# insert 후 seq를 리턴함
def log_insert(userIp , userId , callUrl , callUrlParameter):
    conn, cursor = connect_to_database()
    query = '''
        INSERT INTO log_api(user_ip , user_id ,  call_url , call_url_parameter)
        VALUES (?,?,?,?)    
    '''
    cursor.execute(query, (userIp, userId,callUrl,callUrlParameter))
    seq = cursor.lastrowid
    close_database_connection(cursor, conn)
    return seq

# log upodate
def log_update(seq):
    conn, cursor = connect_to_database()
    current_time = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    query = '''
        UPDATE log_api SET end_time = ? where seq = ?
    '''
    cursor.execute(query, (current_time, seq))
    cursor.fetchall()
    close_database_connection(cursor, conn)

def database_init():
    database_create()
    database_data_insert()

