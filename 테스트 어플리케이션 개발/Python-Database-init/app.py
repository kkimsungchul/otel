from utils.database import database_init, log_insert, log_update
from utils.timeUtil import now
import time

if __name__ == '__main__':
    print("database init start : " + now())
    # 데이터베이스 초기화
    database_init()
    print("database init end : "+now())
    
    # 여기서부터는 api가 호출됐다고 가정
    # api 시작 시간 입력
    seq = log_insert("192.168.0.100" , "kimsungchul" , "/python/boards" , "10000000")
    print(seq)

    #서비스 로직 실행
    time.sleep(5)
    # 서비스 로직 종료
    
    # api 종료 시간 업데이트
    log_update(seq)


