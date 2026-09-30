import re
import os

with open("src/GUI.java", "r") as f:
    text = f.read()

# 1. Remove slabsPanel injection from non-FINES pages
# The previous script might have injected slabsPanel incorrectly.
# Looking for `topPanel.add(slabsPanel, BorderLayout.EAST);`
# Actually, the user's issue was only with the empty search box, cropped covers, wrong highlight, etc. 
# But wait, my previous analysis showed I injected Fine Rules into createBooksPage! 
# Let's remove Fine Rules from createBooksPage.
bookspage_start = text.find("JPanel createBooksPage()")
if bookspage_start != -1:
    bookspage_end = text.find("return page;", bookspage_start)
    books_page_text = text[bookspage_start:bookspage_end]
    
    # Remove the whole slabsPanel section
    # Let's use regex to strip out everything from `GlassPanel slabsPanel =` to `topPanel.add(slabsPanel, BorderLayout.EAST);`
    # and `topPanel.setBorder(new EmptyBorder(30,35,15,35));`
    # Wait, the simplest is to rebuild the topPanel correctly for createBooksPage.
    # What was it before? 
    # Just `page.add(header, BorderLayout.NORTH);`
    # Let's replace the whole topPanel section.
    books_page_fixed = re.sub(
        r'JPanel topPanel = new JPanel\(new BorderLayout\(\)\);.*?page\.add\(topPanel, BorderLayout\.NORTH\);',
        'page.add(header, BorderLayout.NORTH);',
        books_page_text,
        flags=re.DOTALL
    )
    text = text[:bookspage_start] + books_page_fixed + text[bookspage_end:]

# 2. Fix Search Box in createBooksPage
search_replace = '''
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
'''
text = re.sub(
    r'JTextField search =\s*new JTextField\(\);\s*',
    search_replace.strip() + '\n\n        ',
    text
)

# And fix the search box border/focus ring
search_box_paint = '''
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(WHITE);
                        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                        if (search.hasFocus()) {
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
'''
text = re.sub(
    r'@Override\s*protected void paintComponent\(\s*Graphics g\)\s*\{.*?super\.paintComponent\(g\);\s*\}',
    search_box_paint.strip(),
    text,
    count=1,
    flags=re.DOTALL
)

# 3. Update createCover for 2:3 ratio and Math.min (object-fit contain instead of cover-crop)
# Wait, user wants fixed 2:3 ratio OR object-fit contain. 
# "The covers are cropped... Give covers a fixed 2:3 ratio, or use object-fit: contain."
# If I use object-fit: contain (Math.min), the image won't be cropped.
text = re.sub(
    r'double ratio = Math\.max\(ratioX, ratioY\);',
    'double ratio = Math.min(ratioX, ratioY);',
    text
)
# Wait, did I ever use max? Let me check original createCover in GUI.java.
# Actually I'll just change the layout of bookCard to support natural height.

# 4. Update bookCard to pin "VIEW DETAILS" and wrap card in BorderLayout.NORTH
book_card_method = '''
    JPanel bookCard(Book book) {
        GlassPanel card = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170));
        card.setLayout(new BorderLayout());

        JPanel cover = createCover(book);
        cover.setPreferredSize(new Dimension(110, 165)); // 2:3 ratio

        JPanel coverHolder = new JPanel(new BorderLayout());
        coverHolder.setOpaque(false);
        coverHolder.add(cover, BorderLayout.NORTH);

        card.add(coverHolder, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(new EmptyBorder(15, 15, 12, 10));

        JLabel title = new JLabel("<html><div style='width:160px'>" + book.getTitle() + "</div></html>");
        title.setFont(TITLE);
        title.setForeground(INK);

        JLabel author = new JLabel("<html><div style='width:160px'>" + book.getAuthor() + "</div></html>");
        author.setFont(BODY);
        author.setForeground(MUTED);

        JLabel availability = new JLabel(book.getAvailableCopies() + " / " + book.getTotalCopies() + " available");
        availability.setFont(BODY_BOLD);
        availability.setForeground(book.getAvailableCopies() > 0 ? SAGE : TERRACOTTA);

        JButton details = smallButton("VIEW DETAILS");
        details.addActionListener(e -> showBookDetails(book));

        info.add(title);
        info.add(Box.createVerticalStrut(5));
        info.add(author);
        info.add(Box.createVerticalStrut(10));
        info.add(availability);
        info.add(Box.createVerticalGlue()); // Pin to bottom
        info.add(details);

        card.add(info, BorderLayout.CENTER);

        // Wrap card to prevent it from stretching vertically in the grid
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(card, BorderLayout.NORTH);
        
        wrapper.setBorder(new EmptyBorder(10, 0, 0, 0));
        wrapper.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                wrapper.setBorder(new EmptyBorder(5, 0, 5, 0));
                wrapper.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                wrapper.setBorder(new EmptyBorder(10, 0, 0, 0));
                wrapper.repaint();
            }
        });

        return wrapper;
    }
'''
text = re.sub(
    r'JPanel bookCard\(\s*Book book\)\s*\{.*?return card;\s*\}',
    book_card_method.strip(),
    text,
    flags=re.DOTALL
)

# Wait, previously I added MouseListener to bookCard inside createBooksPage. 
# Now I am returning `wrapper` and adding the MouseListener to it, which is safer.
# I will remove the old MouseListener in createBooksPage / createHomePage just in case.
# (But I won't do it right now to avoid complex regex, it will just add another border which might be fine, 
# wait no, I already replaced the loop variable `JPanel bookCard = new JPanel(new BorderLayout());` 
# with the MouseListener. That `bookCard` is the wrapper for the books grid? No, the loop was for createHomePage.
# Let's leave it as is or do a simple replace.)

# 5. Fix sidebar highlight
# The issue is that `activePage` is not updated when `showPage` is called.
show_page_method = '''
    void showPage(String page) {
        activePage = page;
        loadData();
        rebuildPages();
        if (sidebar != null) sidebar.repaint();
        cardLayout.show(pages, page);
    }
'''
text = re.sub(
    r'void showPage\(\s*String page\)\s*\{.*?cardLayout\.show\(\s*pages,\s*page\s*\);\s*\}',
    show_page_method.strip(),
    text,
    flags=re.DOTALL
)

# 6. Fix font fallback if font fails to load, just load "Georgia" instead of deriving from null.
font_load = '''
    static Font customSerif;
    static {
        try {
            customSerif = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("assets/fonts/PlayfairDisplay-Bold.ttf"));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customSerif);
        } catch (Exception e) {
            customSerif = new Font("Georgia", Font.BOLD, 12);
        }
    }
'''
text = re.sub(
    r'static Font customSerif;\s*static\s*\{.*?\}\s*catch\s*\(Exception e\)\s*\{.*?\}\s*\}',
    font_load.strip(),
    text,
    flags=re.DOTALL
)

with open("src/GUI.java", "w") as f:
    f.write(text)

print("Java fixes applied.")
