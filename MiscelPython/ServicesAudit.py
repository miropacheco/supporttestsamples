'''
Created on Sep 27, 2017

@author: CLPA
'''
import numpy as np 
import re;
from difflib import SequenceMatcher
from datetime import datetime;
#(^(.*?\s)(.*?\s)(.*Z\s)(.*?\d+?\s)(.*?)(Ended|Started)(.*)
#$1 $2 $3 $5 $6
if __name__ == '__main__':
    with open("c:/temp/serviceAudit.txt") as f:
        myList = list(f)
    serviceDic = {}    
    for k in myList:
        serviceEntry = k.split()
        #if (k.find("wm.estd.rosettaNet.utils:writeI18NLog") !=-1) :
            #print ("skip");
         #   continue;
        #print (serviceEntry) 
               
        if (serviceEntry[0] + serviceEntry[1] + serviceEntry[7]) in serviceDic and serviceEntry[8] == "Ended":
            
            startDate = datetime.strptime(serviceEntry[6],"%H:%M:%S.%fZ")
            aux = serviceDic[serviceEntry[0] + serviceEntry[1] + serviceEntry[7]]
            #print (aux.split()[6])
            finishDate = datetime.strptime(aux.split()[6], "%H:%M:%S.%fZ")
            print (aux.split()[7],startDate - finishDate)
            
            del serviceDic[serviceEntry[0] + serviceEntry[1] + serviceEntry[7] ]
            
        else: 
            serviceDic[serviceEntry[0] + serviceEntry[1] + serviceEntry[7]] = k;
    for k in serviceDic.values():
        print (k);               
        
        