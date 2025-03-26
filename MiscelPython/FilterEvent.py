'''
Created on Nov 17, 2017

@author: CLPA
'''
import os
files = os.listdir("C:/Users/CLPA/Downloads/xpLogs")
print (files)
expectedSet = set([])
outF = open('c:/tmp/out1.txt', 'w')
fileDict = {}
for fileEntry in files:
    with open("C:/Users/CLPA/Downloads/xpLogs/" + fileEntry) as f:
        for line in f:
            if line.find("com.apama.marketdata.Depth(\"WINZ17\"") !=-1:
                outF.write(line);
            
        
        