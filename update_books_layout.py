import re

with open('src/GUI.java', 'r') as f:
    text = f.read()

# 1. Update text "40 titles available" to "40 titles · 116 copies"
text = text.replace('"40 titles available"', '"40 titles · 116 copies"')

# 2. Add header bottom border
# In createBooksPage(), find: page.add(header, BorderLayout.NORTH);
# Let's add the category chips and sort menu, and set a MatteBorder.
# Wait, replacing the whole createBooksPage and bookCard methods is safer than string replacements.

books_page_method = """
    JPanel createBooksPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setOpaque(false);

        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0,0,0,20)),
            new EmptyBorder(30, 35, 15, 35)
        ));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Book Collection");
        title.setFont(TITLE);
        title.setForeground(INK);

        JLabel sub = new JLabel("40 titles · 116 copies");
        sub.setFont(SMALL);
        sub.setForeground(MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(sub);
        
        header.add(titlePanel, BorderLayout.WEST);

        // Filters and Search
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionsPanel.setOpaque(false);
        
        // Category Chips
        String[] categories = {"All", "Computer Science", "Self-Help", "Fiction", "Classics"};
        JPanel chipsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        chipsPanel.setOpaque(false);
        for (String cat : categories) {
            JButton chip = new JButton(cat);
            chip.setFont(SMALL_BOLD);
            chip.setForeground(cat.equals("All") ? WHITE : MUTED);
            chip.setBackground(cat.equals("All") ? TERRACOTTA : CREAM);
            chip.setOpaque(true);
            chip.setBorderPainted(false);
            chip.setFocusPainted(false);
            chip.setCursor(new Cursor(Cursor.HAND_CURSOR));
            chipsPanel.add(chip);
        }
        actionsPanel.add(chipsPanel);
        
        // Sort Menu
        String[] sortOptions = {"Sort by Title", "Sort by Author", "Sort by Availability"};
        JComboBox<String> sortMenu = new JComboBox<>(sortOptions);
        sortMenu.setFont(SMALL);
        actionsPanel.add(sortMenu);

        // Search box
        JPanel searchBox = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                if (((JTextField)((BorderLayout)getLayout()).getLayoutComponent(BorderLayout.CENTER)).hasFocus()) {
                    g2.setColor(new Color(60, 160, 150)); // Teal
                    g2.setStroke(new BasicStroke(2f));
                } else {
                    g2.setColor(SAND);
                    g2.setStroke(new BasicStroke(1f));
                }
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        searchBox.setOpaque(false);
        searchBox.setPreferredSize(new Dimension(280, 44));
        searchBox.setBorder(new EmptyBorder(0, 12, 0, 12));

        JLabel searchIcon = new JLabel("⌕");
        searchIcon.setFont(customSerif.deriveFont(Font.BOLD, 22f));
        searchIcon.setForeground(MUTED);
        searchIcon.setBorder(new EmptyBorder(0, 2, 1, 7));

        JTextField search = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(MUTED);
                    g2.drawString("Search by title, author or ISBN...", getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top);
                    g2.dispose();
                }
            }
        };
        search.setFont(new Font("SansSerif", Font.PLAIN, 14));
        search.setForeground(INK);
        search.setBackground(new Color(255, 255, 255, 0));
        search.setOpaque(false);
        search.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        search.setToolTipText("Search by title or author");

        searchBox.add(searchIcon, BorderLayout.WEST);
        searchBox.add(search, BorderLayout.CENTER);
        
        actionsPanel.add(searchBox);

        header.add(actionsPanel, BorderLayout.EAST);
        page.add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 6, 20, 20)); // 6 covers per row
        grid.setBackground(CREAM);
        grid.setBorder(new EmptyBorder(30, 35, 30, 35));

        for (Book book : books) {
            grid.add(bookCard(book));
        }

        search.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String query = search.getText().toLowerCase().trim();
                grid.removeAll();
                for (Book book : books) {
                    if (book.getTitle().toLowerCase().contains(query) ||
                        book.getAuthor().toLowerCase().contains(query)) {
                        grid.add(bookCard(book));
                    }
                }
                grid.revalidate();
                grid.repaint();
            }
        });

        search.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { searchBox.repaint(); }
            public void focusLost(FocusEvent e) { searchBox.repaint(); }
        });

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        page.add(scroll, BorderLayout.CENTER);
        return page;
    }
"""

book_card_method = """
    JPanel bookCard(Book book) {
        // We use an OverlayLayout to put the hover overlay on top of the card content
        JPanel container = new JPanel();
        container.setLayout(new OverlayLayout(container));
        container.setOpaque(false);
        
        // 1. Overlay Panel
        JPanel overlay = new JPanel(new GridBagLayout());
        overlay.setBackground(new Color(0, 0, 0, 160)); // Dark semi-transparent
        overlay.setOpaque(true);
        overlay.setVisible(false);
        
        JButton details = smallButton("VIEW DETAILS");
        details.addActionListener(e -> showBookDetails(book));
        overlay.add(details);
        
        // 2. Main Content
        GlassPanel card = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170));
        card.setLayout(new BorderLayout());
        
        JPanel cover = createCover(book);
        cover.setPreferredSize(new Dimension(140, 210)); // Tall cover

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(new EmptyBorder(12, 10, 15, 10));

        JLabel title = new JLabel("<html><div style='width:120px; text-align:center'>" + book.getTitle() + "</div></html>");
        title.setFont(BODY_BOLD);
        title.setForeground(INK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel author = new JLabel("<html><div style='width:120px; text-align:center'>" + book.getAuthor() + "</div></html>");
        author.setFont(SMALL);
        author.setForeground(MUTED);
        author.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel availability = new JLabel(book.getAvailableCopies() + " / " + book.getTotalCopies() + " available");
        availability.setFont(SMALL_BOLD);
        availability.setForeground(book.getAvailableCopies() > 0 ? SAGE : TERRACOTTA);
        availability.setAlignmentX(Component.CENTER_ALIGNMENT);

        info.add(title);
        info.add(Box.createVerticalStrut(6));
        info.add(author);
        info.add(Box.createVerticalStrut(8));
        info.add(Box.createVerticalGlue());
        info.add(availability);

        card.add(cover, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);
        
        // Setup Hover effect
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { overlay.setVisible(true); }
            public void mouseExited(MouseEvent e) { overlay.setVisible(false); }
        });
        overlay.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { overlay.setVisible(true); }
            public void mouseExited(MouseEvent e) { overlay.setVisible(false); }
        });
        
        // Add components to OverlayLayout container
        // Order matters: first added is on top!
        container.add(overlay);
        container.add(card);
        
        // Wrap in another panel to prevent stretching in GridLayout if necessary
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(container, BorderLayout.NORTH);
        
        return wrapper;
    }
"""

start_books = text.find("JPanel createBooksPage()")
end_books = text.find("JPanel bookCard(", start_books)
end_books_method = text.find("JButton smallButton(", end_books)

if start_books != -1 and end_books != -1 and end_books_method != -1:
    text = text[:start_books] + books_page_method + book_card_method + text[end_books_method:]
    with open('src/GUI.java', 'w') as f:
        f.write(text)
    print("Updated GUI.java successfully!")
else:
    print("Could not find the target methods.")
