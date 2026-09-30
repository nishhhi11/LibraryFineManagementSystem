import re

with open('src/GUI.java', 'r') as f:
    text = f.read()

# Modify bookCard method
start_book_card = text.find("    JPanel bookCard(Book book) {")
end_book_card = text.find("    JButton smallButton(", start_book_card)

new_book_card = """
    JPanel bookCard(Book book) {
        JPanel container = new JPanel();
        container.setLayout(new OverlayLayout(container));
        container.setOpaque(false);
        
        // Overlay Panel
        JPanel overlay = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 160));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        overlay.setOpaque(false);
        overlay.setVisible(false);
        
        JPanel overlayContent = new JPanel();
        overlayContent.setLayout(new BoxLayout(overlayContent, BoxLayout.Y_AXIS));
        overlayContent.setOpaque(false);
        
        JButton details = new JButton("VIEW DETAILS ➔");
        details.setFont(SMALL_BOLD);
        details.setForeground(WHITE);
        details.setContentAreaFilled(false);
        details.setBorderPainted(false);
        details.setCursor(new Cursor(Cursor.HAND_CURSOR));
        details.addActionListener(e -> showBookDetails(book));
        
        JButton edit = new JButton("Edit Book");
        edit.setFont(SMALL_BOLD);
        edit.setForeground(WHITE);
        edit.setContentAreaFilled(false);
        edit.setBorderPainted(false);
        edit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton delete = new JButton("Delete Book");
        delete.setFont(SMALL_BOLD);
        delete.setForeground(new Color(255, 100, 100));
        delete.setContentAreaFilled(false);
        delete.setBorderPainted(false);
        delete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        details.setAlignmentX(Component.CENTER_ALIGNMENT);
        edit.setAlignmentX(Component.CENTER_ALIGNMENT);
        delete.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        overlayContent.add(details);
        overlayContent.add(Box.createVerticalStrut(10));
        overlayContent.add(edit);
        overlayContent.add(delete);
        
        overlay.add(overlayContent);
        
        // Main Content
        GlassPanel card = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170));
        card.setLayout(new BorderLayout());
        
        JPanel cover = createCover(book);
        cover.setPreferredSize(new Dimension(140, 210)); 

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(new EmptyBorder(12, 10, 15, 10));

        JLabel category = new JLabel(book.getCategory().toUpperCase());
        category.setFont(customSerif.deriveFont(Font.BOLD, 10f));
        category.setForeground(categoryColor(book.getCategory()));
        category.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("<html><div style='width:120px; text-align:center'>" + book.getTitle() + "</div></html>");
        title.setFont(BODY_BOLD);
        title.setForeground(INK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String authStr = book.getAuthor();
        if (authStr.length() > 22) {
            authStr = authStr.substring(0, 19) + "...";
        }
        JLabel author = new JLabel(authStr);
        author.setFont(SMALL);
        author.setForeground(MUTED);
        author.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel availability = new JLabel();
        availability.setFont(SMALL_BOLD);
        if (book.getAvailableCopies() > 0) {
            availability.setText(book.getAvailableCopies() + " / " + book.getTotalCopies() + " available");
            availability.setForeground(SAGE);
        } else {
            availability.setText("Unavailable · due 5 Oct");
            availability.setForeground(TERRACOTTA);
        }
        availability.setAlignmentX(Component.CENTER_ALIGNMENT);

        info.add(category);
        info.add(Box.createVerticalStrut(4));
        info.add(title);
        info.add(Box.createVerticalStrut(4));
        info.add(author);
        info.add(Box.createVerticalStrut(8));
        info.add(Box.createVerticalGlue());
        info.add(availability);

        card.add(cover, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(5, 0, 0, 0)); // For hover lift
        wrapper.add(container, BorderLayout.NORTH);
        
        MouseAdapter hoverAdapter = new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { 
                overlay.setVisible(true); 
                wrapper.setBorder(new EmptyBorder(0, 0, 5, 0));
            }
            public void mouseExited(MouseEvent e) { 
                overlay.setVisible(false); 
                wrapper.setBorder(new EmptyBorder(5, 0, 0, 0));
            }
        };
        card.addMouseListener(hoverAdapter);
        overlay.addMouseListener(hoverAdapter);
        
        container.add(overlay);
        container.add(card);
        
        return wrapper;
    }
"""

if start_book_card != -1 and end_book_card != -1:
    text = text[:start_book_card] + new_book_card + text[end_book_card:]


# Modify createBooksPage() to add "Add Book" button, and update "Showing 40 of 40" logic
start_create_books = text.find("    JPanel createBooksPage() {")
end_create_books = text.find("    JPanel bookCard(Book book) {")

# To save time and keep it safe, I'll extract and replace just the relevant parts of createBooksPage
create_books_content = text[start_create_books:end_create_books]

# Add "Add Book" button
search_box_add_code = """        actionsPanel.add(searchBox);"""
add_book_code = """        actionsPanel.add(searchBox);

        JButton addBookBtn = new JButton("+ Add Book");
        addBookBtn.setFont(SMALL_BOLD);
        addBookBtn.setForeground(WHITE);
        addBookBtn.setBackground(INK);
        addBookBtn.setFocusPainted(false);
        addBookBtn.setBorder(new EmptyBorder(10, 15, 10, 15));
        addBookBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actionsPanel.add(addBookBtn);"""
