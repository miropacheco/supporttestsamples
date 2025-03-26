from datetime import datetime
import re
def parse_log_file(file_path):
    with open(file_path, 'r') as file:        
        lines = file.readlines()
    #lines = [l for l in lines if ("FRP001_TRGBUF" in l or "Begin local t" in l or "Commit"in l or "notification callback" in l)];    
    deltas = []
    for i in range(1, len(lines)):
        timestamp_format = "%Y-%m-%d %H:%M:%S"
        current_line_timestamp = datetime.strptime(lines[i].split()[0] + ' ' + lines[i].split()[1], timestamp_format)
        previous_line_timestamp = datetime.strptime(lines[i-1].split()[0] + ' ' + lines[i-1].split()[1], timestamp_format)
        delta = current_line_timestamp - previous_line_timestamp
        if (delta.total_seconds()!=0):
            deltas.append((delta.total_seconds(), i+1))
    deltas.sort(reverse=True)
    with open(r"c:\temp\outfile.txt", "w") as outfile:
        outfile.write("".join(lines))
    return deltas

def extract_thread_ids(filename):
    with open(filename, 'r') as file:
        thread_ids = set()
        matching_lines = []
        for line in file:
            match = re.search(r'.*tid=([0-9]+).*ESBTRIG/FRP001_TRGBUF.*', line)
            if match:
                thread_ids.add(match.group(1))
    with open(filename, 'r') as file:            
        for line in file:
            match = re.search(r'(.*tid=)([0-9]+)(.*)',line)
            if match:
                tid = match.group(2)
                if (tid in thread_ids):
                    matching_lines.append(line);
                     
                
            
    with open(r'c:\temp\matching_lines.txt', 'w') as outfile:
        outfile.write('\n'.join(matching_lines))
    
    return list(thread_ids)


def main1():
    log_file_path = r"C:\temp\orderedtids.txt"  # Replace this with the path to your log file
    deltas = parse_log_file(log_file_path)
    
    print("Time differences from highest to lowest along with their corresponding line numbers:")
    for delta, line_number in deltas:
        print(f"{delta} seconds - Line {line_number}")

def main():
    filename = "c:/temp/server.log"
    thread_ids = extract_thread_ids(filename)
    print(thread_ids)

if __name__ == "__main__":
    main1()
