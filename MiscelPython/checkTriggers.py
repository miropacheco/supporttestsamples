'''
Created on Oct 17, 2017

@author: CLPA
'''
import re;
import os;

keyDict = {}
files = os.listdir("C:/Users/CLPA/Downloads/myTest")
print (files)
expectedSet = set([])
fileDict = {}
for fileEntry in files:
    with open("C:/Users/CLPA/Downloads/myTest/" + fileEntry) as f:
        for line in f:
            if line.find("key:") > 0 and line.find("Received Duplicate") == -1:
                
                #GwwCustomerSapCrmToEsb.Contact.triggers:MERGE_M021_M031:wm60723370126(196741)
                m = re.search("(.*?key:\s)(.*?)(\(.*\))",line)  
                group = m.group(2)
                
                if group in keyDict:
                    keyDict[group].append(line);
                    if fileEntry != fileDict[group]:
                        print ("Key:" + group + " present in 2 files");
                        pass
                else:
                    keyDict[group] = [];
                    fileDict[group] = fileEntry;
                    keyDict[group].append(line);
                        
    i = 0;
    unexpected = 0;
    expected = 0;
print (set(fileDict.keys()).difference(set(keyDict.keys())))     
key = "";
for l in keyDict.keys():
    if ''.join(keyDict[l]).find("Ready to invoke service.")==-1 and ''.join(keyDict[l]).find("Processing AND-Join ") >=0 and ''.join(keyDict[l]).find("Processing first") >=0  and  ''.join(keyDict[l]).find("triggerTest:DocA") >=0  and  ''.join(keyDict[l]).find("triggerTest:DocB") >=0:
        print (l)
        unexpected = unexpected + 1
        key = l
        if ''.join(keyDict[l]).find("Multiple threads accessing") == -1 :
            print ("no multiple threads");
    elif ''.join(keyDict[l]).find("Ready to invoke service.")==-1  and (''.join(keyDict[l]).find("Processing AND-Join ") ==-1 or ''.join(keyDict[l]).find("Processing first") ==-1):
        print ("expected failure:" +l)
        expected = expected + 1;
        expectedSet.add(l)      
    else: 
        i = i + 1;
            
print (str(unexpected))    
print (str(expected))
print (str(i))
print (str(len(expectedSet)))