create_books_content = create_books_content.replace(search_box_add_code, add_book_code)

# Add label updating for "Showing 40 of 40" and empty state
key_listener_code = """        search.addKeyListener(new KeyAdapter() {
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
        });"""

new_key_listener_code = """        search.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String query = search.getText().toLowerCase().trim();
                grid.removeAll();
                int count = 0;
                for (Book book : books) {
                    if (book.getTitle().toLowerCase().contains(query) ||
                        book.getAuthor().toLowerCase().contains(query)) {
                        grid.add(bookCard(book));
                        count++;
                    }
                }
                if (count == 0) {
                    JLabel empty = new JLabel("No books match '" + search.getText() + "'");
                    empty.setFont(BODY);
                    empty.setForeground(MUTED);
                    grid.add(empty);
                }
                sub.setText("Showing " + count + " of " + books.size() + " titles");
                grid.revalidate();
                grid.repaint();
            }
        });"""
create_books_content = create_books_content.replace(key_listener_code, new_key_listener_code)

text = text[:start_create_books] + create_books_content + text[end_create_books:]

# Modify createCover() to add shadow
start_create_cover = text.find("    JPanel createCover(")
end_create_cover = text.find("    Color categoryColor(")
create_cover_content = text[start_create_cover:end_create_cover]
old_paint_cover = """                        java.awt.Shape clip = new java.awt.geom.RoundRectangle2D.Float(pad, pad, pw, ph, 12, 12);
                        g2.setClip(clip);

                        if (finalCoverImage != null) {
                            int imageWidth = finalCoverImage.getWidth();
                            int imageHeight = finalCoverImage.getHeight();
                            double scale = Math.min((double) pw / imageWidth, (double) ph / imageHeight);
                            int drawWidth = Math.max(1, (int) Math.ceil(imageWidth * scale));
                            int drawHeight = Math.max(1, (int) Math.ceil(imageHeight * scale));
                            int x = pad + (pw - drawWidth) / 2;
                            int y = pad + (ph - drawHeight) / 2;
                            g2.drawImage(finalCoverImage, x, y, drawWidth, drawHeight, null);
                        } else {
                            g2.setColor(categoryColor(book.getCategory()));
                            g2.fillRect(pad, pad, pw, ph);
                            g2.setColor(WHITE);
                            g2.setFont(SMALL_BOLD);
                            String text = "NO COVER";
                            FontMetrics metrics = g2.getFontMetrics();
                            int x = pad + (pw - metrics.stringWidth(text)) / 2;
                            int y = pad + ((ph - metrics.getHeight()) / 2) + metrics.getAscent();
                            g2.drawString(text, x, y);
                        }
                        
                        g2.setClip(null);"""

new_paint_cover = """                        // Soft shadow
                        g2.setColor(new Color(0, 0, 0, 20));
                        g2.fillRoundRect(pad + 2, pad + 2, pw, ph, 12, 12);
                        g2.setColor(new Color(0, 0, 0, 10));
                        g2.fillRoundRect(pad + 4, pad + 4, pw, ph, 12, 12);

                        java.awt.Shape clip = new java.awt.geom.RoundRectangle2D.Float(pad, pad, pw, ph, 12, 12);
                        g2.setClip(clip);

                        if (finalCoverImage != null) {
                            int imageWidth = finalCoverImage.getWidth();
                            int imageHeight = finalCoverImage.getHeight();
                            double scale = Math.max((double) pw / imageWidth, (double) ph / imageHeight);
                            int drawWidth = Math.max(1, (int) Math.ceil(imageWidth * scale));
                            int drawHeight = Math.max(1, (int) Math.ceil(imageHeight * scale));
                            int x = pad + (pw - drawWidth) / 2;
                            int y = pad + (ph - drawHeight) / 2;
                            g2.drawImage(finalCoverImage, x, y, drawWidth, drawHeight, null);
                        } else {
                            g2.setColor(categoryColor(book.getCategory()));
                            g2.fillRect(pad, pad, pw, ph);
                            g2.setColor(WHITE);
                            g2.setFont(SMALL_BOLD);
                            String text = "NO COVER";
                            FontMetrics metrics = g2.getFontMetrics();
                            int x = pad + (pw - metrics.stringWidth(text)) / 2;
                            int y = pad + ((ph - metrics.getHeight()) / 2) + metrics.getAscent();
                            g2.drawString(text, x, y);
                        }
                        
                        g2.setClip(null);
                        
                        // Inner border for polish
                        g2.setColor(new Color(255, 255, 255, 50));
                        g2.drawRoundRect(pad, pad, pw, ph, 12, 12);"""
create_cover_content = create_cover_content.replace(old_paint_cover, new_paint_cover)
text = text[:start_create_cover] + create_cover_content + text[end_create_cover:]


with open('src/GUI.java', 'w') as f:
    f.write(text)
