import re

with open("src/GUI.java", "r") as f:
    gui_text = f.read()

# 1. Extract slabsPanel
slabs_regex = r'(?s)(// 2\. Fine Slabs Card.*?bottomWidgets\.add\(slabsPanel\);)'
match = re.search(slabs_regex, gui_text)
if match:
    slabs_code = match.group(1)
    
    # Remove it from bottomWidgets
    gui_text = gui_text.replace(slabs_code, "")
    
    # We want to replace it in dashboard with "Overdue Books" card
    overdue_card = """
        // 2. Overdue Books Card
        GlassPanel overduePanel = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170));
        overduePanel.setLayout(new BoxLayout(overduePanel, BoxLayout.Y_AXIS));
        overduePanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JPanel overdueHeader = new JPanel(new BorderLayout());
        overdueHeader.setOpaque(false);
        JLabel overdueTitle = new JLabel("Overdue Books");
        overdueTitle.setFont(TITLE); overdueTitle.setForeground(INK);
        JLabel overdueSub = new JLabel("Live list");
        overdueSub.setFont(SMALL); overdueSub.setForeground(MUTED);
        overdueHeader.add(overdueTitle, BorderLayout.WEST);
        overdueHeader.add(overdueSub, BorderLayout.EAST);
        overduePanel.add(overdueHeader);
        
        overduePanel.add(Box.createVerticalStrut(10));
        
        int overdueCount = 0;
        for (LibRecord r : records) {
            if (!r.isReturned()) {
                overdueCount++;
                JPanel row = new JPanel(new BorderLayout());
                row.setOpaque(false);
                JLabel bookLabel = new JLabel("<html><div style='width: 140px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'><b>" + r.getStudent().getName().split(" ")[0] + "</b> | " + r.getBook().getTitle() + "</div></html>");
                bookLabel.setFont(SMALL); bookLabel.setForeground(INK);
                
                // MOCK data: say it's 3 days late, fine 15
                JLabel fineLabel = new JLabel("3d late (₹15)");
                fineLabel.setFont(SMALL_BOLD); fineLabel.setForeground(TERRACOTTA);
                
                row.add(bookLabel, BorderLayout.CENTER);
                row.add(fineLabel, BorderLayout.EAST);
                overduePanel.add(row);
                overduePanel.add(Box.createVerticalStrut(5));
                if (overdueCount >= 5) break;
            }
        }
        
        if (overdueCount == 0) {
            JLabel noOverdue = new JLabel("No books currently overdue.");
            noOverdue.setFont(SMALL); noOverdue.setForeground(MUTED);
            overduePanel.add(noOverdue);
        }
        
        bottomWidgets.add(overduePanel);
"""
    gui_text = gui_text.replace("bottomWidgets.add(activityPanel);", "bottomWidgets.add(activityPanel);\n" + overdue_card)

    header_injection = """
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(header, BorderLayout.WEST);
        
""" + slabs_code.replace("bottomWidgets.add(slabsPanel);", "topPanel.add(slabsPanel, BorderLayout.EAST);\n        topPanel.setBorder(new EmptyBorder(30,35,15,35));\n        header.setBorder(new EmptyBorder(0,0,0,0));\n        page.add(topPanel, BorderLayout.NORTH);")

    gui_text = re.sub(r'page\.add\(\s*\n\s*header,\s*\n\s*BorderLayout\.NORTH\s*\n\s*\);', header_injection, gui_text)

with open("src/GUI.java", "w") as f:
    f.write(gui_text)

print("Fines UI updated")
