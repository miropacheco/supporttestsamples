import os;
import re;
from dateutil import parser;

def getListOfFiles(dirName):
    # create a list of file and sub directories 
    # names in the given directory 
    listOfFile = os.listdir(dirName)
    allFiles = list()
    # Iterate over all the entries
    for entry in listOfFile:
        # Create full path
        fullPath = os.path.join(dirName, entry)
        # If entry is a directory then get the list of files in this directory 
        if os.path.isdir(fullPath):
            allFiles = allFiles + getListOfFiles(fullPath)
        else:
            allFiles.append(fullPath)
                
    return allFiles


def mysort(s):
    from dateutil import parser
    date = parser.parse(s[1:34])            
    return date;
def extractStatusCSV(mylist):
    retList=[];

    for s in mylist:
    
        if s.find("Memory=")!=-1:
           match = re.search("(Memory=)(.+?)(,)",s)
           memory=published=scheduled=queued=connections=consumed=directMemory=0;
           time=""
           time=s[0:34];
           if match:
               memory=match.group(2);
               #print (memory);
           match = re.search("(Direct=)(.+?)(,)",s)
           if match:
               directMemory=match.group(2);
               #print (published);
               
           match = re.search("(Published=)(.+?)(,)",s)
           if match:
               published=match.group(2);
               #print (published);
           match = re.search("(Scheduled=)(.+?)(,)",s)
           if match:
               scheduled=match.group(2);
               #print (scheduled);
           match = re.search("(Queued=)(.+?)(,)",s)
           if match:
               queued=match.group(2);
               #print (queued);
           match = re.search("(Connections=)(.+?)(,)",s)
           if match:
               connections=match.group(2);
               #print (connections);
           match = re.search("(Consumed=)(.+?)(,)",s)
           if match:
               consumed=match.group(2);
               #print (connections);
    
           retList.append((str(memory), str(directMemory),str(published),str(scheduled),str(queued),str(connections),str(consumed),time))
    return retList;     

if __name__ == '__main__':
    allLines = []
    #x = getListOfFiles("C:/Users/CLPA/Downloads/LOGS-SI-494181/SoftwareAG_CCCDCAumserver_live (1)")
    x = getListOfFiles("C:/Users/CLPA/Downloads/FlexTronics")
    x= [f for f in x if f.find('nirvan')!=-1]
    for f in x: 
        with open(f,encoding="utf8") as myfile:
            print('working on file:' + f);            
            for line in myfile:
                if (line[0] == '[') and \
                line.find ("ServerStatusLog>") == -1 and \
                (line.find("UserManager: User") == -1):
                #line.find ("SSL Unable to complete client driver setup for connection") == -1 and \
                #line.find ("Driver inactive on adapter ") == -1 and \
                #line.find("UM Server Status Generator")==-1 and \
                #(line.find("UserManager: User") == -1) and \
                #line.find(" MemoryManager: Monitor:")==-1:                     
                #(line.find("jwppdiswmum") > -1 or True) :
                #line.find("RealmMonitor: Monitor state validation completed with status") == -1 and     /
                #line.find("Detected 0 inactive drivers bound to nsps0, will attempt to") == -1 and /
                
                #line.find ("Disconnecting client") ==-1 and /
                #line.find ("DataStreamListener:true Admin:true") ==-1 and/
                    #print(line);
                
                    date = parser.parse(line[1:34])   
                    #if (f.index('Node') != 0 and date.month==1): 
                    #allLines.append(line[:-1] + "-" + f[f.index("Node"): f.index("Node") + 6]);
                    allLines.append(line[:-1] + "-" + f);
                    
    
    allLines.sort(key=lambda x:mysort(x))
    status= extractStatusCSV(allLines);
    output = open("C:/Users/CLPA/Downloads/FlexTronics/mergedStatus.csv", "w");
    output.write("time,memory,directMemory, published,scheduled,queued,connections,consumed\n")
    for line in status:
        output.write(line[7] + "," + line[0] + "," + line[1] + "," + line[2] + "," + line[3] +"," + line[4] +',' +  line[5] +',' +  line[6]+"\n")
        
    output = open("C:/Users/CLPA/Downloads/FlexTronics/merge.log", "w");
    for line in allLines[0:int(len(allLines) / 1)]: 
        output.write(line + "\n");            
    
