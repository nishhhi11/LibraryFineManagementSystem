import re

with open("src/GUI.java", "r") as f:
    text = f.read()

# 1. Update Sidebar
nav_replacements = [
    ('addNavigation(\n                top,\n                "Overview",\n                "HOME"\n        );', 'addNavigation(top, "⌂", "Overview", "HOME");'),
    ('addNavigation(\n                top,\n                "Book Collection",\n                "BOOKS"\n        );', 'addNavigation(top, "📚", "Book Collection", "BOOKS");'),
    ('addNavigation(\n                top,\n                "Students",\n                "STUDENTS"\n        );', 'addNavigation(top, "👤", "Students", "STUDENTS");'),
    ('addNavigation(\n                top,\n                "Issue Book",\n                "ISSUE"\n        );', 'addNavigation(top, "🔖", "Issue Book", "ISSUE");'),
    ('addNavigation(\n                top,\n                "Return Book",\n                "RETURN"\n        );', 'addNavigation(top, "↩️", "Return Book", "RETURN");'),
    ('addNavigation(\n                top,\n                "Fine Records",\n                "FINES"\n        );', 'addNavigation(top, "🧾", "Fine Records", "FINES");')
]
for old, new in nav_replacements:
    text = text.replace(old, new)

text = re.sub(
    r'void addNavigation\(\s*JPanel parent,\s*String text,\s*String page\)\s*\{.*?parent\.add\(\s*Box\.createVerticalStrut\(7\)\s*\);\s*\}',
    '''void addNavigation(JPanel parent, String icon, String text, String page) {
        JButton button = navButton(icon, text, page);
        parent.add(button);
        parent.add(Box.createVerticalStrut(2));
    }''',
    text,
    flags=re.DOTALL
)

text = re.sub(
    r'JButton navButton\(\s*String text,\s*String page\)\s*\{.*?button\.add\(\s*label,\s*BorderLayout\.WEST\s*\);\s*button\.addActionListener',
    '''JButton navButton(String icon, String text, String page) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (activePage != null && activePage.equals(page)) {
                    g2.setColor(new Color(72, 58, 49));
                    g2.fillRoundRect(4, 0, getWidth() - 8, getHeight(), 12, 12);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(60, 45, 35));
                    g2.fillRoundRect(4, 0, getWidth() - 8, getHeight(), 12, 12);
                }
                super.paintComponent(g);
                g2.dispose();
            }
        };
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(180, 40));
        button.setMaximumSize(new Dimension(180, 40));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 8));
        button.setBackground(ESPRESSO);
        button.setBorder(new EmptyBorder(0, 10, 0, 8));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(icon + "  " + text);
        label.setFont(BODY_BOLD);
        label.setForeground(new Color(230, 222, 212));
        button.add(label);

        button.addActionListener''',
    text,
    flags=re.DOTALL
)

# 2. Change Topbar
text = re.sub(
    r'JPanel right =\s*new JPanel\(\s*new FlowLayout\(\s*FlowLayout\.RIGHT,\s*25,\s*17\s*\)\s*\);\s*right\.setOpaque\(false\);\s*JLabel status =\s*new JLabel\(\s*""\s*\);\s*status\.setFont\(SMALL_BOLD\);\s*status\.setForeground\(SAGE\);\s*right\.add\(status\);',
    '''JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        right.setOpaque(false);
        
        GlassPanel search = new GlassPanel(WHITE, SAND);
        search.setPreferredSize(new Dimension(240, 34));
        search.setLayout(new BorderLayout());
        search.setBorder(new EmptyBorder(0, 12, 0, 12));
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setForeground(MUTED);
        JLabel searchTxt = new JLabel("Search books or students...");
        searchTxt.setFont(SMALL); searchTxt.setForeground(MUTED);
        search.add(searchIcon, BorderLayout.WEST);
        search.add(searchTxt, BorderLayout.CENTER);
        
        JLabel bell = new JLabel("🔔");
        bell.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        bell.setBorder(new EmptyBorder(0, 5, 0, 5));
        
        JPanel profile = new JPanel(new BorderLayout());
        profile.setOpaque(false);
        profile.setPreferredSize(new Dimension(32, 32));
        JLabel pLabel = new JLabel("N", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(TERRACOTTA);
                g2.fillOval(0,0,getWidth(),getHeight());
                super.paintComponent(g);
                g2.dispose();
            }
        };
        pLabel.setForeground(WHITE);
        pLabel.setFont(SMALL_BOLD);
        profile.add(pLabel);
        
        right.add(search);
        right.add(bell);
        right.add(profile);''',
    text
)

# 3. GlassPanel border and shadow, and MUTED color
text = re.sub(r'static final Color MUTED =\s*new Color\(120,\s*108,\s*96\);', 'static final Color MUTED = new Color(100, 90, 80);', text)

text = re.sub(
    r'g2\.setColor\(\s*new Color\(\s*60,\s*45,\s*35,\s*16\s*\)\s*\);',
    'g2.setColor(new Color(60, 45, 35, 25));',
    text
)
text = re.sub(
    r'g2\.setColor\(\s*borderColor\s*\);',
    'g2.setColor(new Color(230, 215, 195));',
    text
)

book_card_replace = """
            final JPanel bookCard = new JPanel(new BorderLayout());
            bookCard.setOpaque(false);
            bookCard.setBorder(new EmptyBorder(10, 0, 0, 0));
            bookCard.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    bookCard.setBorder(new EmptyBorder(5, 0, 5, 0));
                    bookCard.repaint();
                }
                public void mouseExited(java.awt.event.MouseEvent e) {
                    bookCard.setBorder(new EmptyBorder(10, 0, 0, 0));
                    bookCard.repaint();
                }
            });
"""
text = re.sub(
    r'JPanel bookCard =\s*new JPanel\(\s*new BorderLayout\(\)\s*\);\s*bookCard\.setOpaque\(false\);',
    book_card_replace.strip(),
    text
)

with open("src/GUI.java", "w") as f:
    f.write(text)

print("Polish applied")
