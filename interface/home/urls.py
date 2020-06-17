from django.conf.urls import url
from django.urls import path
from . import views
app_name = 'home'
urlpatterns=[
    url(r'^$',views.home,name='home'),
    url(r'^train/(?P<file_name>[\w ]+)/(?P<ranking_method>[\w ]+)/$',views.train,name='train'),
    url(r'^test/(?P<file_name>[\w ]+)/(?P<ranking_method>[\w ]+)/$',views.test,name='test'),
    url(r'^validate/(?P<file_name>[\w ]+)/(?P<ranking_method>[\w ]+)/$',views.validate,name='validate'),
    url(r'^download/(?P<file_name>[\w ]+)/$',views.download_file,name='download_file')
]