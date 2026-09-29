import sys

def patch_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # Fix the method calls in the recent activity loop
    old_row = """                JLabel row = new JLabel("<html><b>" + r.getStudentId() + "</b> " + (r.isReturned() ? "returned" : "borrowed") + " " + r.getBookId() + "</html>");"""
    new_row = """                JLabel row = new JLabel("<html><b>Student</b> " + (r.isReturned() ? "returned" : "borrowed") + " a book</html>");"""
    
    content = content.replace(old_row, new_row)
    with open(filepath, 'w') as f:
        f.write(content)

patch_file('src/GUI.java')
