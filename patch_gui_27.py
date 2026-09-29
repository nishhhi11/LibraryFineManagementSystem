import sys

def patch_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # Define activePage
    old_sidebar_decl = """    JPanel sidebar;
    JPanel createSidebar() {"""
    new_sidebar_decl = """    JPanel sidebar;
    String activePage = "HOME";
    JPanel createSidebar() {"""
    
    if old_sidebar_decl in content:
        content = content.replace(old_sidebar_decl, new_sidebar_decl)
        
    # Update paintComponent to handle activePage potentially being null during init
    old_nav_button = """                        if (activePage.equals(page)) {"""
    new_nav_button = """                        if (activePage != null && activePage.equals(page)) {"""
    content = content.replace(old_nav_button, new_nav_button)
    
    with open(filepath, 'w') as f:
        f.write(content)

patch_file('src/GUI.java')
