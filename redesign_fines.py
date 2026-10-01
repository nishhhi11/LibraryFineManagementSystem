import re

with open('src/GUI.java', 'r') as f:
    gui = f.read()

# Locate createFinesPage start
start_fines = gui.find("JPanel createFinesPage() {")
# Locate end of fineCard
end_fines = gui.find("    Book findBook(", start_fines)
end_fines = gui.rfind("}", start_fines, end_fines) + 1

new_fines_code = """
    JPanel createFinesPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(CREAM);

        // Header
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(30, 35, 18, 35));

        JLabel title = new JLabel("Fine Ledger");
        title.setFont(DISPLAY);
        title.setForeground(INK);

        JLabel sub = new JLabel("Track and settle library fines.");
        sub.setFont(BODY);
        sub.setForeground(MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(sub);
        
        // Stats Calculation
        double totalFines = 0;
        double unpaidAmount = 0;
        int unpaidStudents = 0;
        java.util.HashSet<String> unpaidStudentSet = new java.util.HashSet<>();
        int overdueBooks = 0;
        double collectedThisMonth = 0;

        for (LibRecord r : records) {
            if (r.isReturned()) {
                totalFines += r.getFine();
                if ("UNPAID".equals(r.getFineStatus())) {
                    unpaidAmount += r.getFine();
                    unpaidStudentSet.add(r.getStudent().getId());
                } else if ("PAID".equals(r.getFineStatus())) {
                    collectedThisMonth += r.getFine();
                }
            } else {
                long days = java.time.temporal.ChronoUnit.DAYS.between(r.getIssueDate(), java.time.LocalDate.now());
                if (days > r.getAllowedDays()) {
                    overdueBooks++;
                }
            }
        }
        unpaidStudents = unpaidStudentSet.size();

        // KPIs
        JPanel stats = new JPanel(new GridLayout(1, 4, 15, 0));
        stats.setOpaque(false);
        stats.setBorder(new EmptyBorder(0, 35, 20, 35));
        
        stats.add(statCard("TOTAL FINES", "₹" + (int)totalFines, "All time records", TERRACOTTA));
        stats.add(statCard("UNPAID", "₹" + (int)unpaidAmount, unpaidStudents + " students", GOLD));
        stats.add(statCard("OVERDUE NOW", String.valueOf(overdueBooks), "Not returned yet", new Color(200, 80, 80)));
        stats.add(statCard("COLLECTED", "₹" + (int)collectedThisMonth, "This month", SAGE));

        // Filters
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filters.setOpaque(false);
        filters.setBorder(new EmptyBorder(0, 20, 10, 20));
        
        JTextField searchFines = new JTextField(20);
        searchFines.setPreferredSize(new Dimension(200, 35));
        searchFines.setText("Search student or book...");
        searchFines.setForeground(MUTED);
        
        String[] statuses = {"All Statuses", "Unpaid", "Paid", "Waived", "None"};
        JComboBox<String> statusFilter = new JComboBox<>(statuses);
        statusFilter.setPreferredSize(new Dimension(150, 35));
        
        JButton exportBtn = new JButton("Export CSV");
        exportBtn.setBackground(INK);
        exportBtn.setForeground(WHITE);
        exportBtn.setFocusPainted(false);
        
        filters.add(searchFines);
        filters.add(statusFilter);
        filters.add(Box.createHorizontalStrut(20));
        filters.add(exportBtn);

        // Top section
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(header, BorderLayout.NORTH);
        top.add(stats, BorderLayout.CENTER);
        top.add(filters, BorderLayout.SOUTH);
        page.add(top, BorderLayout.NORTH);

        // Table
        JPanel list = new JPanel();
        list.setBackground(CREAM);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(new EmptyBorder(0, 35, 30, 35));
        
        // Table Header
        JPanel th = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        th.setOpaque(false);
        th.setMaximumSize(new Dimension(1200, 30));
        th.add(createHeaderLabel("Book", 260));
        th.add(createHeaderLabel("Student", 180));
        th.add(createHeaderLabel("Issued", 80));
        th.add(createHeaderLabel("Due", 80));
        th.add(createHeaderLabel("Returned", 80));
        th.add(createHeaderLabel("Late", 60));
        th.add(createHeaderLabel("Fine", 70));
        th.add(createHeaderLabel("Status", 100));
        th.add(createHeaderLabel("Action", 100));
        
        list.add(th);
        list.add(Box.createVerticalStrut(10));

        int fineRecordsCount = 0;
        for (LibRecord record : records) {
            if (record.isReturned()) {
                list.add(fineRow(record));
                list.add(Box.createVerticalStrut(8));
                fineRecordsCount++;
            }
        }
        
        if (fineRecordsCount == 0) {
            JLabel empty = new JLabel("No fines recorded. Every book returned on time.");
            empty.setFont(BODY);
            empty.setForeground(MUTED);
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            list.add(Box.createVerticalStrut(40));
            list.add(empty);
        }

        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setBackground(CREAM);
        listWrapper.add(list, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(listWrapper);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        page.add(scroll, BorderLayout.CENTER);
        return page;
    }
    
    JLabel createHeaderLabel(String text, int width) {
        JLabel l = new JLabel(text);
        l.setFont(SMALL_BOLD);
        l.setForeground(MUTED);
        l.setPreferredSize(new Dimension(width, 20));
        return l;
    }

    JPanel fineRow(LibRecord record) {
        GlassPanel row = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170));
        row.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setMaximumSize(new Dimension(1200, 65));
        row.setPreferredSize(new Dimension(1100, 65));
        row.setBorder(new EmptyBorder(5, 10, 5, 10));

        // Book
        JPanel bookCell = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bookCell.setOpaque(false);
        bookCell.setPreferredSize(new Dimension(250, 50));
        JLabel cover = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("assets/covers/" + record.getBook().getId() + ".jpg");
            Image img = icon.getImage().getScaledInstance(35, 50, Image.SCALE_SMOOTH);
            cover.setIcon(new ImageIcon(img));
        } catch (Exception e) {}
        JLabel bTitle = new JLabel("<html><div style='width:180px;'>" + record.getBook().getTitle() + "</div></html>");
        bTitle.setFont(BODY_BOLD);
        bTitle.setForeground(INK);
        bookCell.add(cover);
        bookCell.add(bTitle);

        // Student
        JPanel studentCell = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        studentCell.setOpaque(false);
        studentCell.setPreferredSize(new Dimension(170, 50));
        JLabel avatar = new JLabel(record.getStudent().getName().substring(0, 1), SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(new Color(220, 210, 200));
        avatar.setForeground(INK);
        avatar.setFont(SMALL_BOLD);
        avatar.setPreferredSize(new Dimension(30, 30));
        avatar.setBorder(BorderFactory.createLineBorder(new Color(200, 190, 180), 1, true));
        JLabel sName = new JLabel(record.getStudent().getName());
        sName.setFont(BODY);
        studentCell.add(avatar);
        studentCell.add(sName);

        // Dates
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd MMM");
        JLabel issued = new JLabel(record.getIssueDate().format(fmt));
        issued.setPreferredSize(new Dimension(80, 50));
        issued.setFont(SMALL);
        
        JLabel due = new JLabel(record.getIssueDate().plusDays(record.getAllowedDays()).format(fmt));
        due.setPreferredSize(new Dimension(80, 50));
        due.setFont(SMALL);
        
        java.time.LocalDate returnDate = record.getIssueDate().plusDays(record.getActualDays());
        JLabel returned = new JLabel(returnDate.format(fmt));
        returned.setPreferredSize(new Dimension(80, 50));
        returned.setFont(SMALL);

        // Days Late
        JLabel late = new JLabel(record.getDelayedDays() + "d");
        late.setPreferredSize(new Dimension(60, 50));
        late.setFont(BODY_BOLD);
        late.setForeground(record.getDelayedDays() > 0 ? TERRACOTTA : SAGE);

        // Fine
        JLabel fine = new JLabel("₹" + (int)record.getFine());
        fine.setPreferredSize(new Dimension(70, 50));
        fine.setFont(BODY_BOLD);
        fine.setForeground(record.getFine() > 0 ? TERRACOTTA : INK);
        if (record.getFine() > 0) {
            String calculation = "<html><body>" + record.getDelayedDays() + " days late.<br>Tiered rates apply.</body></html>";
            fine.setToolTipText(calculation);
        }

        // Status Pill
        JLabel status = new JLabel(record.getFineStatus(), SwingConstants.CENTER);
        status.setOpaque(true);
        status.setFont(SMALL_BOLD);
        status.setPreferredSize(new Dimension(80, 28));
        switch (record.getFineStatus()) {
            case "UNPAID":
                status.setBackground(new Color(250, 237, 232));
                status.setForeground(TERRACOTTA);
                break;
            case "PAID":
                status.setBackground(new Color(237, 245, 234));
                status.setForeground(SAGE);
                break;
            case "WAIVED":
                status.setBackground(new Color(230, 230, 230));
                status.setForeground(MUTED);
                break;
            default:
                status.setBackground(new Color(245, 245, 245));
                status.setForeground(MUTED);
                status.setText("NONE");
        }
        JPanel statusCell = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        statusCell.setOpaque(false);
        statusCell.setPreferredSize(new Dimension(100, 50));
        statusCell.add(status);

        // Action
        JPanel actionCell = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        actionCell.setOpaque(false);
        actionCell.setPreferredSize(new Dimension(100, 50));
        if ("UNPAID".equals(record.getFineStatus())) {
            JButton payBtn = new JButton("Pay");
            payBtn.setFont(SMALL_BOLD);
            payBtn.setBackground(INK);
            payBtn.setForeground(WHITE);
            payBtn.setFocusPainted(false);
            payBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            payBtn.addActionListener(e -> {
                record.setFineStatus("PAID");
                FileManager.saveRecords(records);
                showPage("FINES");
            });
            actionCell.add(payBtn);
        } else if ("PAID".equals(record.getFineStatus())) {
            JLabel paidIcon = new JLabel("✓ Settled");
            paidIcon.setFont(SMALL_BOLD);
            paidIcon.setForeground(SAGE);
            actionCell.add(paidIcon);
        }

        row.add(bookCell);
        row.add(studentCell);
        row.add(issued);
        row.add(due);
        row.add(returned);
        row.add(late);
        row.add(fine);
        row.add(statusCell);
        row.add(actionCell);

        return row;
    }
"""

if start_fines != -1 and end_fines != -1:
    gui = gui[:start_fines] + new_fines_code + gui[end_fines:]
    with open('src/GUI.java', 'w') as f:
        f.write(gui)
    print("Replaced fines page successfully.")
else:
    print("Failed to find boundaries.", start_fines, end_fines)
