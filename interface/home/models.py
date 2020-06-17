from __future__ import unicode_literals

from django.db import models
from django.utils import timezone
# from django.contrib.auth.models import User
import datetime
use_choices=[('', '-------'),('train', 'Model Training'), ('test', 'Model Testing'), ('validate','Model Validation')]
model_choices=[('', '-------'),('lambdaMart', 'LambdaMart'),('randomForests', 'Random Forests'),('listNet','ListNet')]
class InputDataset(models.Model):
	rankingMethod = models.CharField(max_length=50,choices=model_choices,default=' ') 
	inputFile = models.FileField(upload_to='uploadedFiles/')
	fileUse = models.CharField(max_length=50,choices=use_choices,default=' ')
	def __str__(self):
		return self.rankingMethod+'-'+self.fileUse+'-'+self.inputFile.name+'-'+str(datetime.datetime.now())