import tweepy
import csv
auth = tweepy.OAuthHandler('5QUCmsZc97JKVhSW7UqB4PmGO', 'Szv1qILgFywnl1IbLakJDIqbt44unJOXWEWY7iZ2ksw6WZvDjx')
auth.set_access_token('1159073576495923200-s1eAfxDF86zf9J7brJoe2CEFwU0UW0', 'sxA356gk2uPJFU5iIyoB2yKLUrAbnvZpSGV9cRtqzEWb7')

api = tweepy.API(auth)
query='kohli'
# user = api.get_user('AshishR66701786')
# print user.verified
# print user.listed_count
# print user.friends_count
# # print user.screen_name
# print user.followers_count
# print user.created_at
# for friend in user.friends():
#    print friend.screen_name
class dotdict(dict):
    """dot.notation access to dictionary attributes"""
    __getattr__ = dict.get
    __setattr__ = dict.__setitem__
    __delattr__ = dict.__delitem__

def convertToNumber(x):
    multiplier=1
    if type(x)==type(1):
        return x
    if type(x)==type(True):
        return int(x == True)
    if len(x)==0:
        return 0
    if not x.isdigit():
        if x[len(x)-1]=='K':
            x=x[:len(x)-1]
            multiplier=1000
    x=int(float(x)*multiplier)
    return x

with open('sample_'+query+'.csv', mode='r') as sample:
    sample_reader = csv.reader(sample, delimiter=',')
    cnt =0
    with open(query+'.csv', mode='w') as sample2:
        sample_writer = csv.writer(sample2,delimiter = ',')
        for row in sample_reader:
            if cnt == 0:
                sample_writer.writerow(['Query',"username",'created_at','verified','followers_count','friends_count','listed_count',"tweet","date","Img_present",
                    "likes","comments","retweets","tags","mentions","sum_followers_mention","url_count"])
            else :
                tuser = row[2]
                x = tuser.split("@")
                print x
                try:
                    user = api.get_user(x[len(x)-1])
                except:
                    user ={'created_at':'NA','verified':0,'followers_count':0,'friends_count':0,'listed_count':0}
                    user =dotdict(user)
                mentions = []
                if row[9].find('@')!=-1:
                    mentions = row[9].split(',')
                sum1 = 0
                if len(mentions)>0:
                    for mention in mentions:
                        mention = mention[1:len(mention)]
#                       print mention
                        u = api.get_user(mention)
                        sum1+=u.followers_count
#                         print sum1
                sample_writer.writerow([row[0],row[2],user.created_at,convertToNumber(user.verified),convertToNumber(user.followers_count),convertToNumber(user.friends_count),convertToNumber(user.listed_count),row[3],row[1],convertToNumber(row[4]),convertToNumber(row[5]),convertToNumber(row[6]),convertToNumber(row[7]),row[8],row[9],convertToNumber(sum1),convertToNumber(row[10])])
            cnt+=1
            print cnt


