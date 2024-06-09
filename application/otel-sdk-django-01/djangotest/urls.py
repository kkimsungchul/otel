from django.contrib import admin
from django.urls import path
from .views import get_data, log_data, home

urlpatterns = [
    path('admin/', admin.site.urls),
    path('python/boards/<int:num_items>/', get_data, name='get_data'),
    path('python/log/', log_data, name='log_data'),
    path('', home, name='home'),
]
