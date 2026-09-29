import sys
import re

def patch_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # 1. Add Toast Component at the end of the file (before the last closing brace)
    toast_code = """
    // =========================================================
    // TOAST NOTIFICATIONS & BADGES
    // =========================================================

    public void showToast(String message, Color color) {
        JWindow toast = new JWindow(this);
        toast.setBackground(new Color(0, 0, 0, 0));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(33, 33, 33, 230));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(12, 24, 12, 24)
        ));
        
        JLabel label = new JLabel(message);
        label.setFont(new Font("Inter", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        panel.add(label, BorderLayout.CENTER);
        
        toast.add(panel);
        toast.pack();
        
        // Position at top right
        Point loc = this.getLocation();
        toast.setLocation(loc.x + this.getWidth() - toast.getWidth() - 30, loc.y + 40);
        
        toast.setOpacity(0.0f);
        toast.setVisible(true);
        
        // Fade in
        Timer fadeIn = new Timer(20, null);
        fadeIn.addActionListener(new ActionListener() {
            float opacity = 0.0f;
            public void actionPerformed(ActionEvent e) {
                opacity += 0.1f;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    fadeIn.stop();
                    // Start fade out after 2.5 seconds
                    Timer fadeOut = new Timer(20, null);
                    fadeOut.setInitialDelay(2500);
                    fadeOut.addActionListener(new ActionListener() {
                        float outOpacity = 1.0f;
                        public void actionPerformed(ActionEvent ev) {
                            outOpacity -= 0.1f;
                            if (outOpacity <= 0.0f) {
                                fadeOut.stop();
                                toast.dispose();
                            } else {
                                toast.setOpacity(outOpacity);
                            }
                        }
                    });
                    fadeOut.start();
                }
                toast.setOpacity(opacity);
            }
        });
        fadeIn.start();
    }
"""
    
    # Insert before the last closing brace
    last_brace = content.rfind('}')
    content = content[:last_brace] + toast_code + content[last_brace:]

    # 2. Replace JOptionPane in issueBook with Toast
    old_issue_alert = """        JOptionPane.showMessageDialog(
                this,
                "Book issued successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );"""
    
    new_issue_alert = """        showToast("Book successfully issued to " + sId + "!", SAGE);"""
    content = content.replace(old_issue_alert, new_issue_alert)
    
    with open(filepath, 'w') as f:
        f.write(content)

patch_file('src/GUI.java')
