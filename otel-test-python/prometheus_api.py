import requests
from datetime import datetime, timedelta


def prometheus_data(query, hours=5):
    url = 'http://127.0.0.1:9090/api/v1/targets?state=active'
    # 프로메테우스는 utc 기준으로 시간 설정되어 있음
    end_time = datetime.utcnow()
    start_time = end_time - timedelta(hours=hours)
    params = {
        'query': query,
        'start': start_time.isoformat() + 'Z',
        'end': end_time.isoformat() + 'Z',
        'step': '60s'  # 1분 간격
    }

    # API 호출 파라미터 로깅
    print(f"API params: {params}")

    response = requests.get(url, params=params)
    data = response.json()

    if data['status'] == 'success' and data['data']:
        return data['data']
    else:
        return data.get('error', 'No data found or error in query')


query = 'jvm_memory_used_bytes{instance="127.0.0.1:9090", job="otel-test"}'
print(prometheus_data(query))