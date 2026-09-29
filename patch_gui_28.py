import sys

def patch_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    old_greeting = """        JLabel title =
                new JLabel(
                        "Good evening, Librarian."
                );"""
                
    new_greeting = """        int hour = java.time.LocalTime.now().getHour();
        String greeting = "Good evening";
        if (hour >= 5 && hour < 12) greeting = "Good morning";
        else if (hour >= 12 && hour < 17) greeting = "Good afternoon";
        
        JLabel title =
                new JLabel(
                        greeting + ", Librarian."
                );"""
                
    if old_greeting in content:
        content = content.replace(old_greeting, new_greeting)
        with open(filepath, 'w') as f:
            f.write(content)
        print("Patched successfully")
    else:
        print("Failed to find target code block")

patch_file('src/GUI.java')
