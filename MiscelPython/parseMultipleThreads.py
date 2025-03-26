import os
from sqlalchemy.sql.expression import except_
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


if __name__ == '__main__':
    
    files = getListOfFiles("C:/Users/CLPA/Downloads/threadsjj")
    fileIndex=0;    
    fileThreads={};
    for f in files: 
        with open(f,encoding="utf8") as myfile:
            print('working on file:' + f);
            nid="";            
            threads = {}
            for line in myfile:
                if line[0]=='"' and "nid=" in line:
                    nid=  line[line.index("nid=") +4:line.index("nid=") +12];
                    threads[nid]=[];                                   
                else:
                    if nid!="":
                        threads[nid].append(line)    
            for key in threads:
                print("joining key" + key);                
                threads[key] = "".join(threads[key])                
                if ("flow" in threads[key] or "HTTPa" in threads[key] or \
                    "riggera" in threads[key] or "jdbca" in threads[key] or "Service Thread Poola" in \
                    threads[key] or "ReentrantLocka" in threads[key]):
                    if not key in fileThreads:
                        fileThreads[key]=[];
                    fileThreads[key].append(threads[key])
                         
                
        
        fileIndex = fileIndex + 1;             
    print('done');                   