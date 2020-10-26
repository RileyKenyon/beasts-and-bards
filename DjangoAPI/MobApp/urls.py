from django.conf.urls import url
from django.conf.urls.static import static
from django.conf import settings
from MobApp import views

urlpatterns=[
    # race urls
    url(r'^race/$',views.race_collection),
    url(r'^race/([0-9]+)$',views.race_api),
    url(r'^SaveFile$',views.SaveFile),

    # character urls
    url(r'^character/$',views.character_collection),
    url(r'^character/([0-9]+)$',views.character_api),

    # stats urls under character
    url(r'^character/stats/$',views.stat_collection),
    url(r'^character/stats/([0-9]+)$',views.stat_api),
] + static(settings.MEDIA_URL,document_root=settings.MEDIA_ROOT)