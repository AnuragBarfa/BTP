from django.shortcuts import render
import os
import subprocess
# Create your views here.
def home(request):
	cmd='java -jar RankLib-2.13.jar -train MyFile.txt -tvs 0.75 -ranker 6 -metric2t NDCG@100 -metric2T ERR@10 -save mymodel.txt -gmax 9 -norm zscore'
	stream = os.popen(cmd)
	output = stream.read()
	print(output)

	# process = subprocess.Popen(['ping', '-c 4', 'python.org'], 
 #                           stdout=subprocess.PIPE,
 #                           universal_newlines=True)

	# while True:
	#     output = process.stdout.readline()
	#     print(output.strip())
	#     # Do something else
	#     return_code = process.poll()
	#     if return_code is not None:
	#         print('RETURN CODE', return_code)
	#         # Process has finished, read rest of the output 
	#         for output in process.stdout.readlines():
	#             print(output.strip())
	#         break
	return render(request,'home.html',{'output':output})
