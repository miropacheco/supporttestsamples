'''
Created on Sep 27, 2017

@author: CLPA
'''
import numpy as np 
import re;
outputfile = "c:\\temp\\output2.txt";
out = open(outputfile,'w');
from difflib import SequenceMatcher

if __name__ == '__main__':
    with open("c:\\temp\\output1.txt") as f:
        myList = list(f)
    print(len(myList))
    indices = [i for i, x in enumerate(myList) if x == "\n"]
    indices = np.array(indices) + 1;
    print (indices)
    procList = np.split(myList, indices)
    #print (procList)
    for x in procList:
        if ''.join (x).find("AS400 Read Daemon")==-1 and ''.join (x).find("Client-Push")==-1 and ''.join (x).find("Client-Push")==-1:
            out.write (''.join(x))
          
                     
    