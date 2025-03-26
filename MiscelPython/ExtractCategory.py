from os import listdir
from os.path import isfile, join
import argparse;
import re;
import string
parser = argparse.ArgumentParser()
parser.add_argument("filename")
args = parser.parse_args()
filename = args.filename;
i = 0
l = []


onlyfiles = [f for f in listdir("C:/Users/CLPA/Downloads/ServerLogsWithTraceEnabled/split") if isfile(join("C:/Users/CLPA\Downloads/ServerLogsWithTraceEnabled/split", f))]
print (str(onlyfiles))
outFile = open("c:/temp/log0098.txt",mode='w')
for k in onlyfiles: 
    with open("C:/Users/CLPA/Downloads/ServerLogsWithTraceEnabled/split" + '/'  + k) as f:
            for line in f:
                if line.find("ISS.0098") > 0 or line.find("ISS.0153") > 0:
                    if line.find("ISS.0098.51") >= 0 or line.find("ISS.0098.2") >= 0 or line.find("GwwCommonTriggers.logging:processAppMessageTrigger") > 0 \
                    or line.find(" Either Name or Address or Contact Method is required")> 0 or line.find("processTargetServiceMessageTrigger") > 0 \
                    or line.find("emailNotificationTrigger")> 0 or line.find("processSourcingQuote") > 0 or line.find("trigger_processUpdateCspQuoteCoStatusMessage") > 0 \
                    or line.find("processAppErrorTrigger") > 0:
                        
                        continue; 
                    outFile.write(line);
                
            
