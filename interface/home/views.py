from django.shortcuts import render, get_object_or_404, redirect
import os
import subprocess
from .forms import FileUploadForm
import numpy as np
from django.core.files import File
import mimetypes
from django.views.static import serve 
from django.http import HttpResponse
# Create your views here.
modelToCodeMapper={'lambdaMart':6, 'randomForests':8, 'listNet':7}
increment={6:[11,7,3,4], 8:[11,8,3,4], 7:[11,3,3,4]}
trainDataIndex={6:[1,2], 8:[1,2], 7:[2,3]}
def ndcg(y_true, y_score, k=1000):
	y_true_sorted = sorted(y_true, reverse=True)
	ideal_dcg = 0
	for i in range(min(k,len(y_score))):
		ideal_dcg += (2 ** y_true_sorted[i] - 1.) / np.log2(i + 2)
	dcg = 0
	argsort_indices = np.argsort(y_score)[::-1]
	for i in range(min(k,len(y_score))):
		dcg += (2 ** y_true[argsort_indices[i]] - 1.) / np.log2(i + 2)
	ndcg = dcg / ideal_dcg
	return ndcg

def home(request):
	if request.method == "POST":
		upload_form = FileUploadForm(request.POST, request.FILES)
		if upload_form.is_valid():
			#use cleaned_data before saving object
			model=upload_form.cleaned_data['rankingMethod']
			file=upload_form.cleaned_data['inputFile']
			use=upload_form.cleaned_data['fileUse']
			fileName=file.name
			try:
				os.remove("uploadedFiles/"+fileName)
			except:
				pass
			upload_form = upload_form.save(commit=False)
			upload_form.save()
			if use=='train':
				return redirect('home:train',str(fileName[:len(fileName)-4]),str(model))
			elif use=='test':
				return redirect('home:test',str(fileName[:len(fileName)-4]),str(model))
			elif use=='validate':
				return redirect('home:validate',str(fileName[:len(fileName)-4]),str(model))
			else:
				print('unknown use')
	else:
		upload_form = FileUploadForm()

	return render(request,'home.html', {'form': upload_form})
def train(request,file_name,ranking_method):
	modelCode=modelToCodeMapper[ranking_method]
	cmd=["java","-jar","RankLib-2.13.jar","-train","uploadedFiles/"+file_name+".txt","-tvs","0.50","-ranker",str(modelCode),"-metric2t","NDCG@100","-metric2T","ERR@10","-save","trainedModels/"+ranking_method+".txt","-gmax","9","-norm","zscore"]
	if ranking_method=='listNet':
		cmd.append("-epoch")
		cmd.append("6000")
	output=[]
	proc = subprocess.Popen(cmd, stdout=subprocess.PIPE)
	for line in proc.stdout.readlines():
		x=str(line)
		x=x[2:len(x)-3]
		x=x.replace('\\t',' ')
		# print(x)
		output.append(x)
	index=1
	
	argumentsInfo=output[index:index+increment[modelCode][0]]
	index=index+increment[modelCode][0]+1

	modelParamInfo=output[index:index+increment[modelCode][1]]#[13:21]
	index=index+increment[modelCode][1]+1
	
	inputInfo=output[index:index+increment[modelCode][2]]
	index=index+increment[modelCode][2]+1+4
	
	trainingInfo=[]
	temp=0
	for epochInfo in output[index:]:
		temp=temp+1
		if epochInfo[:5]=='-----':
			break
		else:
			x=epochInfo.split("|")
			for tr in range(1,len(x)):
				if x[tr]==' ':
					x[tr]='0'
			
			trainingInfo.append([int(temp),float(x[trainDataIndex[modelCode][0]]),float(x[trainDataIndex[modelCode][1]])]) 
	index=index+temp

	trainingResultInfo=output[index:index+increment[modelCode][3]]
	return render(request,'train.html',{'ranking_method':ranking_method,'arg':argumentsInfo,'model':modelParamInfo,'input':inputInfo,'train':trainingInfo,'trainRes':trainingResultInfo})

