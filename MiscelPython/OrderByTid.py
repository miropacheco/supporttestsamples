import re

def extract_tid(line):
    match = re.search(r'\(tid=(\d+)\)', line)
    if match:
        return int(match.group(1))
    return None

def reorder_log_file(input_file, output_file):
    tid_lines = {}
    
    # Read lines from input file and categorize them based on presence of 'tid'
    with open(input_file, 'r') as file:
        for line in file:
            tid = extract_tid(line)
            if tid is not None:
                if tid not in tid_lines:
                    tid_lines[tid] = []
                tid_lines[tid].append(line)
    
    # Write tid lines to output file ordered by tid
    with open(output_file, 'w') as file:
        for tid in sorted(tid_lines.keys()):
            for line in tid_lines[tid]:
                file.write(line)

def main():
    input_file = r"c:\temp\matching_lines.txt"    # Replace with your input file path
    output_file = r"c:\temp\orderedtids.txt"  # Replace with desired output file path
    reorder_log_file(input_file, output_file)
    print("Reordering complete.")

if __name__ == "__main__":
    main()
