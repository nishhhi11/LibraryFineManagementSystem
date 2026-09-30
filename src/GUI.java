import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GUI extends JFrame {
    boolean suppressDialogs = false;

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

    static final Color MUTED = new Color(100, 90, 80);

    static final Color WHITE =
            Color.WHITE;

    // ================= FONTS =================

    static Font customSerif;
        static {
        try {
            customSerif = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("assets/fonts/PlayfairDisplay-Bold.ttf"));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customSerif);
        } catch (Exception e) {
            customSerif = new Font("Georgia", Font.BOLD, 12);
        }
    }

    static final Font DISPLAY =
            customSerif.deriveFont(
                    Font.BOLD,
                    31f
            );

    static final Font TITLE =
            customSerif.deriveFont(
                    Font.BOLD,
                    21f
            );

    static final Font SUBTITLE =
            customSerif.deriveFont(
                    Font.BOLD,
                    16f
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

    JPanel sidebar;
    String activePage = "HOME";
    JPanel createSidebar() {

        sidebar =
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
                customSerif.deriveFont(Font.BOLD, 26f)
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

        addNavigation(top, "⌂", "Overview", "HOME");

        addNavigation(top, "📚", "Book Collection", "BOOKS");

        addNavigation(top, "👤", "Students", "STUDENTS");

        addNavigation(top, "🔖", "Issue Book", "ISSUE");

        addNavigation(top, "↩️", "Return Book", "RETURN");

        addNavigation(top, "🧾", "Fine Records", "FINES");

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


    void addNavigation(JPanel parent, String icon, String text, String page) {
        JButton button = navButton(icon, text, page);
        parent.add(button);
        parent.add(Box.createVerticalStrut(2));
    }


    JButton navButton(String icon, String text, String page) {
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

        JLabel label = new JLabel(icon + "  " + text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (activePage != null && activePage.equals(page)) {
                    setForeground(WHITE);
                } else {
                    setForeground(new Color(230, 222, 212));
                }
                super.paintComponent(g);
            }
        };
        label.setFont(BODY_BOLD);
        button.add(label);

        button.setActionCommand(page);
        
        button.addActionListener(
                e -> showPage(page)
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
                        "MindSpace Library"
                );

        title.setFont(
                customSerif.deriveFont(Font.BOLD, 18f)
        );

        title.setForeground(INK);

        left.add(title);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
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
        right.add(profile);

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


    void showPage(String page) {
        activePage = page;
        loadData();
        rebuildPages();
        if (sidebar != null) {
            updateSidebar(sidebar, page);
            sidebar.repaint();
        }
        cardLayout.show(pages, page);
    }
    
    private void updateSidebar(java.awt.Container c, String page) {
        for (java.awt.Component comp : c.getComponents()) {
            if (comp instanceof javax.swing.JButton) {
                javax.swing.JButton b = (javax.swing.JButton) comp;
                String actionCmd = b.getActionCommand();
                if (actionCmd != null && !actionCmd.isEmpty()) {
                    boolean isActive = actionCmd.equals(page);
                    b.setBackground(isActive ? new Color(72, 58, 49) : ESPRESSO);
                    for (java.awt.Component child : b.getComponents()) {
                        if (child instanceof javax.swing.JLabel) {
                            child.setForeground(isActive ? WHITE : new Color(230, 222, 212));
                        }
                    }
                }
            } else if (comp instanceof java.awt.Container) {
                updateSidebar((java.awt.Container) comp, page);
            }
        }
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

        int hour = java.time.LocalTime.now().getHour();
        String greeting = "Good evening";
        if (hour >= 5 && hour < 12) greeting = "Good morning";
        else if (hour >= 12 && hour < 17) greeting = "Good afternoon";
        
        JLabel title =
                new JLabel(
                        greeting + ", Librarian."
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

        hero.setPreferredSize(new Dimension(0, 75));

        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        hero.setBorder(
                new EmptyBorder(12, 27, 12, 27)
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
                        "MINDSPACE / LIBRARY MANAGEMENT"
                );

        eyebrow.setFont(SMALL_BOLD);

        eyebrow.setForeground(GOLD);

        JLabel heroTitle =
                new JLabel(
                        "Read. Learn. Return."
                );

        heroTitle.setFont(
                customSerif.deriveFont(Font.BOLD, 31f)
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

        hero.setAlignmentX(Component.LEFT_ALIGNMENT);
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
        stats.setAlignmentX(Component.LEFT_ALIGNMENT);

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
                                + (int) totalFine,
                        "recorded amount",
                        TERRACOTTA
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

        collectionHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
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
        bookRow.setAlignmentX(Component.LEFT_ALIGNMENT);

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
                Component.LEFT_ALIGNMENT
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
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton browse =
                dashboardAction("Browse Collection", "Explore books and availability", "📚", false);

        JButton issue =
                dashboardAction("Issue a Book", "Create a new issue record", "🔖", true);

        JButton returnBook =
                dashboardAction("Return a Book", "Calculate delayed days and fine", "↩️", false);

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
        bottomWidgets.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomWidgets.setBorder(new EmptyBorder(0, 0, 40, 0)); // 40px bottom margin
        
        // 1. Activity Row
        GlassPanel activityPanel = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170));
        activityPanel.setLayout(new BoxLayout(activityPanel, BoxLayout.Y_AXIS));
        activityPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JPanel actHeader = new JPanel(new BorderLayout());
        actHeader.setOpaque(false);
        JLabel actTitle = new JLabel("Recent Activity");
        actTitle.setFont(TITLE); actTitle.setForeground(INK);
        JLabel actSub = new JLabel("Latest 5");
        actSub.setFont(SMALL); actSub.setForeground(MUTED);
        actHeader.add(actTitle, BorderLayout.WEST);
        actHeader.add(actSub, BorderLayout.EAST);
        activityPanel.add(actHeader);
        
        activityPanel.add(Box.createVerticalStrut(10));
        if (records.isEmpty()) {
            JLabel empty = new JLabel("No activity yet.");
            empty.setFont(SMALL); empty.setForeground(MUTED);
            activityPanel.add(empty);
        } else {
            for (int i = records.size() - 1; i >= Math.max(0, records.size() - 5); i--) {
                LibRecord r = records.get(i);
                JPanel row = new JPanel(new BorderLayout());
                row.setOpaque(false);
                String actionStr = r.isReturned() ? "returned" : "borrowed";
                String timeStr = " · " + ((records.size() - i) * 2) + "h ago";
                JLabel text = new JLabel("<html><b>" + r.getStudent().getName().split(" ")[0] + "</b> " + actionStr + " " + r.getBook().getTitle() + timeStr + "</html>");
                text.setFont(SMALL); text.setForeground(INK);
                row.add(text, BorderLayout.WEST);
                if (r.isReturned() && r.getFine() > 0) {
                    JLabel badge = new JLabel(" ₹" + (int) r.getFine() + " ");
                    badge.setOpaque(true); badge.setBackground(TERRACOTTA); badge.setForeground(WHITE); badge.setFont(SMALL_BOLD);
                    row.add(badge, BorderLayout.EAST);
                }
                activityPanel.add(row);
                activityPanel.add(Box.createVerticalStrut(5));
            }
        }
        bottomWidgets.add(activityPanel);

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

        
        
        
        // 3. Category Breakdown
        GlassPanel chartPanel = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int size = Math.min(getWidth(), getHeight()) - 70;
                int x = 20;
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
                g2.setColor(new Color(255, 252, 246)); g2.fillOval(x + 20, y + 20, size - 40, size - 40);
                
                // Draw total in center
                g2.setColor(INK);
                g2.setFont(BODY_BOLD);
                FontMetrics fm = g2.getFontMetrics();
                String t1 = total + "";
                String t2 = "titles";
                g2.drawString(t1, x + size/2 - fm.stringWidth(t1)/2, y + size/2 - 2);
                g2.setFont(SMALL);
                FontMetrics fm2 = g2.getFontMetrics();
                g2.drawString(t2, x + size/2 - fm2.stringWidth(t2)/2, y + size/2 + 12);
                
                // Draw legend
                int lx = x + size + 20;
                int ly = y + 20;
                g2.setFont(SMALL);
                
                g2.setColor(SAGE); g2.fillRoundRect(lx, ly, 10, 10, 4, 4);
                g2.setColor(INK); g2.drawString("Programming", lx + 18, ly + 9);
                
                ly += 25;
                g2.setColor(GOLD); g2.fillRoundRect(lx, ly, 10, 10, 4, 4);
                g2.setColor(INK); g2.drawString("Databases", lx + 18, ly + 9);
                
                ly += 25;
                g2.setColor(TERRACOTTA); g2.fillRoundRect(lx, ly, 10, 10, 4, 4);
                g2.setColor(INK); g2.drawString("Networking", lx + 18, ly + 9);
                
                g2.dispose();
            }
        };
        chartPanel.setLayout(new BorderLayout());
        chartPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JPanel chartHeader = new JPanel(new BorderLayout());
        chartHeader.setOpaque(false);
        JLabel cTitle = new JLabel("Categories");
        cTitle.setFont(TITLE); cTitle.setForeground(INK);
        JLabel cSub = new JLabel("By title count");
        cSub.setFont(SMALL); cSub.setForeground(MUTED);
        chartHeader.add(cTitle, BorderLayout.WEST);
        chartHeader.add(cSub, BorderLayout.EAST);
        chartPanel.add(chartHeader, BorderLayout.NORTH);
        
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

    JPanel statCard(String heading, String value, String caption, Color accent) {
        return statCard(heading, value, caption, accent, null);
    }

    JPanel statCard(String heading, String value, String caption, Color accent, String icon) {

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

        JLabel h = new JLabel(icon != null ? icon + "  " + heading : heading);

        h.setFont(SMALL_BOLD);

        h.setForeground(MUTED);

        JLabel v =
                new JLabel(
                        value
                );

        v.setFont(
                customSerif.deriveFont(Font.BOLD, 27f)
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
        card.setLayout(new BorderLayout());

        JPanel cover = createCover(book);
        cover.setPreferredSize(new Dimension(72, 105));
        card.add(cover, BorderLayout.WEST);

        JPanel info = new JPanel(new BorderLayout());
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(11, 12, 9, 7));

        JPanel topInfo = new JPanel();
        topInfo.setOpaque(false);
        topInfo.setLayout(new BoxLayout(topInfo, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("<html><div style='width:125px'>" + book.getTitle() + "</div></html>");
        title.setFont(SUBTITLE);
        title.setForeground(INK);

        JLabel author = new JLabel("<html><div style='width:125px'>" + book.getAuthor() + "</div></html>");
        author.setFont(SMALL);
        author.setForeground(MUTED);

        topInfo.add(title);
        topInfo.add(Box.createVerticalStrut(3));
        topInfo.add(author);
        
        JPanel bottomInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bottomInfo.setOpaque(false);

        JLabel status = new JLabel(book.getAvailableCopies() + " of " + book.getTotalCopies() + " available");
        status.setFont(SMALL_BOLD);
        status.setForeground(book.getAvailableCopies() > 0 ? SAGE : TERRACOTTA);
        
        JLabel cat = new JLabel(" • " + book.getCategory());
        cat.setFont(SMALL);
        cat.setForeground(MUTED);
        
        bottomInfo.add(status);
        bottomInfo.add(cat);

        info.add(topInfo, BorderLayout.NORTH);
        info.add(bottomInfo, BorderLayout.SOUTH);

        card.add(info, BorderLayout.CENTER);

        return card;
    }


    // =========================================================
    // QUICK ACTION CARD
    // =========================================================

    JButton dashboardAction(String title, String subtitle, String iconStr, boolean isPrimary) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        GlassPanel glass = new GlassPanel(
                isPrimary ? TERRACOTTA : new Color(255, 252, 246, 225),
                new Color(255, 255, 255, 170)
        );
        glass.setLayout(new BorderLayout());
        glass.setBorder(new EmptyBorder(12, 17, 12, 17));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(iconStr + " " + title);
        titleLabel.setFont(SUBTITLE);
        titleLabel.setForeground(isPrimary ? WHITE : INK);

        JLabel sub = new JLabel(subtitle);
        sub.setFont(SMALL);
        sub.setForeground(isPrimary ? new Color(255,255,255,200) : MUTED);

        content.add(titleLabel);
        content.add(Box.createVerticalStrut(3));
        content.add(sub);
        
        JLabel arrow = new JLabel(">");
        arrow.setFont(TITLE);
        arrow.setForeground(isPrimary ? WHITE : MUTED);
        
        glass.add(content, BorderLayout.CENTER);
        glass.add(arrow, BorderLayout.EAST);
        button.add(glass, BorderLayout.CENTER);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                glass.setBorder(new javax.swing.border.CompoundBorder(
                        new javax.swing.border.LineBorder(new Color(210, 190, 165), 1),
                        new EmptyBorder(11, 16, 11, 16)
                ));
                button.setLocation(button.getX(), button.getY() - 2);
                glass.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                glass.setBorder(new EmptyBorder(12, 17, 12, 17));
                button.setLocation(button.getX(), button.getY() + 2);
                glass.repaint();
            }
        });

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

                        int pad = 6;
                        int pw = panelWidth - pad*2;
                        int ph = panelHeight - pad*2;
                        // Soft shadow
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
                        g2.drawRoundRect(pad, pad, pw, ph, 12, 12);
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

        JButton addBookBtn = new JButton("+ Add Book");
        addBookBtn.setFont(SMALL_BOLD);
        addBookBtn.setForeground(WHITE);
        addBookBtn.setBackground(INK);
        addBookBtn.setFocusPainted(false);
        addBookBtn.setBorder(new EmptyBorder(10, 15, 10, 15));
        addBookBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actionsPanel.add(addBookBtn);

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

    JPanel bookCard(Book book) {
        // We use an OverlayLayout to put the hover overlay on top of the card content
        JPanel container = new JPanel();
        container.setLayout(new OverlayLayout(container));
        container.setOpaque(false);
        
        // 1. Overlay Panel
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

        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setBackground(CREAM);
        listWrapper.add(list, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(listWrapper);

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
                customSerif.deriveFont(Font.BOLD, 28f)
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
        main.fill = GridBagConstraints.HORIZONTAL;
        main.anchor = GridBagConstraints.NORTH;
        main.weighty = 0.0;

        GlassPanel formCard = new GlassPanel(new Color(255, 252, 246, 235), new Color(255, 255, 255, 180));
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel formTitle = new JLabel(issueMode ? "Issue details" : "Return details");
        formTitle.setFont(TITLE);
        formTitle.setForeground(INK);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(formTitle);
        formCard.add(Box.createVerticalStrut(17));

        JLabel studentLabel = new JLabel("Select Student");
        studentLabel.setFont(BODY_BOLD);
        studentLabel.setForeground(INK);
        studentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(studentLabel);
        formCard.add(Box.createVerticalStrut(5));

        ArrayList<String> studItems = new ArrayList<>();
        for (Student s : students) studItems.add(s.getId() + " · " + s.getName() + " · " + s.getCourse());
        JComboBox<String> studentCombo = createSearchableCombo(studItems, "Select Student...");
        studentCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        studentCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(studentCombo);
        formCard.add(Box.createVerticalStrut(15));

        JLabel bookLabel = new JLabel("Select Book");
        bookLabel.setFont(BODY_BOLD);
        bookLabel.setForeground(INK);
        bookLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(bookLabel);
        formCard.add(Box.createVerticalStrut(5));

        ArrayList<String> bookItems = new ArrayList<>();
        for (Book b : books) bookItems.add(b.getId() + " · " + b.getTitle() + " · " + b.getAuthor());
        JComboBox<String> bookCombo = createSearchableCombo(bookItems, "Select Book...");
        bookCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        bookCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(bookCombo);
        formCard.add(Box.createVerticalStrut(15));

        JLabel daysLabel = new JLabel(issueMode ? "Issue Duration (Days)" : "Days Overdue (Auto-calculated)");
        daysLabel.setFont(BODY_BOLD);
        daysLabel.setForeground(INK);
        daysLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(daysLabel);
        formCard.add(Box.createVerticalStrut(5));

        // Chips for days
        JPanel daysPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        daysPanel.setOpaque(false);
        daysPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        final int[] selectedDays = {issueMode ? 14 : 0};
        int[] options = {7, 14, 21, 30};
        ArrayList<JButton> chipBtns = new ArrayList<>();
        
        JSpinner daysSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 365, 1));
        
        if (issueMode) {
            for (int d : options) {
                JButton chip = new JButton(d + " days");
                chip.setFont(SMALL_BOLD);
                chip.setFocusPainted(false);
                chip.setCursor(new Cursor(Cursor.HAND_CURSOR));
                chip.setPreferredSize(new Dimension(80, 36));
                
                if (d == 14) {
                    chip.setBackground(TERRACOTTA);
                    chip.setForeground(WHITE);
                } else {
                    chip.setBackground(WHITE);
                    chip.setForeground(MUTED);
                }
                
                chipBtns.add(chip);
                daysPanel.add(chip);
            }
        } else {
            daysSpinner.setPreferredSize(new Dimension(100, 36));
            daysSpinner.setFont(BODY);
            daysPanel.add(daysSpinner);
        }
        
        formCard.add(daysPanel);
        formCard.add(Box.createVerticalStrut(25));

        if (!issueMode) {
            JLabel conditionLabel = new JLabel("Condition on Return");
            conditionLabel.setFont(BODY_BOLD);
            conditionLabel.setForeground(INK);
            conditionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            formCard.add(conditionLabel);
            formCard.add(Box.createVerticalStrut(5));
            
            JComboBox<String> conditionCombo = new JComboBox<>(new String[]{"Good", "Damaged (₹150 charge)", "Lost (Full price charge)"});
            conditionCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            conditionCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
            conditionCombo.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
                @Override
                protected JButton createArrowButton() {
                    JButton btn = new JButton("▼");
                    btn.setBorderPainted(false);
                    btn.setContentAreaFilled(false);
                    btn.setFocusPainted(false);
                    btn.setFont(new Font("SansSerif", Font.BOLD, 10));
                    btn.setForeground(new Color(120, 110, 100));
                    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    return btn;
                }
            });
            formCard.add(conditionCombo);
            formCard.add(Box.createVerticalStrut(25));
        }
        
        JButton action = actionButton(issueMode ? "Issue Book" : "Return Book");
        action.setAlignmentX(Component.LEFT_ALIGNMENT);
        action.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        action.setEnabled(false);
        
        // Add hover lift to primary button
        action.addMouseListener(new MouseAdapter() {
            Border normalBorder = new EmptyBorder(12, 0, 12, 0);
            Border liftBorder = new EmptyBorder(10, 0, 14, 0); // visually pushes up
            public void mouseEntered(MouseEvent e) { if(action.isEnabled()) action.setBorder(liftBorder); }
            public void mouseExited(MouseEvent e) { action.setBorder(normalBorder); }
        });
        
        formCard.add(action);
        
        formCard.add(Box.createVerticalStrut(30));
        
        // RECENT ISSUES
        JPanel recentPanel = new JPanel();
        recentPanel.setLayout(new BoxLayout(recentPanel, BoxLayout.Y_AXIS));
        recentPanel.setOpaque(false);
        recentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel recentTitle = new JLabel("Recent Issues");
        recentTitle.setFont(BODY_BOLD);
        recentTitle.setForeground(INK);
        recentPanel.add(recentTitle);
        recentPanel.add(Box.createVerticalStrut(10));
        
        int count = 0;
        for (int i = records.size() - 1; i >= 0; i--) {
            LibRecord r = records.get(i);
            if (!r.isReturned()) {
                JLabel row = new JLabel("• " + r.getBook().getTitle() + " to " + r.getStudent().getName());
                row.setFont(SMALL);
                row.setForeground(MUTED);
                recentPanel.add(row);
                recentPanel.add(Box.createVerticalStrut(6));
                count++;
                if (count == 3) break;
            }
        }
        formCard.add(recentPanel);

        // LIVE PREVIEW CARD
        GlassPanel infoCard = new GlassPanel(INK, new Color(255, 255, 255, 45));
        infoCard.setLayout(new BorderLayout());
        infoCard.setBorder(new EmptyBorder(25, 28, 25, 28));
        
        Runnable updatePreview = () -> {
            infoCard.removeAll();
            
            String selS = (String) studentCombo.getSelectedItem();
            String selB = (String) bookCombo.getSelectedItem();
            
            boolean validS = selS != null && !selS.startsWith("Select") && selS.contains(" · ");
            boolean validB = selB != null && !selB.startsWith("Select") && selB.contains(" · ");
            
            if (!validS || !validB) {
                action.setEnabled(false);
                action.setText(issueMode ? "Issue Book" : "Return Book");
                
                JPanel emptyState = new JPanel();
                emptyState.setLayout(new BoxLayout(emptyState, BoxLayout.Y_AXIS));
                emptyState.setOpaque(false);
                
                JLabel emptyIcon = new JLabel("📚");
                emptyIcon.setFont(DISPLAY.deriveFont(48f));
                emptyIcon.setForeground(new Color(255, 255, 255, 50));
                emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                JLabel emptyText = new JLabel("Select a student and a book to preview");
                emptyText.setFont(BODY);
                emptyText.setForeground(new Color(255, 255, 255, 120));
                emptyText.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                emptyState.add(Box.createVerticalStrut(80));
                emptyState.add(emptyIcon);
                emptyState.add(Box.createVerticalStrut(16));
                emptyState.add(emptyText);
                
                infoCard.add(emptyState, BorderLayout.CENTER);
            } else {
                String sid = selS.split(" · ")[0];
                String bid = selB.split(" · ")[0];
                
                Student student = null;
                for (Student s : students) if (s.getId().equals(sid)) student = s;
                Book book = null;
                for (Book b : books) if (b.getId().equals(bid)) book = b;
                
                if (student == null || book == null) return;
                
                JPanel livePanel = new JPanel();
                livePanel.setLayout(new BoxLayout(livePanel, BoxLayout.Y_AXIS));
                livePanel.setOpaque(false);
                
                JLabel infoTitle = new JLabel(issueMode ? "Live Issue Preview" : "Live Fine Preview");
                infoTitle.setFont(TITLE);
                infoTitle.setForeground(WHITE);
                infoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                livePanel.add(infoTitle);
                livePanel.add(Box.createVerticalStrut(20));
                
                int activeBooks = 0;
                double finesDue = 0.0;
                for (LibRecord r : records) {
                    if (r.getStudent().getId().equals(student.getId())) {
                        if (!r.isReturned()) activeBooks++;
                        else finesDue += r.getFine();
                    }
                }
                
                boolean canIssue = true;
                String warningMsg = "";
                
                if (issueMode) {
                    if (activeBooks >= 3) {
                        warningMsg = "⚠ Borrowing limit reached (3 books)";
                        canIssue = false;
                    } else if (finesDue > 0) {
                        warningMsg = "⚠ Cannot issue: Student has unpaid fines";
                        canIssue = false;
                    } else if (book.getAvailableCopies() <= 0) {
                        warningMsg = "⚠ Book is out of stock";
                        canIssue = false;
                    }
                } else {
                    LibRecord active = null;
                    for (LibRecord r : records) {
                        if (!r.isReturned() && r.getStudent().getId().equals(student.getId()) && r.getBook().getId().equals(book.getId())) {
                            active = r;
                            break;
                        }
                    }
                    if (active == null) {
                        warningMsg = "⚠ This book is not issued to this student";
                        canIssue = false;
                    }
                }
                
                action.setEnabled(canIssue);
                action.setText(canIssue ? (issueMode ? "Issue Book" : "Return Book") : "Unavailable");
                
                if (!warningMsg.isEmpty()) {
                    JPanel warnBox = new JPanel(new BorderLayout());
                    warnBox.setOpaque(false);
                    warnBox.setAlignmentX(Component.LEFT_ALIGNMENT);
                    JLabel warn = new JLabel(warningMsg);
                    warn.setFont(BODY_BOLD);
                    warn.setForeground(TERRACOTTA);
                    warnBox.add(warn, BorderLayout.WEST);
                    livePanel.add(warnBox);
                    livePanel.add(Box.createVerticalStrut(15));
                }
                
                JPanel bookRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
                bookRow.setOpaque(false);
                bookRow.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                JPanel cover = createCover(book);
                cover.setPreferredSize(new Dimension(60, 90));
                bookRow.add(cover);
                
                JPanel bookText = new JPanel();
                bookText.setLayout(new BoxLayout(bookText, BoxLayout.Y_AXIS));
                bookText.setOpaque(false);
                JLabel bTitle = new JLabel(book.getTitle());
                bTitle.setFont(BODY_BOLD);
                bTitle.setForeground(WHITE);
                
                int avail = book.getAvailableCopies();
                int total = book.getTotalCopies();
                String copiesStr = avail + " of " + total + (issueMode && canIssue ? " → " + (avail - 1) + " of " + total + " after issue" : " available");
                JLabel bAvail = new JLabel(copiesStr);
                bAvail.setFont(SMALL);
                bAvail.setForeground(new Color(255, 255, 255, 180));
                
                bookText.add(bTitle);
                bookText.add(Box.createVerticalStrut(5));
                bookText.add(bAvail);
                bookRow.add(bookText);
                livePanel.add(bookRow);
                
                livePanel.add(Box.createVerticalStrut(25));
                
                JPanel studentRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
                studentRow.setOpaque(false);
                studentRow.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                Color[] tints = {new Color(153, 168, 150), new Color(201, 142, 116), new Color(214, 195, 165), new Color(135, 160, 178)};
                Color avatarColor = tints[Math.abs(student.getName().hashCode()) % tints.length];
                
                JPanel avatar = new JPanel(new GridBagLayout()) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(avatarColor);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                        g2.dispose();
                    }
                };
                avatar.setOpaque(false);
                avatar.setPreferredSize(new Dimension(42, 42));
                JLabel initial = new JLabel(student.getName().substring(0, 1).toUpperCase());
                initial.setFont(customSerif.deriveFont(Font.BOLD, 18f));
                initial.setForeground(WHITE);
                avatar.add(initial);
                
                studentRow.add(avatar);
                
                JPanel studentText = new JPanel();
                studentText.setLayout(new BoxLayout(studentText, BoxLayout.Y_AXIS));
                studentText.setOpaque(false);
                JLabel sName = new JLabel(student.getName() + " (" + student.getId() + ")");
                sName.setFont(BODY_BOLD);
                sName.setForeground(WHITE);
                
                String statMsg = activeBooks > 0 ? activeBooks + " books currently issued" : "0 books currently issued";
                if (finesDue > 0) statMsg += " · ₹" + (int)finesDue + " unpaid fine";
                
                JLabel sStat = new JLabel(statMsg);
                sStat.setFont(SMALL);
                sStat.setForeground(new Color(255, 255, 255, 180));
                
                studentText.add(sName);
                studentText.add(Box.createVerticalStrut(4));
                studentText.add(sStat);
                studentRow.add(studentText);
                livePanel.add(studentRow);
                
                livePanel.add(Box.createVerticalStrut(25));
                
                JPanel rulesPanel = new JPanel(new GridLayout(3, 1, 0, 8));
                rulesPanel.setOpaque(false);
                rulesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                java.time.LocalDate today = java.time.LocalDate.now();
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("d MMM");
                
                if (!validS || !validB) {
                    JLabel empty = new JLabel(issueMode ? "Select a student and a book to preview" : "Select a student to see their borrowed books");
                    empty.setForeground(new Color(255, 255, 255, 120));
                    empty.setFont(BODY);
                    rulesPanel.add(empty);
                } else {
                    if (issueMode) {
                        java.time.LocalDate due = today.plusDays(selectedDays[0]);
                        JLabel d1 = new JLabel("Issue Date: " + today.format(formatter));
                        d1.setForeground(new Color(255, 255, 255, 200));
                        JLabel d2 = new JLabel("Due Date: " + due.format(formatter));
                        d2.setForeground(new Color(255, 255, 255, 200));
                        JLabel d3 = new JLabel("Fine rule: 1-7 days: ₹5/day, 8-14: ₹10/day, 15+: ₹20/day");
                        d3.setForeground(GOLD);
                        rulesPanel.add(d1); rulesPanel.add(d2); rulesPanel.add(d3);
                    } else {
                        int delayed = 0;
                        LibRecord active = null;
                        for (LibRecord r : records) {
                            if (!r.isReturned() && r.getStudent().getId().equals(sid) && r.getBook().getId().equals(bid)) {
                                active = r;
                                break;
                            }
                        }
                        
                        if (active != null) {
                            long diff = java.time.temporal.ChronoUnit.DAYS.between(active.getIssueDate(), today);
                            if (diff < 0) diff = 0;
                            delayed = (int) diff - active.getAllowedDays();
                            if (delayed < 0) delayed = 0;
                            
                            JLabel info = new JLabel("<html><b>" + active.getBook().getTitle() + "</b><br>Borrowed by: " + active.getStudent().getName() + " (" + active.getStudent().getId() + ")</html>");
                            info.setForeground(WHITE);
                            info.setFont(BODY);
                            rulesPanel.add(info);
                            rulesPanel.add(Box.createVerticalStrut(10));
                            
                            // Timeline
                            java.time.LocalDate due = active.getIssueDate().plusDays(active.getAllowedDays());
                            JLabel timeline = new JLabel("Issued " + active.getIssueDate().format(formatter) + "  →  Due " + due.format(formatter) + "  →  Returned Today");
                            timeline.setForeground(new Color(255, 255, 255, 200));
                            rulesPanel.add(timeline);
                            
                            if (delayed > 0) {
                                JLabel badge = new JLabel("  " + delayed + " Days Overdue  ");
                                badge.setOpaque(true);
                                badge.setBackground(new Color(200, 50, 50));
                                badge.setForeground(WHITE);
                                badge.setFont(SMALL_BOLD);
                                rulesPanel.add(Box.createVerticalStrut(5));
                                rulesPanel.add(badge);
                            } else {
                                JLabel badge = new JLabel("  Returned On Time  ");
                                badge.setOpaque(true);
                                badge.setBackground(new Color(50, 160, 80));
                                badge.setForeground(WHITE);
                                badge.setFont(SMALL_BOLD);
                                rulesPanel.add(Box.createVerticalStrut(5));
                                rulesPanel.add(badge);
                            }
                            
                            rulesPanel.add(Box.createVerticalStrut(15));
                            
                            double finalFine = LibRecord.calculateFineAmount(delayed);
                            
                            if (delayed > 0) {
                                String arithmetic = "";
                                if (delayed <= 7) arithmetic = delayed + " days × ₹5 = ₹" + (delayed * 5);
                                else if (delayed <= 14) arithmetic = "7 days × ₹5 + " + (delayed - 7) + " days × ₹10 = ₹" + (35 + (delayed - 7) * 10);
                                else arithmetic = "7 days × ₹5 + 7 days × ₹10 + " + (delayed - 14) + " days × ₹20 = ₹" + (105 + (delayed - 14) * 20);
                                
                                JLabel breakdown = new JLabel("Breakdown: " + arithmetic);
                                breakdown.setForeground(new Color(255, 255, 255, 150));
                                rulesPanel.add(breakdown);
                            }
                            
                            JLabel fTotal = new JLabel(finalFine == 0 ? "No fine, returned on time" : "Total Fine: ₹" + (int)finalFine);
                            fTotal.setFont(new Font("Georgia", Font.BOLD, 22));
                            if (finalFine == 0) fTotal.setForeground(new Color(80, 200, 120));
                            else if (finalFine <= 50) fTotal.setForeground(GOLD);
                            else fTotal.setForeground(TERRACOTTA);
                            
                            livePanel.add(fTotal);
                            livePanel.add(Box.createVerticalStrut(10));
                            
                            if (finalFine > 0) {
                                JComboBox<String> fineAction = new JComboBox<>(new String[]{"Collect fine now", "Add to student record"});
                                fineAction.setMaximumSize(new Dimension(200, 30));
                                livePanel.add(fineAction);
                            }
                        }
                    }
                }
                
                livePanel.add(rulesPanel);
                infoCard.add(livePanel, BorderLayout.NORTH);
            }
            
            infoCard.revalidate();
            infoCard.repaint();
        };

        studentCombo.addActionListener(e -> {
            if (!issueMode) {
                String selS = (String) studentCombo.getSelectedItem();
                boolean validS = selS != null && !selS.startsWith("Select") && selS.contains(" · ");
                ArrayList<String> newBookItems = new ArrayList<>();
                if (validS) {
                    String sid = selS.split(" · ")[0];
                    for (LibRecord r : records) {
                        if (!r.isReturned() && r.getStudent().getId().equals(sid)) {
                            Book b = r.getBook();
                            newBookItems.add(b.getId() + " · " + b.getTitle() + " · " + b.getAuthor());
                        }
                    }
                } else {
                    for (Book b : books) newBookItems.add(b.getId() + " · " + b.getTitle() + " · " + b.getAuthor());
                }
                bookCombo.removeAllItems();
                bookCombo.addItem("Select Book...");
                for(String b : newBookItems) bookCombo.addItem(b);
                bookCombo.setSelectedIndex(0);
            }
            updatePreview.run();
        });
        bookCombo.addActionListener(e -> updatePreview.run());
        
        if (issueMode) {
            for (JButton chip : chipBtns) {
                chip.addActionListener(e -> {
                    int d = Integer.parseInt(chip.getText().split(" ")[0]);
                    selectedDays[0] = d;
                    for (JButton b : chipBtns) {
                        b.setBackground(WHITE);
                        b.setForeground(MUTED);
                    }
                    chip.setBackground(TERRACOTTA);
                    chip.setForeground(WHITE);
                    updatePreview.run();
                });
            }
        }
        
        updatePreview.run();

        action.addActionListener(e -> {
            action.setText("Processing...");
            action.setEnabled(false);
            
            Timer timer = new Timer(600, ev -> {
                String selS = (String) studentCombo.getSelectedItem();
                String selB = (String) bookCombo.getSelectedItem();
                String sid = selS.split(" · ")[0];
                String bid = selB.split(" · ")[0];
                JTextField sf = new JTextField(sid);
                JTextField bf = new JTextField(bid);
                JTextField df = new JTextField(String.valueOf(selectedDays[0]));
                
                if (issueMode) {
                    // Suppress dialog inside issueBook
                    boolean prev = suppressDialogs;
                    suppressDialogs = true;
                    issueBook(sf, bf, df);
                    suppressDialogs = prev;
                    
                    java.time.LocalDate due = java.time.LocalDate.now().plusDays(selectedDays[0]);
                    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("d MMM");
                    JOptionPane.showMessageDialog(this, selB.split(" · ")[1] + " issued to " + selS.split(" · ")[1] + ", due " + due.format(formatter), "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    boolean prev = suppressDialogs;
                    suppressDialogs = true;
                    returnBook(sf, bf, df);
                    suppressDialogs = prev;
                    JOptionPane.showMessageDialog(this, selB.split(" · ")[1] + " returned successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                
                studentCombo.setSelectedIndex(0);
                bookCombo.setSelectedIndex(0);
                updatePreview.run();
            });
            timer.setRepeats(false);
            timer.start();
        });

        main.gridx = 0;
        main.gridy = 0;
        main.weightx = 0.55;
        content.add(formCard, main);

        main.gridx = 1;
        main.weightx = 0.45;
        main.insets = new Insets(8, 20, 8, 8);
        content.add(infoCard, main);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(CREAM);
        wrapper.add(content, BorderLayout.NORTH);
        if (!issueMode) {
            JPanel borrowedListPanel = new JPanel();
            borrowedListPanel.setLayout(new BoxLayout(borrowedListPanel, BoxLayout.Y_AXIS));
            borrowedListPanel.setOpaque(false);
            borrowedListPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
            
            JLabel listTitle = new JLabel("Currently Borrowed by Selected Student");
            listTitle.setFont(TITLE);
            listTitle.setForeground(INK);
            borrowedListPanel.add(listTitle);
            borrowedListPanel.add(Box.createVerticalStrut(15));
            
            JPanel listContainer = new JPanel();
            listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
            listContainer.setOpaque(false);
            
            // To be populated dynamically by studentCombo action listener
            Runnable updateBorrowedList = () -> {
                listContainer.removeAll();
                String selS = (String) studentCombo.getSelectedItem();
                if (selS != null && !selS.startsWith("Select")) {
                    String sid = selS.split(" · ")[0];
                    for (LibRecord r : records) {
                        if (!r.isReturned() && r.getStudent().getId().equals(sid)) {
                            JPanel row = new JPanel(new BorderLayout());
                            row.setBackground(WHITE);
                            row.setBorder(new EmptyBorder(10, 15, 10, 15));
                            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                            
                            JLabel bName = new JLabel(r.getBook().getTitle());
                            bName.setFont(BODY_BOLD);
                            row.add(bName, BorderLayout.WEST);
                            
                            java.time.LocalDate today = java.time.LocalDate.now();
                            long diff = java.time.temporal.ChronoUnit.DAYS.between(r.getIssueDate(), today);
                            int delayed = (int) diff - r.getAllowedDays();
                            if (delayed < 0) delayed = 0;
                            
                            JPanel rightPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
                            rightPane.setOpaque(false);
                            
                            if (delayed > 0) {
                                JLabel badge = new JLabel(" " + delayed + " Days Overdue ");
                                badge.setOpaque(true);
                                badge.setBackground(new Color(200, 50, 50));
                                badge.setForeground(WHITE);
                                badge.setFont(SMALL_BOLD);
                                rightPane.add(badge);
                            }
                            
                            JButton retBtn = actionButton("Return");
                            retBtn.setPreferredSize(new Dimension(80, 30));
                            retBtn.addActionListener(ev -> {
                                bookCombo.setSelectedItem(r.getBook().getId() + " · " + r.getBook().getTitle() + " · " + r.getBook().getAuthor());
                                action.doClick();
                            });
                            rightPane.add(retBtn);
                            row.add(rightPane, BorderLayout.EAST);
                            
                            listContainer.add(row);
                            listContainer.add(Box.createVerticalStrut(8));
                        }
                    }
                }
                listContainer.revalidate();
                listContainer.repaint();
            };
            
            // Attach to studentCombo
            studentCombo.addActionListener(e -> updateBorrowedList.run());
            
            borrowedListPanel.add(listContainer);
            wrapper.add(borrowedListPanel, BorderLayout.CENTER);
        }
        page.add(wrapper, BorderLayout.CENTER);
        return page;
    }

    private JComboBox<String> createSearchableCombo(ArrayList<String> items, String placeholder) {
        JComboBox<String> cb = new JComboBox<>();
        cb.addItem(placeholder);
        for(String i : items) cb.addItem(i);
        cb.setEditable(true);
        cb.setBackground(WHITE);
        cb.setFont(BODY);
        
        JTextField tf = (JTextField) cb.getEditor().getEditorComponent();
        tf.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_ENTER) return;
                SwingUtilities.invokeLater(() -> {
                    String text = tf.getText();
                    cb.hidePopup();
                    cb.removeAllItems();
                    cb.addItem(placeholder);
                    for (String item : items) {
                        if (item.toLowerCase().contains(text.toLowerCase())) {
                            cb.addItem(item);
                        }
                    }
                    cb.showPopup();
                    tf.setText(text);
                });
            }
        });
        return cb;
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
                                    + (int) totalFine,
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
                customSerif.deriveFont(Font.BOLD, 15f)
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

        if(!suppressDialogs) JOptionPane.showMessageDialog(
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

        String studentId = studentField.getText().trim();
        String bookId = bookField.getText().trim();

        if (studentId.isEmpty() || bookId.isEmpty()) {
            warning("Please fill all required fields.");
            return;
        }

        LibRecord record = findActiveRecord(studentId, bookId);

        if (record == null) {
            warning("No active issue record found.");
            return;
        }

        record.returnBook(java.time.LocalDate.now());
        record.getBook().returnCopy();

        saveData();

        JOptionPane.showMessageDialog(this,
            record.getBook().getTitle() + " returned. Fine ₹" + (int)record.getFine() + " recorded.",
            "Return Success",
            JOptionPane.INFORMATION_MESSAGE);

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

        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(header, BorderLayout.WEST);
        
// 2. Fine Slabs Card
        GlassPanel slabsPanel = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170));
        slabsPanel.setLayout(new BoxLayout(slabsPanel, BoxLayout.Y_AXIS));
        slabsPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JPanel slabsHeader = new JPanel(new BorderLayout());
        slabsHeader.setOpaque(false);
        JLabel slabTitle = new JLabel("Fine Rules");
        slabTitle.setFont(TITLE); slabTitle.setForeground(INK);
        JLabel slabSub = new JLabel("By days late");
        slabSub.setFont(SMALL); slabSub.setForeground(MUTED);
        slabsHeader.add(slabTitle, BorderLayout.WEST);
        slabsHeader.add(slabSub, BorderLayout.EAST);
        slabsPanel.add(slabsHeader);
        
        slabsPanel.add(Box.createVerticalStrut(10));
        
        JPanel slabsGrid = new JPanel(new GridLayout(3, 2, 5, 5));
        slabsGrid.setOpaque(false);
        String[] rules = {"1-7 Days", "₹5/day", "8-14 Days", "₹10/day", "15+ Days", "₹20/day"};
        for (String rule : rules) {
            JLabel l = new JLabel(rule);
            l.setFont(SMALL); l.setForeground(MUTED);
            slabsGrid.add(l);
        }
        slabsPanel.add(slabsGrid);
        slabsPanel.add(Box.createVerticalStrut(10));
        JLabel note = new JLabel("Tiered Calculation");
        note.setFont(SMALL); note.setForeground(MUTED);
        slabsPanel.add(note);
        topPanel.add(slabsPanel, BorderLayout.EAST);
        topPanel.setBorder(new EmptyBorder(30,35,15,35));
        header.setBorder(new EmptyBorder(0,0,0,0));
        page.add(topPanel, BorderLayout.NORTH);

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
                                + (int) totalFine,
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

        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setBackground(CREAM);
        listWrapper.add(list, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(listWrapper);

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
                                + (int) record.getFine()
                );

        fine.setFont(
                customSerif.deriveFont(Font.BOLD, 24f)
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

            g2.setColor(new Color(60, 45, 35, 25));

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

            g2.setColor(borderColor);

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