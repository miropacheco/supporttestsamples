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
    date = parser.parse(s[1:29])            
    return date;
    

if __name__ == '__main__':
    allLines = []
    x = getListOfFiles("C:/Users/CLPA/Downloads/umlogs/btbgpu101")
    x= [f for f in x if f.find('server')!=-1]
    for f in x: 
        with open(f,encoding="utf8") as myfile:
            print('working on file:' + f);            
            for line in myfile:
                
                if (line.find ("0038.") != -1 or 
                line.find (".0039.") != -1 or 
                line.find (".0040.") != -1 or 
                line.find (".0041.") != -1 or                                                     
                line.find (".0012.") != -1 or \
                line.find (".0033.") != -1):
                #(line.find("jwppdiswmum") > -1 or True) :
                #line.find("RealmMonitor: Monitor state validation completed with status") == -1 and     /
                #line.find("Detected 0 inactive drivers bound to nsps0, will attempt to") == -1 and /
                
                #line.find ("Disconnecting client") ==-1 and /
                #line.find ("DataStreamListener:true Admin:true") ==-1 and/
                
                    #date = parser.parse(line[1:29])   
                    #if (f.index('Node') != 0 and date.month==1): 
                    #allLines.append(line[:-1] + "-" + f[f.index("Node"): f.index("Node") + 6]);
                    allLines.append(line[:-1]);
    
    #allLines.sort(key=lambda x:mysort(x))
    output = open("C:/Users/CLPA/Downloads/umlogs/btbgpu101/merge.log", "w");
    for line in allLines[0:int(len(allLines) / 1)]: 
        output.write(line + "\n");            
    
