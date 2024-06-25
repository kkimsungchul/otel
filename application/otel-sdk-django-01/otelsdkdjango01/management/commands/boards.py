from django.core.management.base import BaseCommand
from django.utils.crypto import get_random_string
from otelsdkdjango01.models import board, Session
from django.utils import timezone

class Command(BaseCommand):
    help = 'Populates the database with random items'

    def handle(self, *args, **options):

        session = Session()

        if session.query(board).first() is not None:
            self.stdout.write(self.style.WARNING('Database already populated. No new data inserted.'))
            session.close()
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
            session.bulk_save_objects(items)
            session.commit()
        except Exception as e:
            session.rollback()
            print("An error occurred:", e)
        finally:
            session.close()

        self.stdout.write(self.style.SUCCESS('Successfully populated the database.'))
