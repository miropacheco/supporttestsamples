
import argparse;
import re;
import string
parser = argparse.ArgumentParser()
parser.add_argument("filename")
args = parser.parse_args()
filename = args.filename;
i = 0
l = []
with open(filename) as f:
    previousLine = ""
    for line in f:
        print (line)
        if line.find("Count:")!=-1 and previousLine.find(":") != -1 and previousLine !="":            
            line = line.strip()
            s = line.split(":")
            if (s[1] == ''):
                continue
            print(s)
            i = max(int(s[1]),i)
            l.append(int(s[1]))
        previousLine = line    
    print (i)
    print (sorted(l))
            
    