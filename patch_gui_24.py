import sys

def patch_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    old_code = """        actions.add(returnBook);

        center.add(actions);


        // Keep dashboard content at top"""
        
    new_code = """        actions.add(returnBook);

        center.add(actions);
        center.add(Box.createVerticalStrut(25));
        
        // ---------- BOTTOM WIDGETS (ACTIVITY, SLABS, CHART) ----------
        JPanel bottomWidgets = new JPanel(new GridLayout(1, 3, 15, 0));
        bottomWidgets.setOpaque(false);
        
        // 1. Activity Row
        GlassPanel activityPanel = new GlassPanel(CREAM, new Color(255, 255, 255, 170));
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
                JLabel row = new JLabel("<html><b>" + r.getStudentId() + "</b> " + (r.isReturned() ? "returned" : "borrowed") + " " + r.getBookId() + "</html>");
                row.setFont(SMALL); row.setForeground(INK);
                activityPanel.add(row);
                activityPanel.add(Box.createVerticalStrut(5));
            }
        }
        bottomWidgets.add(activityPanel);
        
        // 2. Fine Slabs Card
        GlassPanel slabsPanel = new GlassPanel(INK, new Color(255,255,255,40));
        slabsPanel.setLayout(new BoxLayout(slabsPanel, BoxLayout.Y_AXIS));
        slabsPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel slabTitle = new JLabel("Fine Slabs");
        slabTitle.setFont(SMALL_BOLD); slabTitle.setForeground(GOLD);
        slabsPanel.add(slabTitle);
        slabsPanel.add(Box.createVerticalStrut(10));
        
        JPanel grid = new JPanel(new GridLayout(3, 2, 5, 5));
        grid.setOpaque(false);
        String[] rules = {"1-7 Days", "\u20b95/day", "8-14 Days", "\u20b910/day", "15+ Days", "\u20b920/day"};
        for (String rule : rules) {
            JLabel l = new JLabel(rule);
            l.setFont(SMALL); l.setForeground(WHITE);
            grid.add(l);
        }
        slabsPanel.add(grid);
        bottomWidgets.add(slabsPanel);
        
        // 3. Category Breakdown (Mocked Donut)
        GlassPanel chartPanel = new GlassPanel(CREAM, new Color(255,255,255,170)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int size = Math.min(getWidth(), getHeight()) - 40;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2 + 10;
                // Draw slices
                g2.setColor(SAGE); g2.fillArc(x, y, size, size, 0, 120);
                g2.setColor(GOLD); g2.fillArc(x, y, size, size, 120, 150);
                g2.setColor(TERRACOTTA); g2.fillArc(x, y, size, size, 270, 90);
                // Draw inner donut hole
                g2.setColor(CREAM); g2.fillOval(x + 15, y + 15, size - 30, size - 30);
                g2.dispose();
            }
        };
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
        chartPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel cTitle = new JLabel("Category Breakdown");
        cTitle.setFont(SMALL_BOLD); cTitle.setForeground(INK);
        chartPanel.add(cTitle);
        bottomWidgets.add(chartPanel);
        
        center.add(bottomWidgets);

        // Keep dashboard content at top"""
        
    if old_code in content:
        content = content.replace(old_code, new_code)
        with open(filepath, 'w') as f:
            f.write(content)
        print("Patched successfully")
    else:
        print("Failed to find target code block")

patch_file('src/GUI.java')
