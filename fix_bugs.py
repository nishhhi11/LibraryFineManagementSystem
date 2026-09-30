import re

with open("src/GUI.java", "r") as f:
    text = f.read()

# Fix grid to slabsGrid in the slabs panel injection
slabs_grid_replacement = """
        JPanel slabsGrid = new JPanel(new GridLayout(3, 2, 5, 5));
        slabsGrid.setOpaque(false);
        String[] rules = {"1-7 Days", "₹5/day", "8-14 Days", "₹10/day", "15+ Days", "₹20/day"};
        for (String rule : rules) {
            JLabel l = new JLabel(rule);
            l.setFont(SMALL); l.setForeground(MUTED);
            slabsGrid.add(l);
        }
        slabsPanel.add(slabsGrid);
"""

text = re.sub(
    r'JPanel grid = new JPanel\(new GridLayout\(3, 2, 5, 5\)\);\s*'
    r'grid\.setOpaque\(false\);\s*'
    r'String\[\] rules = \{"1-7 Days", "₹5/day", "8-14 Days", "₹10/day", "15\+ Days", "₹20/day"\};\s*'
    r'for \(String rule : rules\) \{\s*'
    r'JLabel l = new JLabel\(rule\);\s*'
    r'l\.setFont\(SMALL\); l\.setForeground\(MUTED\);\s*'
    r'grid\.add\(l\);\s*'
    r'\}\s*'
    r'slabsPanel\.add\(grid\);',
    slabs_grid_replacement.strip(),
    text
)

# Fix dashboardAction method
action_replacement = """
    JButton dashboardAction(String title, String subtitle, String iconStr, boolean isPrimary) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        GlassPanel glass = new GlassPanel(
                isPrimary ? TERRACOTTA : new Color(255, 252, 246, 225),
                new Color(255, 255, 255, 170)
        );
        glass.setLayout(new BorderLayout());
        glass.setBorder(new EmptyBorder(12, 17, 12, 17));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(iconStr + " " + title);
        titleLabel.setFont(SUBTITLE);
        titleLabel.setForeground(isPrimary ? WHITE : INK);

        JLabel sub = new JLabel(subtitle);
        sub.setFont(SMALL);
        sub.setForeground(isPrimary ? new Color(255,255,255,200) : MUTED);

        content.add(titleLabel);
        content.add(Box.createVerticalStrut(3));
        content.add(sub);
        
        JLabel arrow = new JLabel(">");
        arrow.setFont(TITLE);
        arrow.setForeground(isPrimary ? WHITE : MUTED);
        
        glass.add(content, BorderLayout.CENTER);
        glass.add(arrow, BorderLayout.EAST);
        button.add(glass, BorderLayout.CENTER);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                glass.setBorder(new javax.swing.border.CompoundBorder(
                        new javax.swing.border.LineBorder(new Color(210, 190, 165), 1),
                        new EmptyBorder(11, 16, 11, 16)
                ));
                button.setLocation(button.getX(), button.getY() - 2);
                glass.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                glass.setBorder(new EmptyBorder(12, 17, 12, 17));
                button.setLocation(button.getX(), button.getY() + 2);
                glass.repaint();
            }
        });

        return button;
    }
"""

text = re.sub(
    r'JButton dashboardAction\(\s*String title,\s*String subtitle\)\s*\{.*?(?=\n    // =========================================================\n    // BOOK COVER\n)',
    action_replacement.strip() + "\n",
    text,
    flags=re.DOTALL
)

with open("src/GUI.java", "w") as f:
    f.write(text)

print("Bugs fixed")
