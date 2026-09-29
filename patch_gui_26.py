import sys

def patch_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    old_nav_button = """    JButton navButton(
            String text,
            String page) {

        JButton button =
                new JButton();"""
                
    new_nav_button = """    JButton navButton(
            String text,
            String page) {

        JButton button =
                new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        if (activePage.equals(page)) {
                            setBackground(new Color(72, 58, 49)); // Lighter active background
                        } else {
                            if (!getModel().isRollover()) setBackground(ESPRESSO);
                        }
                        super.paintComponent(g);
                        if (activePage.equals(page)) {
                            g.setColor(GOLD);
                            g.fillRect(0, 0, 4, getHeight());
                        }
                    }
                };"""
                
    if old_nav_button in content:
        content = content.replace(old_nav_button, new_nav_button)
        
        # Also need to make sure sidebar repaints on showPage
        old_show = """    void showPage(
            String pageName) {

        activePage = pageName;"""
        new_show = """    void showPage(
            String pageName) {

        activePage = pageName;
        if (sidebar != null) sidebar.repaint();"""
        content = content.replace(old_show, new_show)
        
        # Make sidebar accessible globally if it isn't
        if "JPanel sidebar;" not in content:
            old_sidebar_decl = """    JPanel createSidebar() {

        JPanel sidebar ="""
            new_sidebar_decl = """    JPanel sidebar;
    JPanel createSidebar() {

        sidebar ="""
            content = content.replace(old_sidebar_decl, new_sidebar_decl)
        
        with open(filepath, 'w') as f:
            f.write(content)
        print("Patched successfully")
    else:
        print("Failed to find target code block")

patch_file('src/GUI.java')
