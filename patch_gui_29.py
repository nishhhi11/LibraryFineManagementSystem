import sys

def patch_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # Find the bottom widgets block we added earlier
    start_marker = "// ---------- BOTTOM WIDGETS (ACTIVITY, SLABS, CHART) ----------"
    end_marker = "        // Keep dashboard content at top"
    
    if start_marker in content and end_marker in content:
        start_idx = content.find(start_marker)
        end_idx = content.find(end_marker)
        old_block = content[start_idx:end_idx]
        
        new_block = """// ---------- BOTTOM WIDGETS (ACTIVITY, SLABS, CHART) ----------
        JPanel bottomWidgets = new JPanel(new GridLayout(1, 3, 15, 0));
        bottomWidgets.setOpaque(false);
        
        // 1. Activity Row
        GlassPanel activityPanel = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170));
        activityPanel.setLayout(new BoxLayout(activityPanel, BoxLayout.Y_AXIS));
        activityPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel actTitle = new JLabel("Recent Activity");
        actTitle.setFont(SMALL_BOLD); actTitle.setForeground(INK);
        activityPanel.add(actTitle);
        activityPanel.add(Box.createVerticalStrut(10));
        if (records.isEmpty()) {
            JLabel empty = new JLabel("No activity yet.");
            empty.setFont(SMALL); empty.setForeground(MUTED);
            activityPanel.add(empty);
        } else {
            for (int i = records.size() - 1; i >= Math.max(0, records.size() - 3); i--) {
                LibRecord r = records.get(i);
                JPanel row = new JPanel(new BorderLayout());
                row.setOpaque(false);
                JLabel text = new JLabel("<html><b>" + r.getStudentId() + "</b> " + (r.isReturned() ? "returned" : "borrowed") + " " + r.getBookId() + "</html>");
                text.setFont(SMALL); text.setForeground(INK);
                row.add(text, BorderLayout.WEST);
                if (r.isReturned() && r.getFine() > 0) {
                    JLabel badge = new JLabel(" \u20b9" + r.getFine() + " ");
                    badge.setOpaque(true); badge.setBackground(TERRACOTTA); badge.setForeground(WHITE); badge.setFont(SMALL_BOLD);
                    row.add(badge, BorderLayout.EAST);
                }
                activityPanel.add(row);
                activityPanel.add(Box.createVerticalStrut(5));
            }
        }
        bottomWidgets.add(activityPanel);
        
        // 2. Fine Slabs Card
        GlassPanel slabsPanel = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170));
        slabsPanel.setLayout(new BoxLayout(slabsPanel, BoxLayout.Y_AXIS));
        slabsPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel slabTitle = new JLabel("Fine Rules");
        slabTitle.setFont(SMALL_BOLD); slabTitle.setForeground(INK);
        slabsPanel.add(slabTitle);
        slabsPanel.add(Box.createVerticalStrut(10));
        
        JPanel grid = new JPanel(new GridLayout(3, 2, 5, 5));
        grid.setOpaque(false);
        String[] rules = {"1-7 Days", "\u20b95/day", "8-14 Days", "\u20b910/day", "15+ Days", "\u20b920/day"};
        for (String rule : rules) {
            JLabel l = new JLabel(rule);
            l.setFont(SMALL); l.setForeground(MUTED);
            grid.add(l);
        }
        slabsPanel.add(grid);
        slabsPanel.add(Box.createVerticalStrut(10));
        JLabel note = new JLabel("Fine = days late \u00d7 rate");
        note.setFont(SMALL); note.setForeground(MUTED);
        slabsPanel.add(note);
        bottomWidgets.add(slabsPanel);
        
        // 3. Category Breakdown
        GlassPanel chartPanel = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int size = Math.min(getWidth(), getHeight()) - 60;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2 + 15;
                
                // Count categories dynamically
                int prog = 0, data = 0, net = 0;
                for(Book b : books) {
                    if(b.getCategory().equalsIgnoreCase("Programming")) prog++;
                    else if(b.getCategory().equalsIgnoreCase("Databases")) data++;
                    else net++;
                }
                int total = prog + data + net;
                if(total == 0) total = 1; // prevent div zero
                
                int ang1 = (int)(prog * 360.0 / total);
                int ang2 = (int)(data * 360.0 / total);
                int ang3 = 360 - ang1 - ang2;
                
                g2.setColor(SAGE); g2.fillArc(x, y, size, size, 0, ang1);
                g2.setColor(GOLD); g2.fillArc(x, y, size, size, ang1, ang2);
                g2.setColor(TERRACOTTA); g2.fillArc(x, y, size, size, ang1+ang2, ang3);
                g2.setColor(new Color(255, 252, 246)); g2.fillOval(x + 15, y + 15, size - 30, size - 30);
                g2.dispose();
            }
        };
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
        chartPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel cTitle = new JLabel("Categories");
        cTitle.setFont(SMALL_BOLD); cTitle.setForeground(INK);
        chartPanel.add(cTitle);
        bottomWidgets.add(chartPanel);
        
        center.add(bottomWidgets);

        """
        
        content = content[:start_idx] + new_block + content[end_idx:]
        with open(filepath, 'w') as f:
            f.write(content)
        print("Patched successfully")
    else:
        print("Failed to find target code block")

patch_file('src/GUI.java')
