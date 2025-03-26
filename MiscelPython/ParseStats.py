
import argparse;
import re;
'''
TotalMem: The total amount of memory available to the Java virtual machine 
FreeMem: The amount of unused memory that is currently available to the Java virtual machine. 
CurT: The number of system threads currently active. 
MaxT: The highest number of system threads that have ever been active concurrently on the server. 
SSNs: The number of sessions currently active 
SSNx: The highest number of sessions that were active concurrently on the server 
SSNAvg: The average length of a session lifetime. 
REQs: The number of service requests currently executing 
REQx: The maximum number of service requests concurrently executing during the last poll cycle (1 minute) 
REQAvg: The average execution time length for the service requests during the last poll cycle (1 minute) 
StartReq: The number of service requests that started during the last poll cycle (1 minute) 
EndReq: The number of service requests that terminated during the last poll cycle (1 minute).
---------------------
''' 
parser = argparse.ArgumentParser()
parser.add_argument("filename")
args = parser.parse_args()
filename = args.filename;
outputfile = "c:\\temp\\myoutput.csv";
out = open(outputfile,'w');
out.writelines(['date,time,timeZone,TotalMem,FreeMem,CurT,MaxT,SSNs,SSNx,SSNAvg,REQs,REQx,REQAvg,StartReq,EndReq']);
out.flush;
lines = list();
with open(filename) as f:
    for line in f:
        finalString="";
        if line == "\n":            
            continue;
        if line.find("0000000") > 0:
            res = re.split("\s+",line);
            for s in res:
                try:
                    if res.index(s) == 3 or res.index(s) == 4: 
                        finalString = finalString +"," + str(int(s,16)/(pow(1024,2)));
                    else: 
                        finalString = finalString +"," + str(int(s,16));
                except ValueError:
                    finalString = finalString +"," + s;
                
            
        finalString = finalString[1:] + "\n";
        lines.append(finalString);
out.writelines(lines);
out.flush()            
out.close();
f.close();            
            
            
        
             