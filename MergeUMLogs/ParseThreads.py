import re

def parse_thread_dump(file_path):
    thread_pattern = ".*Service Thread Pool.*"
    parsing_thread = False

    with open(file_path, 'r') as file:
        for line in file:                    
            if re.match(thread_pattern, line):
                parsing_thread = True                
                print(line.strip())  # Print the thread line
            elif parsing_thread:
                if line.strip() == "":
                    parsing_thread = False
                else:
                    print(line.strip())  # Print the thread content

# Replace 'file_path.txt' with the path to your thread dump file
parse_thread_dump('c:/tmp/threads.txt')