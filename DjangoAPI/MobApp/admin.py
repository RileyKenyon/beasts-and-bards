from django.contrib import admin
from .models import Race, Character, Stat

# Register your models here.
admin.site.register(Race)
admin.site.register(Character)
admin.site.register(Stat)