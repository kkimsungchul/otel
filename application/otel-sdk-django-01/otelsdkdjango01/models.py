from django.db import models

# Create your models here.

class log_api(models.Model):
    seq = models.AutoField(primary_key=True)
    user_ip = models.CharField(max_length=15)
    user_id = models.CharField(max_length=30)
    start_time = models.DateTimeField()
    end_time = models.DateTimeField()
    call_url = models.CharField(max_length=100)
    call_url_parameter = models.CharField(max_length=300)

class board(models.Model):
    seq = models.AutoField(primary_key=True)
    title = models.CharField(max_length=30)
    content = models.CharField(max_length=2000)
    create_date = models.DateTimeField()
    update_date = models.DateTimeField()