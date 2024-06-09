from django.http import JsonResponse
from django.utils import timezone
from django.utils.crypto import get_random_string
from .models import log_api, board
from django.http import HttpResponse


def get_data(request, num_items):
    start_time = timezone.now()
    print(start_time)

    data = board.objects.all()[:num_items].values('seq',
                                                  'title',
                                                  'content',
                                                  'create_date',
                                                  'update_date')
    data_list = list(data)
    end_time = timezone.now()
    print(end_time)

    log_api.objects.create(
        user_ip=request.META.get('REMOTE_ADDR', ''),
        user_id=get_random_string(30),
        start_time=start_time,
        end_time=timezone.now(),
        call_url=request.path,
        call_url_parameter=num_items
    )
    return JsonResponse(data_list, safe=False)  # 데이터를 JSON 형식으로 반환

def log_data(request):
    data = log_api.objects.all().values('seq',
                                        'user_ip',
                                        'start_time',
                                        'end_time',
                                        'call_url',
                                        'call_url_parameter')

    return JsonResponse(list(data), safe=False)

def home(request):
    return HttpResponse("Welcome to my Django app!")