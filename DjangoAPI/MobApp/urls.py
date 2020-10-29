from django.conf.urls import url
from django.conf.urls.static import static
from django.conf import settings
from MobApp import views

urlpatterns=[
    # race urls
    url(r'^races/$',views.race_collection),
    url(r'^races/([0-9]+)$',views.race_api),
    url(r'^SaveFile$',views.SaveFile),

    # character urls
    url(r'^characters/$',views.character_collection),
    url(r'^characters/([0-9]+)$',views.character_api),

    # stats urls under character
    url(r'^characters/stats/$',views.stat_collection),
    url(r'^characters/([0-9]+)/stats$',views.stat_api),

    #landing for API
    url('',views.index),
] + static(settings.MEDIA_URL,document_root=settings.MEDIA_ROOT)