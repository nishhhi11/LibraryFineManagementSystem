with open('data/books.txt', 'r') as f:
    lines = f.readlines()

new_lines = []
for line in lines:
    parts = line.strip().split('|')
    if len(parts) == 6:
        id = parts[0]
        if id == 'A102':
            parts[5] = '0' # Unavailable
        elif id == 'A105':
            parts[5] = '1'
        elif id == 'F403':
            parts[5] = '0'
        elif id == 'G302':
            parts[5] = '1'
        
        new_lines.append('|'.join(parts) + '\n')
    else:
        new_lines.append(line)

with open('data/books.txt', 'w') as f:
    f.writelines(new_lines)
