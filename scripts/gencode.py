#!/usr/bin/env python3
# Extract code snippets from the complete Chisel examples in src

import os

def list_files(folder):
    for entry in os.listdir(folder):
        full_path = os.path.join(folder, entry)
        if os.path.isfile(full_path):
            extract(full_path)
        elif os.path.isdir(full_path):
            list_files(full_path)

def extract(f):
    print(f)
    code = None
    with open(f, 'r', errors='replace') as infile:
        for line in infile:
            tokens = line.strip().split()
            if len(tokens) >= 2 and tokens[0] in ('//-', '--/'):
                if tokens[1] == 'start':
                    code = open(os.path.join('code', tokens[2] + '.txt'), 'w')
                elif tokens[1] == 'end':
                    if code is not None:
                        code.close()
                        code = None
            elif code is not None:
                code.write(line)
    if code is not None:
        code.close()

list_files('src/main/scala/')
list_files('src/test/scala/')
list_files('src/main/vhdl/')
