import re;
from collections import Counter
from dateutil.parser import *
procs = {}
failList=[]
nwt = 0
iMax=0
totalProc= 0
avgDeltaSuccess = 0
avgDeltaFail = 0
totalFail = 0
totalSuccess = 0
maxExecTimeSuccess=0
maxExecTimeFailure=0
totalSuccessOver30=0
totalFailOver30=0
l = [];
with open("C:/Users/clpa/Downloads/server (27).log",encoding="utf8") as f, open("C:/Users/CLPA/Downloads/out.txt",mode="w",encoding="utf8") as out:
    for line in f:        
        if (line.find("<soap:Envelope xmlns")!=-1) or (line.find("Access denied for user wswdm ")!=-1) :
            if line.find("<n:")!=-1:
                subLine = line[line.find("<n:"):]
                subLine= subLine[:subLine.find(">")]
                print (subLine);
                l.append(subLine)
                
                
            #print ("Found it");
            out.write(line);
             
    d = {x:l.count(x) for x in l}          
    print (d)  
            