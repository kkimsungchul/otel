from django.contrib import admin
from django.urls import path
from .views import get_data, get_data_all, log_data, home, get_user

urlpatterns = [
    path('board/', get_data_all, name='get_data_all'),
    path('board/<int:num_items>/', get_data, name='get_data'),
    path('log/', log_data, name='log_data'),
    path('user/', get_user, name='get_user'),
    path('', home, name='home'),
]