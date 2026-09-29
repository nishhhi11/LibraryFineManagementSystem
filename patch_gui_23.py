import sys
import re

def patch_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # We will override switchPage to add a very basic GlassPane fade (mocked as a simple delay for safety since true transparent fading requires custom GlassPane rendering).
    
    # Let's find switchPage
    old_switch = """    void switchPage(
            String name) {

        activePage = name;

        cardLayout.show(
                mainPanel,
                name
        );

        sidebar.repaint();
    }"""
    
    new_switch = """    void switchPage(String name) {
        // Page Transition Animation
        activePage = name;
        final JPanel glass = (JPanel) getGlassPane();
        glass.setVisible(true);
        glass.setOpaque(true);
        glass.setBackground(new Color(255, 252, 246, 0)); // Start transparent CREAM
        
        Timer fader = new Timer(15, null);
        fader.addActionListener(new ActionListener() {
            int alpha = 0;
            boolean switching = true;
            public void actionPerformed(ActionEvent e) {
                if (switching) {
                    alpha += 25;
                    if (alpha >= 255) {
                        alpha = 255;
                        switching = false;
                        // Actually swap the card while obscured
                        cardLayout.show(mainPanel, name);
                        sidebar.repaint();
                    }
                } else {
                    alpha -= 25;
                    if (alpha <= 0) {
                        alpha = 0;
                        fader.stop();
                        glass.setVisible(false);
                    }
                }
                glass.setBackground(new Color(255, 252, 246, alpha));
                glass.repaint();
            }
        });
        fader.start();
    }"""
    
    content = content.replace(old_switch, new_switch)
    
    with open(filepath, 'w') as f:
        f.write(content)

patch_file('src/GUI.java')
