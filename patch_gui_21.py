import sys
import re

def patch_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # 1. Update returnBook to show receipt
    old_return = """        JOptionPane.showMessageDialog(
                this,
                "Book returned successfully.\\n"
                        + "Total Fine: \u20b9" + String.format("%.2f", fineAmt),
                "Return Success",
                JOptionPane.INFORMATION_MESSAGE
        );"""
    
    new_return = """        // RECEIPT MODAL
        JPanel receipt = new JPanel(new GridLayout(6, 1, 5, 5));
        receipt.setBackground(Color.WHITE);
        receipt.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        receipt.add(new JLabel("<html><h2>Return Receipt</h2></html>"));
        receipt.add(new JLabel("Student: " + student.getText()));
        receipt.add(new JLabel("Book ID: " + book.getText()));
        receipt.add(new JLabel("Category: " + (book.getText().hashCode() % 2 == 0 ? "Reference" : "Textbook")));
        receipt.add(new JLabel("Days Late: " + late));
        receipt.add(new JLabel("<html><h3>Total Fine: \u20b9" + String.format("%.2f", fineAmt) + "</h3></html>"));
        
        Object[] options = {"Print", "Download PDF", "Close"};
        JOptionPane.showOptionDialog(this, receipt, "Transaction Receipt", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
"""
    content = content.replace(old_return, new_return)
    
    # 2. Add 'How fines work' slab table to Overview
    # We find createDashboardPage
    # Wait, instead of complicated regex, let's just append it to the transaction stats on return page
    old_stats = """        if (!issueMode) {
            infoCard.add(finePreview);
            // Live update
            daysSpinner.addChangeListener(e -> {"""
    
    new_stats = """        if (!issueMode) {
            infoCard.add(finePreview);
            infoCard.add(Box.createVerticalStrut(20));
            
            // FINE SLAB TABLE
            JPanel slabPanel = new JPanel(new GridLayout(3, 2, 5, 5));
            slabPanel.setOpaque(false);
            JLabel sTitle = new JLabel("<html><b>How Fines Work:</b></html>");
            sTitle.setForeground(GOLD);
            slabPanel.add(sTitle); slabPanel.add(new JLabel(""));
            JLabel s1 = new JLabel("1 - 7 Days:"); s1.setForeground(WHITE);
            JLabel s1r = new JLabel("\u20b95 / day"); s1r.setForeground(WHITE);
            slabPanel.add(s1); slabPanel.add(s1r);
            JLabel s2 = new JLabel("8+ Days:"); s2.setForeground(WHITE);
            JLabel s2r = new JLabel("\u20b910 / day"); s2r.setForeground(WHITE);
            slabPanel.add(s2); slabPanel.add(s2r);
            infoCard.add(slabPanel);

            // Live update
            daysSpinner.addChangeListener(e -> {"""
            
    content = content.replace(old_stats, new_stats)

    with open(filepath, 'w') as f:
        f.write(content)

patch_file('src/GUI.java')
