import os
import re
from collections import defaultdict

def analyze_stack_traces(directory, filter_line):
    # Regex pattern to match thread information and stack traces
    thread_pattern = re.compile(r'"(.*?)" .*tid=0x([0-9a-fA-F]+)')
    stack_trace_pattern = re.compile(r'\s+at .+')

    # Dictionary to store thread occurrences across files
    thread_occurrences = defaultdict(list)
    thread_contents = defaultdict(list)

    # Iterate through each file in the directory
    for filename in os.listdir(directory):
        if os.path.isfile(os.path.join(directory, filename)):
            with open(os.path.join(directory, filename), 'r') as file:
                content = file.read()

                # Split content into individual stack traces
                stack_traces = content.split("\n\n")

                for stack_trace in stack_traces:
                    # Check if stack trace contains the filter line
                    if filter_line in stack_trace:
                        # Extract thread information
                        match = thread_pattern.search(stack_trace)
                        if match:
                            thread_name = match.group(1)
                            thread_id = match.group(2)
                            thread_info = (thread_name, thread_id)

                            # Extract and store stack trace content
                            stack_trace_content = stack_trace_pattern.sub('', stack_trace).strip()
                            thread_contents[thread_info].append(stack_trace_content)

                            # Record file occurrence for this thread
                            thread_occurrences[thread_info].append(filename)

    # Dictionary to store identical thread occurrences
    identical_threads = {}
    for thread_info, contents in thread_contents.items():
        if len(contents) > 1 and all(content == contents[0] for content in contents):
            identical_threads[thread_info] = len(contents)

    return thread_occurrences, identical_threads

# Directory containing stack trace files
directory = "/tmp/stacks"
# Filter line to identify threads of interest
filter_line = "at com.webmethods.portal.service.sql.core.BaseSqlWorker.executeQuery(BaseSqlWorker.java:625)"

# Analyze stack traces in the directory
thread_occurrences, identical_threads = analyze_stack_traces(directory, filter_line)

# Print results
print("Threads with occurrences across files:")
for thread_info, files in thread_occurrences.items():
    print(f"Thread {thread_info} appeared in files: {files}")

print("\nIdentical threads with their occurrence counts:")
for thread_info, count in identical_threads.items():
    print(f"Thread {thread_info} appeared {count} times with identical contents.")
