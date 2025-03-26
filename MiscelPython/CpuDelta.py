import re
## CPU delta script. 
## Author: 
## Miro: Specification
## Chat GPT: Almost good code
## MIro: Fixing up the chat gpt

def extract_values_from_line(line):
    # Define a regular expression pattern to match the CPU value and nid
    pattern = r'cpu=([0-9.]+)ms.*(nid=.+?\s)(.*)'
    
    # Use re.search to find the pattern in the line
    match = re.search(pattern, line)

    # Check if a match is found
    if match:
        cpu_value = float(match.group(1))  # Convert CPU value to float for sorting
        nid_value = match.group(2)
        return cpu_value, nid_value
    else:
        return None, None
def common_keys(dict1, dict2):
    return set(dict1.keys()) & set(dict2.keys())

def main():
    # Specify the file paths
    file_path_1 = r'C:\Users\CLPA\Downloads\SI-572210\pid-1428584_threads_1_7573 (1).txt'
    file_path_2 = r'C:\Users\CLPA\Downloads\SI-572210\pid-1428584_threads_1_7573 (2).txt'
    #file_path_1 = r'C:\Users\CLPA\OneDrive - Software AG\Documents\DemoThreads\CPUThread.txt'
    #file_path_2= r'C:\Users\CLPA\OneDrive - Software AG\Documents\DemoThreads\CPUThread1.txt'

    cpu_values_dict = {}

    cpu_values_dict2 = {}

    try:
        # Read from the first file
        with open(file_path_1, 'r') as file_1:
            for line in file_1:
                cpu_value, nid_value = extract_values_from_line(line)
                if cpu_value is not None:
                    cpu_values_dict[nid_value]=cpu_value;

        # Read from the second file
        with open(file_path_2, 'r') as file_2:
            for line in file_2:
                cpu_value, nid_value = extract_values_from_line(line)
                if cpu_value is not None and nid_value in cpu_values_dict:
                    cpu_values_dict2[nid_value]=cpu_value;
        # Filter common nid values
        cks = common_keys(cpu_values_dict2,cpu_values_dict)
        merged={}
        for k in cks:
            cpu1=cpu_values_dict2[k];
            cpu2=cpu_values_dict[k];
            delta = cpu1 - cpu2;
            merged[k]=delta;
            

        # Calculate CPU value deltas for common nids

        # Sort deltas based on CPU value difference
        sorted_dict = dict(sorted(merged.items(), key=lambda item: item[1],reverse=True))
        
    

        # Print the sorted deltas
        print("Sorted CPU Value Deltas:")
        for k in sorted_dict.keys():
            print(f"NID: {k}, Delta: {sorted_dict[k]}")

    except FileNotFoundError as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    main()
