from django.http import JsonResponse
from django.utils import timezone
from django.utils.crypto import get_random_string
from .models import log_api, board
from django.http import HttpResponse
from django.shortcuts import render
import grpc

# import demo_pb2
# import demo_pb2_grpc
# def get_recommendations(product_ids):
#     with grpc.insecure_channel('localhost:50051') as channel:
#         stub = demo_pb2_grpc.RecommendationServiceStub(channel)
#         response = stub.ListRecommendations(demo_pb2.ListRecommendationsRequest(product_ids=product_ids))
#         return response

def get_client_ip(request):
    x_forwarded_for = request.META.get('HTTP_X_FORWARDED_FOR')
    if x_forwarded_for:
        ip = x_forwarded_for.split(',')[0]  # X-Forwarded-For 헤더는 콤마로 구분된 IP 목록을 포함할 수 있습니다.
    else:
        ip = request.META.get('REMOTE_ADDR')  # 직접 연결된 클라이언트의 경우 REMOTE_ADDR 사용
    return ip

def get_data(request, num_items):
    start_time = timezone.now()

    data = board.objects.all()[:num_items].values('seq',
                                                  'title',
                                                  'content',
                                                  'create_date',
                                                  'update_date')
    data_list = list(data)
    end_time = timezone.now()

    client_ip = get_client_ip(request)

    log_api.objects.create(
        user_ip=client_ip,
        user_id=get_random_string(30),
        start_time=start_time,
        end_time=end_time,
        call_url=request.path,
        call_url_parameter=num_items
    )
    return JsonResponse(data_list, safe=False)  # 데이터를 JSON 형식으로 반환

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