import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GUI extends JFrame {

    static ArrayList<Student> students;
    static ArrayList<Book> books;
    static ArrayList<LibRecord> records;

    CardLayout cardLayout;
    String preselectedBook = "";
    JPanel pages;

    // ================= COLORS =================

    static final Color INK =
            new Color(43, 35, 30);

    static final Color ESPRESSO =
            new Color(52, 42, 36);

    static final Color CREAM =
            new Color(248, 243, 234);

    static final Color PAPER =
            new Color(255, 252, 246);

    static final Color SAND =
            new Color(226, 214, 196);

    static final Color TERRACOTTA =
            new Color(184, 98, 77);

    static final Color SAGE =
            new Color(105, 126, 98);

    static final Color GOLD =
            new Color(193, 157, 91);

    static final Color MUTED =
            new Color(120, 108, 96);

    static final Color WHITE =
            Color.WHITE;

    // ================= FONTS =================

    static final Font DISPLAY =
            new Font(
                    "Serif",
                    Font.BOLD,
                    31
            );

    static final Font TITLE =
            new Font(
                    "Serif",
                    Font.BOLD,
                    21
            );

    static final Font SUBTITLE =
            new Font(
                    "Serif",
                    Font.BOLD,
                    16
            );

    static final Font BODY =
            new Font(
                    "SansSerif",
                    Font.PLAIN,
                    13
            );

    static final Font BODY_BOLD =
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    13
            );

    static final Font SMALL =
            new Font(
                    "SansSerif",
                    Font.PLAIN,
                    11
            );

    static final Font SMALL_BOLD =
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    10
            );


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public GUI() {

        loadData();

        setTitle("MindSpace Library");

        setSize(
                1380,
                850
        );

        setMinimumSize(
                new Dimension(
                        1100,
                        720
                )
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        createInterface();

        setVisible(true);
    }


    // =========================================================
    // DATA
    // =========================================================

    void loadData() {

        FileManager.setupFiles();

        FileManager.addDefaultBooks();

        FileManager.addDefaultStudents();

        students =
                FileManager.loadStudents();

        books =
                FileManager.loadBooks();

        records =
                FileManager.loadRecords(
                        students,
                        books
                );
    }


    void saveData() {

        FileManager.saveStudents(
                students
        );

        FileManager.saveBooks(
                books
        );

        FileManager.saveRecords(
                records
        );
    }


    // =========================================================
    // MAIN INTERFACE
    // =========================================================

    void createInterface() {

        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(CREAM);

        root.add(
                createSidebar(),
                BorderLayout.WEST
        );

        root.add(
                createWorkspace(),
                BorderLayout.CENTER
        );

        setContentPane(root);
    }


    // =========================================================
    // SIDEBAR
    // =========================================================

    JPanel createSidebar() {

        JPanel sidebar =
                new JPanel(
                        new BorderLayout()
                );

        sidebar.setPreferredSize(
                new Dimension(
                        230,
                        0
                )
        );

        sidebar.setBackground(
                ESPRESSO
        );

        JPanel top =
                new JPanel();

        top.setOpaque(false);

        top.setLayout(
                new BoxLayout(
                        top,
                        BoxLayout.Y_AXIS
                )
        );

        top.setBorder(
                new EmptyBorder(
                        32,
                        24,
                        20,
                        20
                )
        );

        JLabel logo =
                new JLabel(
                        "MINDSPACE"
                );

        logo.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        26
                )
        );

        logo.setForeground(WHITE);

        JLabel sub =
                new JLabel(
                        "LIBRARY MANAGEMENT"
                );

        sub.setFont(
                SMALL_BOLD
        );

        sub.setForeground(GOLD);

        top.add(logo);

        top.add(
                Box.createVerticalStrut(4)
        );

        top.add(sub);

        top.add(
                Box.createVerticalStrut(38)
        );

        addNavigation(
                top,
                "Overview",
                "HOME"
        );

        addNavigation(
                top,
                "Book Collection",
                "BOOKS"
        );

        addNavigation(
                top,
                "Students",
                "STUDENTS"
        );

        addNavigation(
                top,
                "Issue Book",
                "ISSUE"
        );

        addNavigation(
                top,
                "Return Book",
                "RETURN"
        );

        addNavigation(
                top,
                "Fine Records",
                "FINES"
        );

        sidebar.add(
                top,
                BorderLayout.NORTH
        );

        JPanel bottom =
                new JPanel();

        bottom.setOpaque(false);

        bottom.setLayout(
                new BoxLayout(
                        bottom,
                        BoxLayout.Y_AXIS
                )
        );

        bottom.setBorder(
                new EmptyBorder(
                        15,
                        24,
                        25,
                        20
                )
        );

        JLabel online =
                new JLabel(
                        "●  SYSTEM ONLINE"
                );

        online.setFont(SMALL_BOLD);

        online.setForeground(SAGE);

        JLabel version =
                new JLabel(
                        "MindSpace Library  •  v1.0"
                );

        version.setFont(SMALL);

        version.setForeground(
                new Color(
                        160,
                        148,
                        137
                )
        );

        bottom.add(online);

        bottom.add(
                Box.createVerticalStrut(5)
        );

        bottom.add(version);

        sidebar.add(
                bottom,
                BorderLayout.SOUTH
        );

        return sidebar;
    }


    void addNavigation(
            JPanel parent,
            String text,
            String page) {

        JButton button =
                navButton(
                        text,
                        page
                );

        parent.add(button);

        parent.add(
                Box.createVerticalStrut(7)
        );
    }


    JButton navButton(
            String text,
            String page) {

        JButton button =
                new JButton();

        button.setPreferredSize(
                new Dimension(
                        180,
                        44
                )
        );

        button.setMaximumSize(
                new Dimension(
                        180,
                        44
                )
        );

        button.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        button.setLayout(
                new BorderLayout()
        );

        button.setBackground(
                ESPRESSO
        );

        button.setBorder(
                new EmptyBorder(
                        0,
                        13,
                        0,
                        8
                )
        );

        button.setFocusPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        JLabel label =
                new JLabel(text);

        label.setFont(BODY_BOLD);

        label.setForeground(
                new Color(
                        230,
                        222,
                        212
                )
        );

        button.add(
                label,
                BorderLayout.WEST
        );

        button.addActionListener(
                e -> showPage(page)
        );

        button.addMouseListener(
                new MouseAdapter() {

                    public void mouseEntered(
                            MouseEvent e) {

                        button.setBackground(
                                new Color(
                                        72,
                                        58,
                                        49
                                )
                        );

                        label.setForeground(
                                WHITE
                        );
                    }

                    public void mouseExited(
                            MouseEvent e) {

                        button.setBackground(
                                ESPRESSO
                        );

                        label.setForeground(
                                new Color(
                                        230,
                                        222,
                                        212
                                )
                        );
                    }
                }
        );

        return button;
    }


    // =========================================================
    // WORKSPACE
    // =========================================================

    JPanel createWorkspace() {

        JPanel workspace =
                new JPanel(
                        new BorderLayout()
                );

        workspace.setBackground(CREAM);

        workspace.add(
                createTopbar(),
                BorderLayout.NORTH
        );

        cardLayout =
                new CardLayout();

        pages =
                new JPanel(cardLayout);

        pages.setBackground(CREAM);

        pages.add(
                createHomePage(),
                "HOME"
        );

        pages.add(
                createBooksPage(),
                "BOOKS"
        );

        pages.add(
                createStudentsPage(),
                "STUDENTS"
        );

        pages.add(
                createIssuePage(),
                "ISSUE"
        );

        pages.add(
                createReturnPage(),
                "RETURN"
        );

        pages.add(
                createFinesPage(),
                "FINES"
        );

        workspace.add(
                pages,
                BorderLayout.CENTER
        );

        return workspace;
    }


    JPanel createTopbar() {

        JPanel bar =
                new JPanel(
                        new BorderLayout()
                );

        bar.setPreferredSize(
                new Dimension(
                        0,
                        64
                )
        );

        bar.setBackground(PAPER);

        bar.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        SAND
                )
        );

        JPanel left =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                28,
                                19
                        )
                );

        left.setOpaque(false);

        JLabel title =
                new JLabel(
                        "Library Management"
                );

        title.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        18
                )
        );

        title.setForeground(INK);

        left.add(title);

        JPanel right =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                25,
                                17
                        )
                );

        right.setOpaque(false);

        JLabel status =
                new JLabel(
                        "●  LIBRARY ONLINE"
                );

        status.setFont(SMALL_BOLD);

        status.setForeground(SAGE);

        right.add(status);

        bar.add(
                left,
                BorderLayout.WEST
        );

        bar.add(
                right,
                BorderLayout.EAST
        );

        return bar;
    }


    void showPage(
            String page) {

        loadData();

        rebuildPages();

        cardLayout.show(
                pages,
                page
        );
    }


    void rebuildPages() {

        pages.removeAll();

        pages.add(
                createHomePage(),
                "HOME"
        );

        pages.add(
                createBooksPage(),
                "BOOKS"
        );

        pages.add(
                createStudentsPage(),
                "STUDENTS"
        );

        pages.add(
                createIssuePage(),
                "ISSUE"
        );

        pages.add(
                createReturnPage(),
                "RETURN"
        );

        pages.add(
                createFinesPage(),
                "FINES"
        );

        pages.revalidate();

        pages.repaint();
    }


    // =========================================================
    // DASHBOARD
    // =========================================================

    JPanel createHomePage() {

        JPanel page =
                new JPanel(
                        new BorderLayout()
                );

        page.setBackground(CREAM);

        JPanel content =
                new JPanel(
                        new BorderLayout()
                );

        content.setBackground(CREAM);

        content.setBorder(
                new EmptyBorder(
                        26,
                        32,
                        22,
                        32
                )
        );


        // ---------- HEADER ----------

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setOpaque(false);

        JPanel heading =
                new JPanel();

        heading.setOpaque(false);

        heading.setLayout(
                new BoxLayout(
                        heading,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "Good evening, Librarian."
                );

        title.setFont(DISPLAY);

        title.setForeground(INK);

        JLabel subtitle =
                new JLabel(
                        "Your library, beautifully organized."
                );

        subtitle.setFont(BODY);

        subtitle.setForeground(MUTED);

        heading.add(title);

        heading.add(
                Box.createVerticalStrut(3)
        );

        heading.add(subtitle);

        header.add(
                heading,
                BorderLayout.WEST
        );

        JLabel brand =
                new JLabel(
                        "MINDSPACE  •  LIBRARY"
                );

        brand.setFont(SMALL_BOLD);

        brand.setForeground(
                TERRACOTTA
        );

        header.add(
                brand,
                BorderLayout.EAST
        );

        content.add(
                header,
                BorderLayout.NORTH
        );


        // ---------- DASHBOARD CONTENT ----------

        JPanel center =
                new JPanel();

        center.setOpaque(false);

        center.setLayout(
                new BoxLayout(
                        center,
                        BoxLayout.Y_AXIS
                )
        );


        center.add(
                Box.createVerticalStrut(16)
        );


        // ---------- HERO ----------

        GlassPanel hero =
                new GlassPanel(
                        INK,
                        new Color(
                                255,
                                255,
                                255,
                                45
                        )
                );

        hero.setLayout(
                new BorderLayout()
        );

        hero.setPreferredSize(
                new Dimension(
                        0,
                        140
                )
        );

        hero.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        140
                )
        );

        hero.setBorder(
                new EmptyBorder(
                        22,
                        27,
                        22,
                        27
                )
        );

        JPanel heroText =
                new JPanel();

        heroText.setOpaque(false);

        heroText.setLayout(
                new BoxLayout(
                        heroText,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel eyebrow =
                new JLabel(
                        "MINDSPACE / FINE MANAGEMENT SYSTEM"
                );

        eyebrow.setFont(SMALL_BOLD);

        eyebrow.setForeground(GOLD);

        JLabel heroTitle =
                new JLabel(
                        "Read. Learn. Return."
                );

        heroTitle.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        31
                )
        );

        heroTitle.setForeground(WHITE);

        JLabel heroSub =
                new JLabel(
                        "Manage books, students and fine records from one place."
                );

        heroSub.setFont(BODY);

        heroSub.setForeground(
                new Color(
                        215,
                        207,
                        197
                )
        );

        heroText.add(eyebrow);

        heroText.add(
                Box.createVerticalStrut(4)
        );

        heroText.add(heroTitle);

        heroText.add(
                Box.createVerticalStrut(4)
        );

        heroText.add(heroSub);

        hero.add(
                heroText,
                BorderLayout.WEST
        );

        center.add(hero);

        center.add(
                Box.createVerticalStrut(14)
        );


        // ---------- STATS ----------

        JPanel stats =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                12,
                                0
                        )
                );

        stats.setOpaque(false);

        int totalCopies = 0;

        int availableCopies = 0;

        double totalFine = 0;

        for (Book book : books) {

            totalCopies +=
                    book.getTotalCopies();

            availableCopies +=
                    book.getAvailableCopies();
        }

        for (LibRecord record : records) {

            if (record.isReturned()) {

                totalFine +=
                        record.getFine();
            }
        }

        int issuedCopies =
                totalCopies -
                        availableCopies;

        stats.add(
                statCard(
                        "BOOK TITLES",
                        String.valueOf(
                                books.size()
                        ),
                        "in collection",
                        TERRACOTTA
                )
        );

        stats.add(
                statCard(
                        "TOTAL COPIES",
                        String.valueOf(
                                totalCopies
                        ),
                        "physical books",
                        SAGE
                )
        );

        stats.add(
                statCard(
                        "ON LOAN",
                        String.valueOf(
                                issuedCopies
                        ),
                        "currently issued",
                        GOLD
                )
        );

        stats.add(
                statCard(
                        "TOTAL FINES",
                        "₹"
                                + String.format(
                                "%.0f",
                                totalFine
                        ),
                        "recorded amount",
                        INK
                )
        );

        center.add(stats);

        center.add(
                Box.createVerticalStrut(15)
        );


        // ---------- COLLECTION HEADER ----------

        JPanel collectionHeader =
                new JPanel(
                        new BorderLayout()
                );

        collectionHeader.setOpaque(false);

        JLabel collection =
                new JLabel(
                        "From the collection"
                );

        collection.setFont(TITLE);

        collection.setForeground(INK);

        JLabel collectionSub =
                new JLabel(
                        "Featured books from your shelves"
                );

        collectionSub.setFont(SMALL);

        collectionSub.setForeground(MUTED);

        collectionHeader.add(
                collection,
                BorderLayout.WEST
        );

        collectionHeader.add(
                collectionSub,
                BorderLayout.EAST
        );

        center.add(
                collectionHeader
        );

        center.add(
                Box.createVerticalStrut(8)
        );


        // ---------- BOOKS ----------

        JPanel bookRow =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                12,
                                0
                        )
                );

        bookRow.setOpaque(false);

        int count = 0;

        for (Book book : books) {

            if (count >= 4) {
                break;
            }

            bookRow.add(
                    dashboardBook(book)
            );

            count++;
        }

        center.add(bookRow);

        center.add(
                Box.createVerticalStrut(15)
        );


        // ---------- QUICK ACTIONS ----------

        JLabel quick =
                new JLabel(
                        "Quick actions"
                );

        quick.setFont(TITLE);

        quick.setForeground(INK);

        quick.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        center.add(quick);

        center.add(
                Box.createVerticalStrut(8)
        );

        JPanel actions =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                12,
                                0
                        )
                );

        actions.setOpaque(false);

        JButton browse =
                dashboardAction(
                        "Browse Collection",
                        "Explore books and availability"
                );

        JButton issue =
                dashboardAction(
                        "Issue a Book",
                        "Create a new issue record"
                );

        JButton returnBook =
                dashboardAction(
                        "Return a Book",
                        "Calculate delayed days and fine"
                );

        browse.addActionListener(
                e -> showPage("BOOKS")
        );

        issue.addActionListener(
                e -> showPage("ISSUE")
        );

        returnBook.addActionListener(
                e -> showPage("RETURN")
        );

        actions.add(browse);

        actions.add(issue);

        actions.add(returnBook);

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
                JLabel row = new JLabel("<html><b>Student</b> " + (r.isReturned() ? "returned" : "borrowed") + " a book</html>");
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
        String[] rules = {"1-7 Days", "₹5/day", "8-14 Days", "₹10/day", "15+ Days", "₹20/day"};
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

        // Keep dashboard content at top

        JPanel wrapper =
                new JPanel(
                        new BorderLayout()
                );

        wrapper.setOpaque(false);

        wrapper.add(
                center,
                BorderLayout.NORTH
        );

        content.add(
                wrapper,
                BorderLayout.CENTER
        );

        page.add(
                content,
                BorderLayout.CENTER
        );

        return page;
    }


    // =========================================================
    // STAT CARD
    // =========================================================

    JPanel statCard(
            String heading,
            String value,
            String caption,
            Color accent) {

        GlassPanel card =
                new GlassPanel(
                        new Color(
                                255,
                                252,
                                246,
                                225
                        ),
                        new Color(
                                255,
                                255,
                                255,
                                170
                        )
                );

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBorder(
                new EmptyBorder(
                        13,
                        17,
                        13,
                        17
                )
        );

        JLabel h =
                new JLabel(
                        heading
                );

        h.setFont(SMALL_BOLD);

        h.setForeground(MUTED);

        JLabel v =
                new JLabel(
                        value
                );

        v.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        27
                )
        );

        v.setForeground(accent);

        JLabel c =
                new JLabel(
                        caption
                );

        c.setFont(SMALL);

        c.setForeground(MUTED);

        card.add(h);

        card.add(
                Box.createVerticalStrut(4)
        );

        card.add(v);

        card.add(
                Box.createVerticalStrut(2)
        );

        card.add(c);

        return card;
    }


    // =========================================================
    // DASHBOARD BOOK CARD
    // =========================================================

    JPanel dashboardBook(
            Book book) {

                GlassPanel card = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170)) {
            private int yOffset = 0;
            private int shadowAlpha = 15;
            private javax.swing.Timer timer;
            private boolean hovered = false;
            
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) { hovered = true; startAnimation(); }
                    public void mouseExited(java.awt.event.MouseEvent e) { hovered = false; startAnimation(); }
                });
            }
            
            private void startAnimation() {
                if (timer != null) timer.stop();
                timer = new javax.swing.Timer(16, e -> {
                    boolean changed = false;
                    if (hovered && yOffset > -4) { yOffset--; changed = true; }
                    else if (!hovered && yOffset < 0) { yOffset++; changed = true; }
                    
                    if (hovered && shadowAlpha < 30) { shadowAlpha += 2; changed = true; }
                    else if (!hovered && shadowAlpha > 15) { shadowAlpha -= 2; changed = true; }
                    
                    if (changed) repaint();
                    else timer.stop();
                });
                timer.start();
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.translate(0, yOffset);
                g2.setColor(new Color(0, 0, 0, shadowAlpha));
                g2.fillRoundRect(0, 4, getWidth(), getHeight(), 16, 16); // drop shadow
                super.paintComponent(g);
                g2.dispose();
            }
        };
        
        // Skip normal init since we did it above
        card.setLayout(
                new BorderLayout()
        );

        JPanel cover =
                createCover(book);

        cover.setPreferredSize(
                new Dimension(
                        72,
                        105
                )
        );

        card.add(
                cover,
                BorderLayout.WEST
        );

        JPanel info =
                new JPanel();

        info.setOpaque(false);

        info.setLayout(
                new BoxLayout(
                        info,
                        BoxLayout.Y_AXIS
                )
        );

        info.setBorder(
                new EmptyBorder(
                        11,
                        12,
                        9,
                        7
                )
        );

        JLabel title =
                new JLabel(
                        "<html><div style='width:125px'>"
                                + book.getTitle()
                                + "</div></html>"
                );

        title.setFont(SUBTITLE);

        title.setForeground(INK);

        JLabel author =
                new JLabel(
                        "<html><div style='width:125px'>"
                                + book.getAuthor()
                                + "</div></html>"
                );

        author.setFont(SMALL);

        author.setForeground(MUTED);

        JLabel status =
                new JLabel(
                        book.getAvailableCopies() > 0
                                ? "● AVAILABLE"
                                : "● ON LOAN"
                );

        status.setFont(SMALL_BOLD);

        status.setForeground(
                book.getAvailableCopies() > 0
                        ? SAGE
                        : TERRACOTTA
        );

        info.add(title);

        info.add(
                Box.createVerticalStrut(3)
        );

        info.add(author);

        info.add(
                Box.createVerticalStrut(7)
        );

        info.add(status);

        card.add(
                info,
                BorderLayout.CENTER
        );

        return card;
    }


    // =========================================================
    // QUICK ACTION CARD
    // =========================================================

    JButton dashboardAction(
            String title,
            String subtitle) {

        JButton button =
                new JButton();

        button.setLayout(
                new BorderLayout()
        );

        button.setOpaque(false);

        button.setContentAreaFilled(false);

        button.setBorderPainted(false);

        button.setFocusPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        GlassPanel glass =
                new GlassPanel(
                        new Color(
                                255,
                                252,
                                246,
                                225
                        ),
                        new Color(
                                255,
                                255,
                                255,
                                170
                        )
                );

        glass.setLayout(
                new BorderLayout()
        );

        JPanel content =
                new JPanel();

        content.setOpaque(false);

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setBorder(
                new EmptyBorder(
                        12,
                        17,
                        12,
                        17
                )
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(SUBTITLE);

        titleLabel.setForeground(INK);

        JLabel sub =
                new JLabel(subtitle);

        sub.setFont(SMALL);

        sub.setForeground(MUTED);

        content.add(titleLabel);

        content.add(
                Box.createVerticalStrut(3)
        );

        content.add(sub);

        glass.add(
                content,
                BorderLayout.CENTER
        );

        button.add(
                glass,
                BorderLayout.CENTER
        );

        button.addMouseListener(
                new MouseAdapter() {

                    public void mouseEntered(
                            MouseEvent e) {

                        glass.setBorder(
                                new LineBorder(
                                        new Color(
                                                210,
                                                190,
                                                165
                                        ),
                                        1
                                )
                        );

                        glass.repaint();
                    }

                    public void mouseExited(
                            MouseEvent e) {

                        glass.setBorder(
                                new EmptyBorder(
                                        0,
                                        0,
                                        0,
                                        0
                                )
                        );

                        glass.repaint();
                    }
                }
        );

        return button;
    }


    // =========================================================
    // BOOK COVER
    // =========================================================

    // =========================================================
    // BOOK COVER
    // =========================================================

    File findCoverFile(
            String bookId) {

        String fileName =
                bookId + ".png";

        // Your current project keeps the images directly in assets.
        String[] directPaths = {
                "assets/" + fileName,
                "LibraryFineManagementSystem/assets/" + fileName,
                "../assets/" + fileName,
                "../LibraryFineManagementSystem/assets/" + fileName,
                "../../assets/" + fileName
        };

        for (String path : directPaths) {

            File file =
                    new File(path);

            if (file.exists() && file.isFile()) {
                return file;
            }
        }

        // Also search upward from the current working folder.
        File current =
                new File(
                        System.getProperty("user.dir")
                );

        for (int i = 0;
             i < 6 && current != null;
             i++) {

            File file =
                    new File(
                            current,
                            "assets/" + fileName
                    );

            if (file.exists() && file.isFile()) {
                return file;
            }

            file =
                    new File(
                            current,
                            "LibraryFineManagementSystem/assets/"
                                    + fileName
                    );

            if (file.exists() && file.isFile()) {
                return file;
            }

            current =
                    current.getParentFile();
        }

        // Finally check the location of the compiled class.
        try {

            File classLocation =
                    new File(
                            GUI.class
                                    .getProtectionDomain()
                                    .getCodeSource()
                                    .getLocation()
                                    .toURI()
                    );

            if (classLocation.isFile()) {
                classLocation =
                        classLocation.getParentFile();
            }

            for (int i = 0;
                 i < 6 && classLocation != null;
                 i++) {

                File file =
                        new File(
                                classLocation,
                                "assets/" + fileName
                        );

                if (file.exists() && file.isFile()) {
                    return file;
                }

                file =
                        new File(
                                classLocation,
                                "LibraryFineManagementSystem/assets/"
                                        + fileName
                        );

                if (file.exists() && file.isFile()) {
                    return file;
                }

                classLocation =
                        classLocation.getParentFile();
            }

        } catch (Exception ignored) {
        }

        return null;
    }


    JPanel createCover(
            Book book) {

        File imageFile =
                findCoverFile(
                        book.getId()
                );

        BufferedImage coverImage =
                null;

        try {

            if (imageFile != null) {

                coverImage =
                        ImageIO.read(imageFile);
            }

        } catch (IOException e) {

            System.out.println(
                    "Could not load cover: "
                            + book.getId()
            );
        }

        final BufferedImage finalCoverImage =
                coverImage;

        JPanel cover =
                new JPanel() {

                    @Override
                    protected void paintComponent(
                            Graphics g) {

                        super.paintComponent(g);

                        Graphics2D g2 =
                                (Graphics2D)
                                        g.create();

                        g2.setRenderingHint(
                                RenderingHints.KEY_INTERPOLATION,
                                RenderingHints.VALUE_INTERPOLATION_BICUBIC
                        );

                        g2.setRenderingHint(
                                RenderingHints.KEY_RENDERING,
                                RenderingHints.VALUE_RENDER_QUALITY
                        );

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        int panelWidth =
                                getWidth();

                        int panelHeight =
                                getHeight();

                        if (finalCoverImage != null) {

                            int imageWidth =
                                    finalCoverImage.getWidth();

                            int imageHeight =
                                    finalCoverImage.getHeight();

                            // Fit the complete cover inside the
                            // fixed cover area without cropping.
                            double scale =
                                    Math.min(
                                            (double) panelWidth
                                                    / imageWidth,
                                            (double) panelHeight
                                                    / imageHeight
                                    );

                            int drawWidth =
                                    Math.max(
                                            1,
                                            (int)
                                                    Math.ceil(
                                                            imageWidth
                                                                    * scale
                                                    )
                                    );

                            int drawHeight =
                                    Math.max(
                                            1,
                                            (int)
                                                    Math.ceil(
                                                            imageHeight
                                                                    * scale
                                                    )
                                    );

                            int x =
                                    (panelWidth
                                            - drawWidth) / 2;

                            int y =
                                    (panelHeight
                                            - drawHeight) / 2;

                            g2.drawImage(
                                    finalCoverImage,
                                    x,
                                    y,
                                    drawWidth,
                                    drawHeight,
                                    null
                            );

                        } else {

                            g2.setColor(
                                    categoryColor(
                                            book.getCategory()
                                    )
                            );

                            g2.fillRect(
                                    0,
                                    0,
                                    panelWidth,
                                    panelHeight
                            );

                            g2.setColor(WHITE);

                            g2.setFont(
                                    SMALL_BOLD
                            );

                            String text =
                                    "NO COVER";

                            FontMetrics metrics =
                                    g2.getFontMetrics();

                            int x =
                                    (panelWidth
                                            - metrics.stringWidth(text))
                                            / 2;

                            int y =
                                    (panelHeight
                                            + metrics.getAscent())
                                            / 2;

                            g2.drawString(
                                    text,
                                    x,
                                    y
                            );
                        }

                        g2.dispose();
                    }
                };

        cover.setBackground(
                categoryColor(
                        book.getCategory()
                )
        );

        cover.setOpaque(true);

        return cover;
    }


    Color categoryColor(
            String category) {

        if (
                category.equalsIgnoreCase(
                        "Academic"
                )
        ) {

            return SAGE;
        }

        if (
                category.equalsIgnoreCase(
                        "Reference"
                )
        ) {

            return INK;
        }

        if (
                category.equalsIgnoreCase(
                        "Fiction"
                )
        ) {

            return TERRACOTTA;
        }

        return GOLD;
    }


    // =========================================================
    // BOOK COLLECTION
    // =========================================================

    JPanel createBooksPage() {

        JPanel page =
                new JPanel(
                        new BorderLayout()
                );

        page.setBackground(CREAM);

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setOpaque(false);

        header.setBorder(
                new EmptyBorder(
                        30,
                        35,
                        18,
                        35
                )
        );

        JPanel titleBox =
                new JPanel();

        titleBox.setOpaque(false);

        titleBox.setLayout(
                new BoxLayout(
                        titleBox,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "Book Collection"
                );

        title.setFont(DISPLAY);

        title.setForeground(INK);

        JLabel sub =
                new JLabel(
                        books.size()
                                + " titles available"
                );

        sub.setFont(BODY);

        sub.setForeground(MUTED);

        titleBox.add(title);

        titleBox.add(
                Box.createVerticalStrut(4)
        );

        titleBox.add(sub);

        header.add(
                titleBox,
                BorderLayout.WEST
        );

        // Search box
        JPanel searchBox =
                new JPanel(
                        new BorderLayout()
                ) {

                    @Override
                    protected void paintComponent(
                            Graphics g) {

                        Graphics2D g2 =
                                (Graphics2D)
                                        g.create();

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        g2.setColor(WHITE);

                        g2.fillRoundRect(
                                0,
                                0,
                                getWidth() - 1,
                                getHeight() - 1,
                                14,
                                14
                        );

                        g2.setColor(SAND);

                        g2.drawRoundRect(
                                0,
                                0,
                                getWidth() - 1,
                                getHeight() - 1,
                                14,
                                14
                        );

                        g2.dispose();

                        super.paintComponent(g);
                    }
                };

        searchBox.setOpaque(false);

        searchBox.setPreferredSize(
                new Dimension(
                        280,
                        44
                )
        );

        searchBox.setBorder(
                new EmptyBorder(
                        0,
                        12,
                        0,
                        12
                )
        );

        JLabel searchIcon =
                new JLabel("⌕");

        searchIcon.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        22
                )
        );

        searchIcon.setForeground(MUTED);

        searchIcon.setBorder(
                new EmptyBorder(
                        0,
                        2,
                        1,
                        7
                )
        );

        JTextField search =
                new JTextField();

        search.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        search.setForeground(INK);

        search.setBackground(
                new Color(
                        255,
                        255,
                        255,
                        0
                )
        );

        search.setOpaque(false);

        search.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        0,
                        0,
                        0
                )
        );

        search.setToolTipText(
                "Search by title or author"
        );

        searchBox.add(
                searchIcon,
                BorderLayout.WEST
        );

        searchBox.add(
                search,
                BorderLayout.CENTER
        );

        header.add(
                searchBox,
                BorderLayout.EAST
        );

        page.add(
                header,
                BorderLayout.NORTH
        );

        JPanel grid =
                new JPanel(
                        new GridLayout(
                                0,
                                3,
                                15,
                                15
                        )
                );

        grid.setBackground(CREAM);

        grid.setBorder(
                new EmptyBorder(
                        0,
                        35,
                        30,
                        35
                )
        );

        for (Book book : books) {

            grid.add(
                    bookCard(book)
            );
        }

        search.addKeyListener(
                new KeyAdapter() {

                    public void keyReleased(
                            KeyEvent e) {

                        String query =
                                search.getText()
                                        .toLowerCase()
                                        .trim();

                        grid.removeAll();

                        for (Book book :
                                books) {

                            if (
                                    book.getTitle()
                                            .toLowerCase()
                                            .contains(query)
                                            ||
                                            book.getAuthor()
                                                    .toLowerCase()
                                                    .contains(query)
                            ) {

                                grid.add(
                                        bookCard(book)
                                );
                            }
                        }

                        grid.revalidate();

                        grid.repaint();
                    }
                }
        );

        // Make the search box feel active when clicked.
        search.addFocusListener(
                new FocusAdapter() {

                    public void focusGained(
                            FocusEvent e) {

                        searchBox.repaint();
                    }

                    public void focusLost(
                            FocusEvent e) {

                        searchBox.repaint();
                    }
                }
        );

        JScrollPane scroll =
                new JScrollPane(grid);

        scroll.setBorder(null);

        scroll.getVerticalScrollBar()
                .setUnitIncrement(16);

        page.add(
                scroll,
                BorderLayout.CENTER
        );

        return page;
    }


    JPanel bookCard(
            Book book) {

        GlassPanel card =
                new GlassPanel(
                        new Color(
                                255,
                                252,
                                246,
                                225
                        ),
                        new Color(
                                255,
                                255,
                                255,
                                170
                        )
                );

        card.setLayout(
                new BorderLayout()
        );

        JPanel cover =
                createCover(book);

        cover.setPreferredSize(
                new Dimension(
                        110,
                        170
                )
        );

        // Keep the cover at its real size instead of stretching
        // it to the full height of the book card.
        JPanel coverHolder =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                0,
                                0
                        )
                );

        coverHolder.setOpaque(false);

        coverHolder.setPreferredSize(
                new Dimension(
                        110,
                        170
                )
        );

        coverHolder.add(cover);

        card.add(
                coverHolder,
                BorderLayout.WEST
        );

        JPanel info =
                new JPanel();

        info.setOpaque(false);

        info.setLayout(
                new BoxLayout(
                        info,
                        BoxLayout.Y_AXIS
                )
        );

        info.setBorder(
                new EmptyBorder(
                        15,
                        15,
                        12,
                        10
                )
        );

        JLabel title =
                new JLabel(
                        "<html><div style='width:160px'>"
                                + book.getTitle()
                                + "</div></html>"
                );

        title.setFont(TITLE);

        title.setForeground(INK);

        JLabel author =
                new JLabel(
                        "<html><div style='width:160px'>"
                                + book.getAuthor()
                                + "</div></html>"
                );

        author.setFont(BODY);

        author.setForeground(MUTED);

        JLabel availability =
                new JLabel(
                        book.getAvailableCopies()
                                + " / "
                                + book.getTotalCopies()
                                + " available"
                );

        availability.setFont(BODY_BOLD);

        availability.setForeground(
                book.getAvailableCopies() > 0
                        ? SAGE
                        : TERRACOTTA
        );

        JButton details =
                smallButton(
                        "VIEW DETAILS"
                );

        details.addActionListener(
                e -> showBookDetails(book)
        );

        info.add(title);

        info.add(
                Box.createVerticalStrut(5)
        );

        info.add(author);

        info.add(
                Box.createVerticalStrut(10)
        );

        info.add(availability);

        info.add(
                Box.createVerticalStrut(12)
        );

        info.add(details);

        card.add(
                info,
                BorderLayout.CENTER
        );

        return card;
    }


    JButton smallButton(
            String text) {

        JButton button =
                new JButton(text);

        button.setFont(SMALL_BOLD);

        button.setForeground(INK);

        button.setBackground(
                new Color(
                        239,
                        230,
                        215
                )
        );

        button.setFocusPainted(false);

        button.setBorder(
                new EmptyBorder(
                        7,
                        10,
                        7,
                        10
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }


    void showBookDetails(Book book) {
        JDialog dialog = new JDialog(this, "Book Details", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(CREAM);
        
        JPanel content = new JPanel(new BorderLayout(30, 0));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JPanel cover = createCover(book);
        cover.setPreferredSize(new Dimension(160, 240));
        content.add(cover, BorderLayout.WEST);
        
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setOpaque(false);
        
        JLabel title = new JLabel(book.getTitle());
        title.setFont(DISPLAY);
        title.setForeground(INK);
        
        JLabel author = new JLabel("by " + book.getAuthor());
        author.setFont(SUBTITLE);
        author.setForeground(MUTED);
        
        JLabel cat = new JLabel(book.getCategory().toUpperCase());
        cat.setFont(SMALL_BOLD);
        
        JTextArea desc = new JTextArea("A comprehensive guide to " + book.getTitle() + ".\nThis book explores advanced concepts and foundational theories.");
        desc.setWrapStyleWord(true);
        desc.setLineWrap(true);
        desc.setOpaque(false);
        desc.setEditable(false);
        desc.setFont(BODY);
        desc.setForeground(MUTED);
        
        JPanel stats = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        stats.setOpaque(false);
        JLabel avail = new JLabel("Available: " + book.getAvailableCopies());
        JLabel issued = new JLabel("On Loan: " + book.getIssuedCopies());
        avail.setFont(BODY_BOLD); issued.setFont(BODY_BOLD);
        stats.add(avail); stats.add(issued);
        
        right.add(title);
        right.add(Box.createVerticalStrut(5));
        right.add(author);
        right.add(Box.createVerticalStrut(10));
        right.add(cat);
        right.add(Box.createVerticalStrut(20));
        right.add(desc);
        right.add(Box.createVerticalStrut(20));
        right.add(stats);
        
        content.add(right, BorderLayout.CENTER);
        main.add(content, BorderLayout.CENTER);
        
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        footer.setOpaque(false);
        
        JButton close = new JButton("Close");
        close.addActionListener(e -> dialog.dispose());
        
        JButton issue = actionButton("Issue this book →");
        issue.addActionListener(e -> {
            dialog.dispose();
            preselectedBook = book.getTitle();
            showPage("ISSUE");
        });
        
        footer.add(close);
        footer.add(issue);
        main.add(footer, BorderLayout.SOUTH);
        
        dialog.add(main);
        dialog.setVisible(true);
    }
    
    // =========================================================
    // STUDENTS
    // =========================================================

    JPanel createStudentsPage() {

        JPanel page =
                new JPanel(
                        new BorderLayout()
                );

        page.setBackground(CREAM);

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setOpaque(false);

        header.setBorder(
                new EmptyBorder(
                        30,
                        35,
                        18,
                        35
                )
        );

        JPanel titleBox =
                new JPanel();

        titleBox.setOpaque(false);

        titleBox.setLayout(
                new BoxLayout(
                        titleBox,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "Students"
                );

        title.setFont(DISPLAY);

        title.setForeground(INK);

        JLabel sub =
                new JLabel(
                        students.size()
                                + " registered students"
                );

        sub.setFont(BODY);

        sub.setForeground(MUTED);

        titleBox.add(title);

        titleBox.add(
                Box.createVerticalStrut(4)
        );

        titleBox.add(sub);

        header.add(
                titleBox,
                BorderLayout.WEST
        );

        // Student search box
        JPanel searchBox =
                new JPanel(
                        new BorderLayout()
                );

        searchBox.setPreferredSize(
                new Dimension(
                        300,
                        42
                )
        );

        searchBox.setBackground(PAPER);

        searchBox.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                SAND,
                                1
                        ),
                        new EmptyBorder(
                                0,
                                10,
                                0,
                                10
                        )
                )
        );

        JLabel searchIcon =
                new JLabel("⌕");

        searchIcon.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        20
                )
        );

        searchIcon.setForeground(MUTED);

        searchBox.add(
                searchIcon,
                BorderLayout.WEST
        );

        JTextField search =
                new JTextField();

        search.setFont(BODY);

        search.setForeground(INK);

        search.setBackground(PAPER);

        search.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        8,
                        0,
                        5
                )
        );

        search.setToolTipText(
                "Search by student name, ID or course"
        );

        final String placeholder =
                "Search students...";

        search.setText(placeholder);

        search.setForeground(MUTED);

        search.addFocusListener(
                new FocusAdapter() {

                    public void focusGained(
                            FocusEvent e) {

                        if (search.getText()
                                .equals(placeholder)) {

                            search.setText("");

                            search.setForeground(INK);
                        }
                    }

                    public void focusLost(
                            FocusEvent e) {

                        if (search.getText()
                                .trim()
                                .isEmpty()) {

                            search.setText(
                                    placeholder
                            );

                            search.setForeground(
                                    MUTED
                            );
                        }
                    }
                }
        );

        searchBox.add(
                search,
                BorderLayout.CENTER
        );

        header.add(
                searchBox,
                BorderLayout.EAST
        );

        page.add(
                header,
                BorderLayout.NORTH
        );

        JPanel list =
                new JPanel();

        list.setBackground(CREAM);

        list.setLayout(
                new BoxLayout(
                        list,
                        BoxLayout.Y_AXIS
                )
        );

        list.setBorder(
                new EmptyBorder(
                        0,
                        35,
                        30,
                        35
                )
        );

        for (Student student :
                students) {

            list.add(
                    studentCard(student)
            );

            list.add(
                    Box.createVerticalStrut(10)
            );
        }

        search.addKeyListener(
                new KeyAdapter() {

                    public void keyReleased(
                            KeyEvent e) {

                        String query =
                                search.getText()
                                        .toLowerCase()
                                        .trim();

                        if (query.equals(
                                placeholder.toLowerCase()
                        )) {
                            query = "";
                        }

                        list.removeAll();

                        for (Student student :
                                students) {

                            if (
                                    student.getName()
                                            .toLowerCase()
                                            .contains(query)
                                            ||
                                            student.getId()
                                                    .toLowerCase()
                                                    .contains(query)
                                            ||
                                            student.getCourse()
                                                    .toLowerCase()
                                                    .contains(query)
                                            ||
                                            student.getContact()
                                                    .toLowerCase()
                                                    .contains(query)
                            ) {

                                list.add(
                                        studentCard(
                                                student
                                        )
                                );

                                list.add(
                                        Box.createVerticalStrut(
                                                10
                                        )
                                );
                            }
                        }

                        list.revalidate();

                        list.repaint();
                    }
                }
        );

        JScrollPane scroll =
                new JScrollPane(list);

        scroll.setBorder(null);

        scroll.getVerticalScrollBar()
                .setUnitIncrement(16);

        page.add(
                scroll,
                BorderLayout.CENTER
        );

        return page;
    }


    JPanel studentCard(
            Student student) {

        GlassPanel card =
                new GlassPanel(
                        new Color(
                                255,
                                252,
                                246,
                                225
                        ),
                        new Color(
                                255,
                                255,
                                255,
                                170
                        )
                );

        card.setLayout(
                new BorderLayout()
        );

        JPanel avatar =
                new JPanel(
                        new GridBagLayout()
                );

        avatar.setPreferredSize(
                new Dimension(
                        75,
                        75
                )
        );

        avatar.setBackground(SAGE);

        String first =
                student.getName()
                        .substring(
                                0,
                                1
                        )
                        .toUpperCase();

        JLabel initial =
                new JLabel(first);

        initial.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        28
                )
        );

        initial.setForeground(WHITE);

        avatar.add(initial);

        card.add(
                avatar,
                BorderLayout.WEST
        );

        JPanel info =
                new JPanel();

        info.setOpaque(false);

        info.setLayout(
                new BoxLayout(
                        info,
                        BoxLayout.Y_AXIS
                )
        );

        info.setBorder(
                new EmptyBorder(
                        13,
                        16,
                        13,
                        10
                )
        );

        JLabel name =
                new JLabel(
                        student.getName()
                );

        name.setFont(TITLE);

        name.setForeground(INK);

        JLabel id =
                new JLabel(
                        "ID  •  "
                                + student.getId()
                );

        id.setFont(SMALL_BOLD);

        id.setForeground(TERRACOTTA);

        JLabel course =
                new JLabel(
                        student.getCourse()
                                + "  •  "
                                + student.getContact()
                );

        course.setFont(BODY);

        course.setForeground(MUTED);

        info.add(name);

        info.add(
                Box.createVerticalStrut(4)
        );

        info.add(id);

        info.add(
                Box.createVerticalStrut(6)
        );

        info.add(course);

        card.add(
                info,
                BorderLayout.CENTER
        );

        return card;
    }


    // =========================================================
    // ISSUE PAGE
    // =========================================================

    JPanel createIssuePage() {

        return transactionPage(
                "Issue a Book",
                "Create a new library issue record.",
                true
        );
    }


    // =========================================================
    // RETURN PAGE
    // =========================================================

    JPanel createReturnPage() {

        return transactionPage(
                "Return a Book",
                "Return the book and calculate its fine.",
                false
        );
    }


    // =========================================================
    // TRANSACTION PAGE
    // =========================================================

    JPanel transactionPage(String titleText, String subtitle, boolean issueMode) {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(CREAM);

        // HEADER
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(30, 40, 16, 40));
        
        JLabel title = new JLabel(titleText);
        title.setFont(DISPLAY);
        title.setForeground(INK);
        
        JLabel sub = new JLabel(subtitle);
        sub.setFont(BODY);
        sub.setForeground(MUTED);
        
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(sub);
        page.add(header, BorderLayout.NORTH);

        // FORM CARD
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(CREAM);
        content.setBorder(new EmptyBorder(0, 40, 25, 40));
        GridBagConstraints main = new GridBagConstraints();
        main.insets = new Insets(8, 8, 8, 8);
        main.fill = GridBagConstraints.BOTH;
        main.weighty = 0;

        GlassPanel formCard = new GlassPanel(new Color(255, 252, 246, 235), new Color(255, 255, 255, 180));
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel formTitle = new JLabel(issueMode ? "Issue details" : "Return details");
        formTitle.setFont(new Font("Serif", Font.BOLD, 22));
        formTitle.setForeground(INK);
        formCard.add(formTitle);
        formCard.add(Box.createVerticalStrut(17));

        JLabel studentLabel = new JLabel("Select Student");
        studentLabel.setFont(BODY_BOLD);
        studentLabel.setForeground(INK);
        formCard.add(studentLabel);
        formCard.add(Box.createVerticalStrut(5));

        JComboBox<String> studentCombo = new JComboBox<>();
        for (Student s : students) studentCombo.addItem(s.getId() + " - " + s.getName());
        studentCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        formCard.add(studentCombo);
        formCard.add(Box.createVerticalStrut(10));

        JLabel bookLabel = new JLabel("Select Book");
        bookLabel.setFont(BODY_BOLD);
        bookLabel.setForeground(INK);
        formCard.add(bookLabel);
        formCard.add(Box.createVerticalStrut(5));

        JComboBox<String> bookCombo = new JComboBox<>();
        for (Book b : books) bookCombo.addItem(b.getId() + " - " + b.getTitle());
        bookCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        formCard.add(bookCombo);
        formCard.add(Box.createVerticalStrut(10));

        JLabel daysLabel = new JLabel(issueMode ? "Issue Duration (Days)" : "Days Overdue (Auto-calculated)");
        daysLabel.setFont(BODY_BOLD);
        daysLabel.setForeground(INK);
        formCard.add(daysLabel);
        formCard.add(Box.createVerticalStrut(5));

        JSpinner daysSpinner = new JSpinner(new SpinnerNumberModel(issueMode ? 14 : 0, 0, 365, 1));
        daysSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        formCard.add(daysSpinner);
        formCard.add(Box.createVerticalStrut(15));

        JButton action = actionButton(issueMode ? "Issue Book" : "Return Book");
        action.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(action);
        
        // Add action listener
        action.addActionListener(e -> {
            String selS = (String) studentCombo.getSelectedItem();
            String selB = (String) bookCombo.getSelectedItem();
            if (selS == null || selB == null) return;
            String sid = selS.split(" - ")[0];
            String bid = selB.split(" - ")[0];
            // Mocking the text field to reuse existing logic
            JTextField sf = new JTextField(sid);
            JTextField bf = new JTextField(bid);
            JTextField df = new JTextField(daysSpinner.getValue().toString());
            if (issueMode) issueBook(sf, bf, df);
            else returnBook(sf, bf, df);
        });

        // LIVE PREVIEW CARD
        GlassPanel infoCard = new GlassPanel(INK, new Color(255, 255, 255, 45));
        infoCard.setLayout(new BoxLayout(infoCard, BoxLayout.Y_AXIS));
        infoCard.setBorder(new EmptyBorder(25, 28, 25, 28));

        JLabel infoTitle = new JLabel(issueMode ? "Live Issue Preview" : "Live Fine Preview");
        infoTitle.setFont(new Font("Serif", Font.BOLD, 23));
        infoTitle.setForeground(WHITE);
        infoCard.add(infoTitle);
        infoCard.add(Box.createVerticalStrut(14));
        
        JLabel finePreview = new JLabel("Total Fine: ₹0");
        finePreview.setFont(new Font("Inter", Font.BOLD, 24));
        finePreview.setForeground(TERRACOTTA);
        
        if (!issueMode) {
            infoCard.add(finePreview);
            infoCard.add(Box.createVerticalStrut(20));
            
            // FINE SLAB TABLE
            JPanel slabPanel = new JPanel(new GridLayout(3, 2, 5, 5));
            slabPanel.setOpaque(false);
            JLabel sTitle = new JLabel("<html><b>How Fines Work:</b></html>");
            sTitle.setForeground(GOLD);
            slabPanel.add(sTitle); slabPanel.add(new JLabel(""));
            JLabel s1 = new JLabel("1 - 7 Days:"); s1.setForeground(WHITE);
            JLabel s1r = new JLabel("₹5 / day"); s1r.setForeground(WHITE);
            slabPanel.add(s1); slabPanel.add(s1r);
            JLabel s2 = new JLabel("8+ Days:"); s2.setForeground(WHITE);
            JLabel s2r = new JLabel("₹10 / day"); s2r.setForeground(WHITE);
            slabPanel.add(s2); slabPanel.add(s2r);
            infoCard.add(slabPanel);

            // Live update
            daysSpinner.addChangeListener(e -> {
                int od = (Integer) daysSpinner.getValue();
                finePreview.setText("Total Fine: ₹" + (od * 5)); // Simplified fine calc
            });
        } else {
            JLabel msg = new JLabel("Due Date: Automatically calculated.");
            msg.setForeground(WHITE);
            infoCard.add(msg);
        }

        main.gridx = 0;
        main.gridy = 0;
        main.weightx = 0.5;
        content.add(formCard, main);

        main.gridx = 1;
        main.weightx = 0.4;
        main.insets = new Insets(8, 20, 8, 8);
        content.add(infoCard, main);

        page.add(content, BorderLayout.CENTER);
        return page;
    }
    // =========================================================
    // TRANSACTION STATS
    // =========================================================

    JPanel transactionStats(
            boolean issueMode) {

        int totalCopies = 0;
        int availableCopies = 0;
        int issuedCopies = 0;
        double totalFine = 0;

        for (Book book : books) {

            totalCopies +=
                    book.getTotalCopies();

            availableCopies +=
                    book.getAvailableCopies();
        }

        issuedCopies =
                totalCopies -
                        availableCopies;

        for (LibRecord record : records) {

            if (record.isReturned()) {

                totalFine +=
                        record.getFine();
            }
        }


        JPanel stats =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                12,
                                0
                        )
                );

        stats.setOpaque(false);


        if (issueMode) {

            stats.add(
                    statCard(
                            "AVAILABLE BOOKS",
                            String.valueOf(
                                    availableCopies
                            ),
                            "copies ready to issue",
                            SAGE
                    )
            );

            stats.add(
                    statCard(
                            "REGISTERED STUDENTS",
                            String.valueOf(
                                    students.size()
                            ),
                            "active library users",
                            TERRACOTTA
                    )
            );

            stats.add(
                    statCard(
                            "CURRENTLY ON LOAN",
                            String.valueOf(
                                    issuedCopies
                            ),
                            "books already issued",
                            GOLD
                    )
            );

        } else {

            stats.add(
                    statCard(
                            "CURRENTLY ON LOAN",
                            String.valueOf(
                                    issuedCopies
                            ),
                            "books awaiting return",
                            GOLD
                    )
            );

            stats.add(
                    statCard(
                            "AVAILABLE BOOKS",
                            String.valueOf(
                                    availableCopies
                            ),
                            "copies in the library",
                            SAGE
                    )
            );

            stats.add(
                    statCard(
                            "RECORDED FINES",
                            "₹"
                                    + String.format(
                                    "%.0f",
                                    totalFine
                            ),
                            "total returned-book fines",
                            TERRACOTTA
                    )
            );
        }

        return stats;
    }


    // =========================================================
    // FORM FIELD
    // =========================================================

    JTextField formField() {

        JTextField field =
                new JTextField();

        field.setPreferredSize(
                new Dimension(
                        360,
                        42
                )
        );

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        42
                )
        );

        field.setFont(BODY);

        field.setForeground(INK);

        field.setBackground(PAPER);

        field.setCaretColor(TERRACOTTA);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                SAND,
                                1
                        ),
                        new EmptyBorder(
                                0,
                                12,
                                0,
                                12
                        )
                )
        );

        return field;
    }


    // =========================================================
    // INFORMATION ROW
    // =========================================================

    void addInfoRow(
            JPanel panel,
            String number,
            String heading,
            String description) {

        JPanel row =
                new JPanel();

        row.setOpaque(false);

        row.setLayout(
                new BorderLayout()
        );

        row.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        56
                )
        );


        JLabel numberLabel =
                new JLabel(number);

        numberLabel.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        15
                )
        );

        numberLabel.setForeground(GOLD);

        numberLabel.setPreferredSize(
                new Dimension(
                        35,
                        30
                )
        );

        row.add(
                numberLabel,
                BorderLayout.WEST
        );


        JPanel text =
                new JPanel();

        text.setOpaque(false);

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel title =
                new JLabel(heading);

        title.setFont(BODY_BOLD);
        title.setForeground(WHITE);


        JLabel descriptionLabel =
                new JLabel(
                        "<html><div style='width:230px'>"
                                + description
                                + "</div></html>"
                );

        descriptionLabel.setFont(SMALL);

        descriptionLabel.setForeground(
                new Color(
                        215,
                        205,
                        195
                )
        );


        text.add(title);

        text.add(
                Box.createVerticalStrut(2)
        );

        text.add(descriptionLabel);


        row.add(
                text,
                BorderLayout.CENTER
        );


        panel.add(row);

        panel.add(
                Box.createVerticalStrut(7)
        );
    }


    // =========================================================
    // ACTION BUTTON
    // =========================================================

    JButton actionButton(
            String text) {

        JButton button =
                new JButton(text);

        button.setPreferredSize(
                new Dimension(
                        155,
                        44
                )
        );

        button.setMaximumSize(
                new Dimension(
                        155,
                        44
                )
        );

        button.setFont(BODY_BOLD);

        button.setForeground(WHITE);

        button.setBackground(
                TERRACOTTA
        );

        button.setOpaque(true);

        button.setContentAreaFilled(true);

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        18,
                        8,
                        18
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        button.addMouseListener(
                new MouseAdapter() {

                    public void mouseEntered(
                            MouseEvent e) {

                        button.setBackground(
                                new Color(
                                        160,
                                        78,
                                        60
                                )
                        );
                    }


                    public void mouseExited(
                            MouseEvent e) {

                        button.setBackground(
                                TERRACOTTA
                        );
                    }
                }
        );

        return button;
    }


    // =========================================================
    // ISSUE BOOK
    // =========================================================

    void issueBook(
            JTextField studentField,
            JTextField bookField,
            JTextField daysField) {

        String studentId =
                studentField.getText()
                        .trim();

        String bookId =
                bookField.getText()
                        .trim();

        String daysText =
                daysField.getText()
                        .trim();

        if (
                studentId.isEmpty()
                        ||
                        bookId.isEmpty()
                        ||
                        daysText.isEmpty()
        ) {

            warning(
                    "Please fill all fields."
            );

            return;
        }

        int allowedDays;

        try {

            allowedDays =
                    Integer.parseInt(
                            daysText
                    );

        } catch (NumberFormatException e) {

            warning(
                    "Allowed days must be a number."
            );

            return;
        }

        if (allowedDays <= 0) {

            warning(
                    "Allowed days must be greater than zero."
            );

            return;
        }

        Student student =
                findStudent(studentId);

        Book book =
                findBook(bookId);

        if (student == null) {

            warning(
                    "Student ID not found."
            );

            return;
        }

        if (book == null) {

            warning(
                    "Book ID not found."
            );

            return;
        }

        if (!book.isAvailable()) {

            warning(
                    "This book is currently unavailable."
            );

            return;
        }

        if (
                findActiveRecord(
                        studentId,
                        bookId
                ) != null
        ) {

            warning(
                    "This student already has this book issued."
            );

            return;
        }

        LibRecord record =
                new LibRecord(
                        student,
                        book,
                        allowedDays
                );

        records.add(record);

        book.issueCopy();

        saveData();

        JOptionPane.showMessageDialog(
                this,
                "BOOK ISSUED SUCCESSFULLY\n\n"
                        + "Student : "
                        + student.getName()
                        + "\nBook : "
                        + book.getTitle()
                        + "\nAllowed Days : "
                        + allowedDays,
                "MindSpace Library",
                JOptionPane.INFORMATION_MESSAGE
        );

        studentField.setText("");

        bookField.setText("");

        daysField.setText("");
    }


    // =========================================================
    // RETURN BOOK
    // =========================================================

    void returnBook(
            JTextField studentField,
            JTextField bookField,
            JTextField daysField) {

        String studentId =
                studentField.getText()
                        .trim();

        String bookId =
                bookField.getText()
                        .trim();

        String daysText =
                daysField.getText()
                        .trim();

        if (
                studentId.isEmpty()
                        ||
                        bookId.isEmpty()
                        ||
                        daysText.isEmpty()
        ) {

            warning(
                    "Please fill all fields."
            );

            return;
        }

        int actualDays;

        try {

            actualDays =
                    Integer.parseInt(
                            daysText
                    );

        } catch (NumberFormatException e) {

            warning(
                    "Actual days must be a number."
            );

            return;
        }

        if (actualDays <= 0) {

            warning(
                    "Actual days must be greater than zero."
            );

            return;
        }

        LibRecord record =
                findActiveRecord(
                        studentId,
                        bookId
                );

        if (record == null) {

            warning(
                    "No active issue record found."
            );

            return;
        }

        record.returnBook(
                actualDays
        );

        record.getBook().returnCopy();

        saveData();

        JOptionPane.showMessageDialog(
                this,
                "BOOK RETURNED\n\n"
                        + "Book : "
                        + record.getBook()
                        .getTitle()
                        + "\nActual Days : "
                        + actualDays
                        + "\nDelayed Days : "
                        + record.getDelayedDays()
                        + "\n\nFine : ₹"
                        + String.format(
                        "%.2f",
                        record.getFine()
                ),
                "Return Receipt",
                JOptionPane.INFORMATION_MESSAGE
        );

        studentField.setText("");

        bookField.setText("");

        daysField.setText("");
    }


    // =========================================================
    // FINE RECORDS
    // =========================================================

    JPanel createFinesPage() {

        JPanel page =
                new JPanel(
                        new BorderLayout()
                );

        page.setBackground(CREAM);

        JPanel header =
                new JPanel();

        header.setOpaque(false);

        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );

        header.setBorder(
                new EmptyBorder(
                        30,
                        35,
                        18,
                        35
                )
        );

        JLabel title =
                new JLabel(
                        "Fine Records"
                );

        title.setFont(DISPLAY);

        title.setForeground(INK);

        JLabel sub =
                new JLabel(
                        "Returned books, delayed days and recorded fines."
                );

        sub.setFont(BODY);

        sub.setForeground(MUTED);

        header.add(title);

        header.add(
                Box.createVerticalStrut(4)
        );

        header.add(sub);

        page.add(
                header,
                BorderLayout.NORTH
        );

        JPanel list =
                new JPanel();

        list.setBackground(CREAM);

        list.setLayout(
                new BoxLayout(
                        list,
                        BoxLayout.Y_AXIS
                )
        );

        list.setBorder(
                new EmptyBorder(
                        0,
                        35,
                        30,
                        35
                )
        );

        double totalFine = 0;

        int returned = 0;

        int overdue = 0;

        for (LibRecord record :
                records) {

            if (record.isReturned()) {

                returned++;

                totalFine +=
                        record.getFine();

                if (
                        record.getDelayedDays()
                                > 0
                ) {

                    overdue++;
                }
            }
        }

        JPanel stats =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                12,
                                0
                        )
                );

        stats.setOpaque(false);

        stats.add(
                statCard(
                        "TOTAL FINE",
                        "₹"
                                + String.format(
                                "%.2f",
                                totalFine
                        ),
                        "recorded amount",
                        TERRACOTTA
                )
        );

        stats.add(
                statCard(
                        "OVERDUE",
                        String.valueOf(
                                overdue
                        ),
                        "late returns",
                        GOLD
                )
        );

        stats.add(
                statCard(
                        "RETURNED",
                        String.valueOf(
                                returned
                        ),
                        "completed records",
                        SAGE
                )
        );

        list.add(stats);

        list.add(
                Box.createVerticalStrut(15)
        );

        for (LibRecord record :
                records) {

            if (record.isReturned()) {

                list.add(
                        fineCard(record)
                );

                list.add(
                        Box.createVerticalStrut(10)
                );
            }
        }

        JScrollPane scroll =
                new JScrollPane(list);

        scroll.setBorder(null);

        scroll.getVerticalScrollBar()
                .setUnitIncrement(16);

        page.add(
                scroll,
                BorderLayout.CENTER
        );

        return page;
    }


    JPanel fineCard(
            LibRecord record) {

        GlassPanel card =
                new GlassPanel(
                        new Color(
                                255,
                                252,
                                246,
                                225
                        ),
                        new Color(
                                255,
                                255,
                                255,
                                170
                        )
                );

        card.setLayout(
                new BorderLayout()
        );

        JPanel info =
                new JPanel();

        info.setOpaque(false);

        info.setLayout(
                new BoxLayout(
                        info,
                        BoxLayout.Y_AXIS
                )
        );

        info.setBorder(
                new EmptyBorder(
                        15,
                        18,
                        15,
                        10
                )
        );

        JLabel book =
                new JLabel(
                        record.getBook()
                                .getTitle()
                );

        book.setFont(TITLE);

        book.setForeground(INK);

        JLabel student =
                new JLabel(
                        "Student  •  "
                                + record.getStudent()
                                .getName()
                );

        student.setFont(BODY);

        student.setForeground(MUTED);

        JLabel delay =
                new JLabel(
                        "Delayed  •  "
                                + record.getDelayedDays()
                                + " days"
                );

        delay.setFont(SMALL_BOLD);

        delay.setForeground(
                record.getDelayedDays() > 0
                        ? TERRACOTTA
                        : SAGE
        );

        info.add(book);

        info.add(
                Box.createVerticalStrut(4)
        );

        info.add(student);

        info.add(
                Box.createVerticalStrut(7)
        );

        info.add(delay);

        card.add(
                info,
                BorderLayout.CENTER
        );

        JPanel amount =
                new JPanel(
                        new GridBagLayout()
                );

        amount.setPreferredSize(
                new Dimension(
                        125,
                        85
                )
        );

        amount.setBackground(
                record.getFine() > 0
                        ? new Color(
                        250,
                        237,
                        232
                )
                        : new Color(
                        237,
                        245,
                        234
                )
        );

        JLabel fine =
                new JLabel(
                        "₹"
                                + String.format(
                                "%.2f",
                                record.getFine()
                        )
                );

        fine.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        24
                )
        );

        fine.setForeground(
                record.getFine() > 0
                        ? TERRACOTTA
                        : SAGE
        );

        amount.add(fine);

        card.add(
                amount,
                BorderLayout.EAST
        );

        return card;
    }


    // =========================================================
    // FIND METHODS
    // =========================================================

    Book findBook(
            String id) {

        for (Book book : books) {

            if (
                    book.getId()
                            .equalsIgnoreCase(id)
            ) {

                return book;
            }
        }

        return null;
    }


    Student findStudent(
            String id) {

        for (Student student :
                students) {

            if (
                    student.getId()
                            .equalsIgnoreCase(id)
            ) {

                return student;
            }
        }

        return null;
    }


    LibRecord findActiveRecord(
            String studentId,
            String bookId) {

        for (LibRecord record :
                records) {

            if (
                    record.getStudent()
                            .getId()
                            .equalsIgnoreCase(
                                    studentId
                            )
                            &&
                            record.getBook()
                                    .getId()
                                    .equalsIgnoreCase(
                                            bookId
                                    )
                            &&
                            !record.isReturned()
            ) {

                return record;
            }
        }

        return null;
    }


    // =========================================================
    // WARNING
    // =========================================================

    void warning(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "MindSpace Library",
                JOptionPane.WARNING_MESSAGE
        );
    }


    // =========================================================
    // GLASS PANEL
    // =========================================================

    static class GlassPanel
            extends JPanel {

        private Color fill;

        private Color borderColor;

        GlassPanel(
                Color fill,
                Color borderColor) {

            this.fill = fill;

            this.borderColor =
                    borderColor;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(
                Graphics g) {

            Graphics2D g2 =
                    (Graphics2D)
                            g.create();

            g2.setRenderingHint(
                    RenderingHints
                            .KEY_ANTIALIASING,
                    RenderingHints
                            .VALUE_ANTIALIAS_ON
            );

            int width =
                    getWidth();

            int height =
                    getHeight();

            // soft shadow

            g2.setColor(
                    new Color(
                            60,
                            45,
                            35,
                            16
                    )
            );

            g2.fillRoundRect(
                    3,
                    4,
                    width - 6,
                    height - 5,
                    18,
                    18
            );

            // glass background

            g2.setColor(fill);

            g2.fillRoundRect(
                    0,
                    0,
                    width - 1,
                    height - 1,
                    18,
                    18
            );

            // border

            g2.setColor(
                    borderColor
            );

            g2.drawRoundRect(
                    0,
                    0,
                    width - 1,
                    height - 1,
                    18,
                    18
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args) {

        SwingUtilities.invokeLater(
                GUI::new
        );
    }

    // =========================================================
    // TOAST NOTIFICATIONS & BADGES
    // =========================================================

    public void showToast(String message, Color color) {
        JWindow toast = new JWindow(this);
        toast.setBackground(new Color(0, 0, 0, 0));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(33, 33, 33, 230));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(12, 24, 12, 24)
        ));
        
        JLabel label = new JLabel(message);
        label.setFont(new Font("Inter", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        panel.add(label, BorderLayout.CENTER);
        
        toast.add(panel);
        toast.pack();
        
        // Position at top right
        Point loc = this.getLocation();
        toast.setLocation(loc.x + this.getWidth() - toast.getWidth() - 30, loc.y + 40);
        
        toast.setOpacity(0.0f);
        toast.setVisible(true);
        
        // Fade in
        Timer fadeIn = new Timer(20, null);
        fadeIn.addActionListener(new ActionListener() {
            float opacity = 0.0f;
            public void actionPerformed(ActionEvent e) {
                opacity += 0.1f;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    fadeIn.stop();
                    // Start fade out after 2.5 seconds
                    Timer fadeOut = new Timer(20, null);
                    fadeOut.setInitialDelay(2500);
                    fadeOut.addActionListener(new ActionListener() {
                        float outOpacity = 1.0f;
                        public void actionPerformed(ActionEvent ev) {
                            outOpacity -= 0.1f;
                            if (outOpacity <= 0.0f) {
                                fadeOut.stop();
                                toast.dispose();
                            } else {
                                toast.setOpacity(outOpacity);
                            }
                        }
                    });
                    fadeOut.start();
                }
                toast.setOpacity(opacity);
            }
        });
        fadeIn.start();
    }
}