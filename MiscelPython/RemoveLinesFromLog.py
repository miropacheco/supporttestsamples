def remove_lines_with_substrings(input_filename, output_filename, substrings):
    with open(input_filename, 'r') as input_file, open(output_filename, 'w') as output_file:
        for line in input_file:
            if not any(substring in line for substring in substrings):
                output_file.write(line)

input_filename = r'c:\users\clpa\downloads\server.log.20240419'

# Replace 'output_filename.txt' with the path to your output file
output_filename = r'c:\users\clpa\downloads\out.log'

# Substrings to search for and remove
substrings = ["Received UM message", "Successfully sent", "Successfully processed","The SESSIONNAME field in a WMSESSION","ISP.0090.00"]

remove_lines_with_substrings(input_filename, output_filename, substrings)

print("Lines not containing the specified substrings have been written to '{}'.".format(output_filename))



