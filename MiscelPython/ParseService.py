'''
Created on Jul 27, 2023

@author: CLPA
'''
lines = [];
with open("c:\\tmp\\service3.txt","r") as f:
    for line in f:
        lines.append(line)
indexes = [-1] + [i for i, x in enumerate(lines) if x == '\n']
lines = [lines [indexes[i]+1:indexes[i+1]] for i in range(len(indexes)-1)]
services={};
for line in lines[1:]:    
    service = line[0][:-1]
    running= line[2][12:-1];
    
    #print(service, count);
    if (running != 'false'):
        services[service]=running;
lines.clear()    
with open("c:\\tmp\\service4.log","r") as f:
    for line in f:
        lines.append(line)

indexes = [-1] + [i for i, x in enumerate(lines) if x == '\n']
lines = [lines [indexes[i]+1:indexes[i+1]] for i in range(len(indexes)-1)]
commonServices= {}
services4only=services
services3only={}
for line in lines[1:]:    
    service = line[0][:-1]
    count= line[1][9:-1];
    #print(service, count);
    if service in services.keys():
        commonServices[service]=[services[service],count]
        services4only.pop(service);
    else:
        services3only[service]=count;
print (len(commonServices))
print ("run on both")       
for s in commonServices.keys():
    print(s, commonServices[s]);        
print ("run on 3 only") 
for s in services3only.keys():
    print(s, services3only[s]);
            
print ("run on 4 only")
for s in services4only.keys():
    print(s, services4only[s]);
lines.clear()    
with open("c:\\tmp\\triggers.log","r") as f:
    for line in f:        
        lines.append(line[:-1].upper())
print ("Tringges");        
for s in commonServices.keys():
    if (s.upper() in lines):
        print(s, ','+ commonServices[s][0]+',',commonServices[s][1]);        
    