def test(request,file_name,ranking_method):
	score_data,label_data,query,output=getScoreAndLabels(file_name,ranking_method)
	inputInfo=output[6]
	inputInfo=inputInfo[1:len(inputInfo)-1]
	modelInfo=output[4]
	file1 = open("RankedTweets.txt","w")
	for qid in score_data.keys():
	    for doc in score_data[qid]:
	        s=str(qid)+" "+str(doc[2])+"_of_"+query[qid]+"\n"
	        file1.write(s)
	file1.close()


	# some_file  = open('RankedTweets.txt', "r")
	# django_file = File(some_file)
	temp=[[1,2,3],[4,5,6],[7,8,9]]
	return render(request,'test.html',{'ranking_method':ranking_method,'input':inputInfo,'model':modelInfo})

def validate(request,file_name,ranking_method):
	score_data,label_data,query,output=getScoreAndLabels(file_name,ranking_method)
	inputInfo=output[6]
	inputInfo=inputInfo[1:len(inputInfo)-1]
	modelInfo=output[4]
	file1 = open("RankedTweets.txt","w")
	for qid in score_data.keys():
		for doc in score_data[qid]:
			s=str(qid)+" "+str(doc[2])+"_of_"+query[qid]+"\n"
			file1.write(s)
	file1.close()
	tot_ndcg = 0
	for id in score_data:
		y_score = []
		y_true = []
		for doc in score_data[id]:
			y_score.append(doc[0])
			y_true.append(label_data[id][doc[2]])
		print(ndcg(y_true,y_score))
		tot_ndcg+=ndcg(y_true,y_score)
	tot_ndcg/=len(label_data)
	print(tot_ndcg)
	return render(request,'validate.html',{'ndcg':tot_ndcg,'input':inputInfo,'model':modelInfo})


def getScoreAndLabels(file_name,ranking_method):
	cmd=["java","-jar","RankLib-2.13.jar","-rank","uploadedFiles/"+file_name+".txt","-load","trainedModels/"+ranking_method+".txt","-norm","zscore","-indri","myNewRankedLists.txt"]
	output=[]
	proc = subprocess.Popen(cmd, stdout=subprocess.PIPE)
	for line in proc.stdout.readlines():
		x=str(line)
		x=x[2:len(x)-3]
		x=x.replace('\\t',' ')
		# print(x)
		output.append(x)
	score_data={}
	label_data = {}
	query={}
	file1 = open("uploadedFiles/"+file_name+".txt","r")
	for x in file1:
		curr_row=x.split(" ")
		qid=int(curr_row[1].split(":")[1])
		date=float(curr_row[14].split(":")[1])
		index2=int(curr_row[26].split("_")[0])-1
		if qid not in score_data:
			label_data[qid] = {}
			score_data[qid] = []
			query[qid]=curr_row[26].split("_")[2]
		label_data[qid][index2+1] = int(curr_row[0])
		score_data[qid].append([0,date,index2+1])
	file1.close()
	f = open("myNewRankedLists.txt","r")
	f1 = f.readlines()
	ans = {}
	for x in f1:
		y = x.split()
		value = y[len(y)-2]
		q_id = y[0]
		z = x.split('_')
		i = z[0]
		i = i.split()
		# print(i)
		i = i[4]
		for v in range(len(score_data[int(q_id)])):
			# print(score_data[int(q_id)][v][2])
			# print(i)
			if score_data[int(q_id)][v][2] == int(i):
				score_data[int(q_id)][v][0] = float(value)
				break
	for id in score_data.keys():
		score_data[id].sort(key = lambda sub: (-sub[0], sub[1]))

	return score_data,label_data,query,output


def download_file(request,file_name):
	# fill these variables with real values
	print("before")
	print(os.listdir('.'))
	print("afte")
	file_name=file_name+".txt"
	if file_name!="RankedTweets.txt":
		fl_path = "trainedModels/"+file_name
	else:
		fl_path = file_name
	fl = open(fl_path, 'r')

	mime_type, _ = mimetypes.guess_type(fl_path)
	response = HttpResponse(fl, content_type=mime_type)
	response['Content-Disposition'] = "attachment; filename=%s" % file_name
	return response

	# filepath = file_name+'.txt' 
	# # print(pwd)
	# return serve(request, os.path.basename(filepath),os.path.dirname(filepath))

