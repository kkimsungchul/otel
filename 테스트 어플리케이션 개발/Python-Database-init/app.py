from utils.database import database_init
from utils.timeUtil import now

if __name__ == '__main__':
    print("database init start : " + now())
    database_init()
    print("database init end : "+now())


