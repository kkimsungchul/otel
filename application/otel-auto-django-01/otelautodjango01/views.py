from django.http import JsonResponse
from django.utils import timezone
from django.utils.crypto import get_random_string

# 메서드 임포트
from .models import log_api, board
from django.http import HttpResponse
import logging

logger = logging.getLogger("otelautodjango01")

def get_client_ip(request):
    # ip_address = request.META["HTTP_X_REAL_IP"]
    ip_address = "127.0.0.1"
    return ip_address


def get_data(request, num_items):
    print("???")
    logger.info("## test message~~")
    start_time = timezone.now()

    data = board.objects.all()[:num_items].values('seq',
                                                  'title',
                                                  'content',
                                                  'create_date',
                                                  'update_date')
    data_list = list(data)

    end_time = timezone.now()
    duration = (end_time - start_time).total_seconds() * 1000  # 밀리초 단위로 변환

    client_ip = get_client_ip(request)

    log_api.objects.create(
        user_ip=client_ip,
        user_id=get_random_string(30),
        start_time=start_time,
        end_time=end_time,
        call_url=request.path,
        call_url_parameter=num_items
    )

    if num_items <= 100:
        return JsonResponse(data_list, safe=False)  # 데이터를 JSON 형식으로 반환

    else:
        return HttpResponse(f"Successfully fetched {num_items} data items in {duration:.2f} ms", status=200)


def get_data_all(request):

    start_time = timezone.now()  # 처리 시작 시간 기록

    data = board.objects.all().values('seq', 'title', 'content', 'create_date', 'update_date')
    data_list = list(data)  # 데이터 리스트 생성
    num_items = len(data_list)

    end_time = timezone.now()  # 처리 종료 시간 기록
    duration = (end_time - start_time).total_seconds() * 1000  # 처리 시간을 밀리초 단위로 변환

    # 성공 메시지와 함께 처리 시간 반환
    return HttpResponse(f"Successfully fetched {num_items} data in {duration:.2f} ms", status=200)

def log_data(request):

    data = log_api.objects.all().values('seq',
                                        'user_ip',
                                        'user_id',
                                        'start_time',
                                        'end_time',
                                        'call_url',
                                        'call_url_parameter')

    return JsonResponse(list(data), safe=False)


def home(request):
    return HttpResponse("Welcome to my Django app!")
