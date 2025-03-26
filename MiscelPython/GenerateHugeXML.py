'''
Created on Feb 23, 2016

@author: CLPA


'''
with open("c:/temp/out.xml", mode='w') as f:
    f.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<notes>")
    for i in range (1,5000000):
        f.write("<note><to>Tove</to>    <from>Jani</from>    <heading>Reminder</heading>    <body>Don't forget me this weekend!</body></note>")
    f.write("</notes>")
        

    