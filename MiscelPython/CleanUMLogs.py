'''
Created on Jun 1, 2020

@author: CLPA
'''

import re;
import os;

keyDict = {}
files = os.listdir("C:\\temp\\USGS\\Log09");
outputfile = "C:\\temp\\USGS\\Log09\\\merged05.txt";
out = open(outputfile,'w');

print (files)
for fileEntry in files:
    print ( "reading file:" + str(fileEntry + " " +  str(fileEntry.find("merge"))))
    if fileEntry.find("merge") < 0:
        print ("processing file:" + fileEntry);
        with open("C:/temp/USGS/Log09/" + fileEntry,encoding='utf-8') as f:
            for line in f:
                if line.find("CDT 2020]") > 0  and line.find("has been active for over 60 seconds running ") < 0 and line.find(" UserManager:") < 0:
                    ''' t's a valid line save it
                    '''
                    out.write(line);
                      
                 
                   
out.flush();