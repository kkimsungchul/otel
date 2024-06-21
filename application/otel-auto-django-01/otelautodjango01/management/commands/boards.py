from django.core.management.base import BaseCommand
from django.utils.crypto import get_random_string
from otelautodjango01.models import board
from django.utils import timezone

class Command(BaseCommand):
    help = 'Populates the database with random items'

    def handle(self, *args, **options):
        if board.objects.exists():
            self.stdout.write(self.style.WARNING('Database already populated. No new data inserted.'))
            return

        print("## Database data insert start")
        items = [
            board(
                title=get_random_string(30),
                content=get_random_string(2000),
                create_date=timezone.now(),
                update_date=timezone.now()
            ) for _ in range(100000)
        ]
        print("## Database data insert end")
        try:
            board.objects.bulk_create(items)
        except Exception as e:
            print("An error occurred:", e)

        self.stdout.write(self.style.SUCCESS('Successfully populated the database.'))
