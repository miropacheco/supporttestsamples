
import argparse;
import re;
import sys
from future.backports.test.support import _original_stdout

parser = argparse.ArgumentParser()
#prog = re.compile("(.*prio.*)(.*tid=)(.*?)(\s.*runnable.*)")
#prog = re.compile("(.*prio.*)(.*nid=)")
#prog = re.compile("(.*tid=)(0x0000000016475000|0x00002b3cccba6000)(\s)")
prog = re.compile("(.*Id=.*?in )")
# "Thread-217643" daemon prio=10 tid=0x000000001746e000 nid=0x4221 waiting on condition [0x00002b3cec128000]

parser.add_argument("filename")
args = parser.parse_args()
filename = args.filename;
myDictionary = dict();
repeated = dict();
tid = "";

total = 0
locked = list()
waiting = list()
activeList = list();
with open(filename) as f:
    for line in f:
        if (prog.match(line)):
            # print line            
            #tid = re.search("(.*id=)(.*?)(\s)", line).group(2);
            try:
                #tid = re.search("(.*nid=)(.*?)(\s)", line).group(2);            
                tid = re.search("(.*Id=)(.*?)(\sin)", line).group(2);
            except:
                print("broken line" + line)
                
            total += 1;
            if not (tid in myDictionary.keys()):
                activeList = list();
                activeList.append(line)
                myDictionary[tid] = list();
                myDictionary[tid].append(activeList);
                repeated[tid] = 1;
            else:
                repeated[tid] += 1;
                myDictionary[tid].append(list());
                activeList = myDictionary[tid][-1]
                # print "adding to list:"  + tid + str(activeList);
        elif (line != "" and tid != ""):
            activeList.append(line.replace("jvm 2    | ","").strip());
            if line.find("locked <") > 0 :
                ''' it"s a locked '''
                locked.append(re.search("(.*<)(.*)(>.*)",line).group(2));
            if line.find("waiting on <") > 0:
                ''' it"s a locked '''
                waiting.append(re.search("(.*<)(.*)(>.*)",line).group(2));
                
        elif(line == ""):
            tid = "";
JDBCThreads= list()
original_stdout = sys.stdout
with open('c:/tmp/out.txt', 'w') as f:
    sys.stdout = f 
    for k in myDictionary:
        fullThread = "\n".join(myDictionary[k][0]);
        if (fullThread.find("HTTP")!=-1 and fullThread.find("soap")!=-1):        
            print(fullThread)
            JDBCThreads.append(fullThread)
    print (len(JDBCThreads))
     
sys.stdout= _original_stdout
equalStacks = dict();  
maxRepeat = max(repeated.values())
print (locked)
print(waiting);
print ("Locked and waiting:" + str(set(locked).intersection(set(waiting))));

          
for k in repeated.keys():
    if (repeated[k] > 1) :
        stackSize = len(myDictionary[k][0]);
        
        for l in  myDictionary[k]:
            # if (stackSize !=len(l)):
            # print ("Divergent sizes:" + k  + str(myDictionary[k]))
            #   a = 0;
            # else:
            if (len(set(l).difference(set(myDictionary[k][0]))) == 0):                                        
                    # print("Equal stacks:"  + k + str(myDictionary[k]))
                    if (k in equalStacks.keys()):
                        equalStacks[k] = equalStacks[k] + 1;
                    else:
                        equalStacks[k] = 1;

                        
s = list()
for k in equalStacks.keys():
    if (equalStacks[k] == repeated[k] and repeated[k] == maxRepeat):
        #if (str(myDictionary[k]).find("waiting") == -1 and str(myDictionary[k]).find("FilePolling")==-1 and str(myDictionary[k]).find("accept")==-1 and str(myDictionary[k]).find("sleep")==-1):
        print("Equal stacks:" + k + str(myDictionary[k]))
        stack0 = "".join(myDictionary[k][0])
        s.append(stack0)
                            
print (set(s))