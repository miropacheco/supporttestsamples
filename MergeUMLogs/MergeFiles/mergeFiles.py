'''
Created on Apr 16, 2021

@author: CLPA
'''
import re
if __name__ == '__main__':
    str = "[Thu Apr 01 05:22:56.135 UTC 2021] [CommonPool:0]   Copyright (c) Software AG Limited. All rights reserved";
    expr = "\[.*?\]\s\[.*?\]";
    print(re.search(expr,str))
    
