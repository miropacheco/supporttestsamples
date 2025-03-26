'''
Created on Sep 27, 2017

@author: CLPA
'''
import numpy as np 
import re;
from difflib import SequenceMatcher

if __name__ == '__main__':
    with open(r"C:\Users\CLPA\Downloads\logs-service-usage\ServiceUsage-working-class-instance.txt") as f:
        myList = list(f)
    print(len(myList))
    indices = [i for i, x in enumerate(myList) if x == "\n"]
    services = np.split(myList, indices)

    for k in services[1:]:
        if (len(k) > 2):
            print(k[1][:-1] + ',' +  k[2].split(':')[1][:-1] + ',' + k[3].split(':')[1][:-1] + ',' + ''.join(k[4].split(':')[1:])[:-1])         
                     
    