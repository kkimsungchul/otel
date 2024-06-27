
from flask import Flask
# 이번 문서에서 작성한 모듈을 로드한다.
from heapsy import HeapDendrogram


class LeakObj(object):
    data = 1


app = Flask(__name__)
leakable = []


@app.route('/leakable')
def leak():
    # API 요청이 들어올 때 마다 global에 정의된 leackable 리스트에
    # LeakObj 인스턴스가 쌓이게 된다.
    global leakable
    leakable.append(LeakObj())

    return f'hi {len(leakable)}'


@app.route('/metrics')
def heap_usage():
    # 힙을 사용량을 추적하기 위한 prometheus metric endpoint를 추가한다.
    hd = HeapDendrogram()
    hd.generate()

    return hd.as_prometheus_metric()


if __name__ == '__main__':
    # localhost:5000 에서 listen
    app.run()