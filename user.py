import tweepy
auth = tweepy.OAuthHandler('5QUCmsZc97JKVhSW7UqB4PmGO', 'Szv1qILgFywnl1IbLakJDIqbt44unJOXWEWY7iZ2ksw6WZvDjx')
auth.set_access_token('1159073576495923200-s1eAfxDF86zf9J7brJoe2CEFwU0UW0', 'sxA356gk2uPJFU5iIyoB2yKLUrAbnvZpSGV9cRtqzEWb7')

api = tweepy.API(auth)

# user = api.get_user('AshishR66701786')
# print user.verified
# print user.listed_count
# print user.friends_count
# # print user.screen_name
# print user.followers_count
# print user.created_at
# for friend in user.friends():
#    print friend.screen_name


import csv
with open('sample_pakistan.csv', mode='r') as sample:
    sample_reader = csv.reader(sample, delimiter=',')
    cnt =0
    with open('pakistan.csv', mode='w') as sample2:
	    sample_writer = csv.writer(sample2,delimiter = ',')
	    for row in sample_reader:
	    	if cnt == 0:
	    		sample_writer.writerow(['Query',"username",'created_at','verified','followers_count','friends_count','listed_count',"tweet","date","Img_present",
	    			"likes","comments","retweets","tags","mentions","sum_followers_mention","url_count"])
	    	else :
	    		tuser = row[2]
	    		x = tuser.split("@")
    			user = api.get_user(x[len(x)-1])
    			mentions = []
    			if row[9].find('@')!=-1:
    				mentions = row[9].split(',')
    			sum = 0
    			if len(mentions)>0:
	    			for mention in mentions:
	    				mention = mention[1:len(mention)]
	    				print(mention)
	    				u = api.get_user(mention)
	    				sum+=u.followers_count
    			sample_writer.writerow([row[0],row[2],user.created_at,user.verified,user.followers_count,user.friends_count,user.listed_count,
    				row[3],row[1],row[4],row[5],row[6],row[7],row[8],row[9],sum,row[10]])
	    	cnt+=1
	    	print cnt


