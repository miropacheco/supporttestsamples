'''
Created on Sep 27, 2017

@author: CLPA
'''
import numpy as np 
import re;
from difflib import SequenceMatcher

if __name__ == '__main__':
    with open("c:/temp/threads.txt") as f:
        myList = list(f)
    print(len(myList))
    indices = [i for i, x in enumerate(myList) if x == " \n"]
    print (indices)
    procList = np.split(myList, indices)
    print(len(procList));
    p = re.compile('pid=.*\s')
    threadDict = {}
    for k in procList:         
        tid = re.findall("(id=.+?\s",k[0])
        name = re.findall("\".+?\"",k[0])
        state = k[1] if len(k) > 1 else "";
        if (tid == []):
            tid = re.findall("tid=.+?\s",k[1])
            name = re.findall("\".+?\"",k[1])
            state = k[2] if len(k) > 2 else ""; 
        tid = tid[0] + name[0] + state;        
        if tid in threadDict:
            threadDict[tid].append(k)
        else:
            threadDict[tid] = [k];
    print("Dict len:" + str(len(threadDict.keys())))
    for k in threadDict.keys():
        if len(threadDict[k]) == 1:
            print ("Not suspicioous:" + k);
        if (len(threadDict[k])==20000):            
            if ''.join(threadDict[k][0]) == ''.join(threadDict[k][1]) :
                print ("Not moved at all 1 occurrence:" + k)
            if len(threadDict[k][0]) > 3  and len(threadDict[k][1]) > 3: 
                if ''.join(threadDict[k][0][2:]) == ''.join(threadDict[k][1][2:]):
                   print ("Moved a little bit:" + k)
            if SequenceMatcher(None,''.join(threadDict[k][0]), ''.join(threadDict[k][1])).ratio() > .8   :
                print("Kind of similar:" + k);        
                    
                                                      
        if (len(threadDict[k])>2):                        
            if ''.join(threadDict[k][0]) == ''.join(threadDict[k][1])  and  ''.join(threadDict[k][1]) == ''.join(threadDict[k][2])  :
                print ("Not moved at all:" + k)
            elif len(threadDict[k][0]) > 3  and len(threadDict[k][1]) > 3 and  len(threadDict[k][2]) > 3 :
                thread1 = ''.join(threadDict[k][0])     
                thread2 = ''.join(threadDict[k][1])
                thread3 = ''.join(threadDict[k][2])
                if thread2[-len(thread2)//2:] == thread3[-len(thread3)//2:]:
                    print("Similar 50%:" + k) 
                else:
                    print ("has moved in" + k)   
            else:
                print("moving a lot:" + k)
                
            #if SequenceMatcher(None,''.join(threadDict[k][0]), ''.join(threadDict[k][1])).ratio() > .8  or SequenceMatcher(None,''.join(threadDict[k][1]), ''.join(threadDict[k][2])).ratio() > .8 or SequenceMatcher(None,''.join(threadDict[k][0]), ''.join(threadDict[k][2])).ratio() > .8 :
            #    print("Kind of similar:" + k);        
            #similarities based
                           
              
                     
    