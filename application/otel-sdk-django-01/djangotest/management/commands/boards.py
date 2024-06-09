from django.core.management.base import BaseCommand
from django.utils.crypto import get_random_string
from djangotest.models import board
from django.utils import timezone

class Command(BaseCommand):
    help = 'Populates the database with random items'

    def handle(self, *args, **options):

        items = [
            board(
                title=get_random_string(30),
                content=get_random_string(2000),
                create_date=timezone.now(),
                update_date=timezone.now()
            ) for _ in range(1000000)
        ]

        try:
            board.objects.bulk_create(items)
        except Exception as e:
            print("An error occurred:", e)

        self.stdout.write(self.style.SUCCESS('Successfully populated the database.'))
