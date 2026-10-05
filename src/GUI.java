import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import javax.swing.table.*;
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
            new Color(175, 138, 70);

    static final Color MUTED = new Color(85, 75, 68);

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
        setSize(1380, 850);
        setMinimumSize(new Dimension(1100, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        students = FileManager.loadStudents();
        books = FileManager.loadBooks();
        records = FileManager.loadRecords(students, books);
    }
    void saveData() {
        FileManager.saveStudents(students);
        FileManager.saveBooks(books);
        FileManager.saveRecords(records);
    }


    // =========================================================
    // MAIN INTERFACE
    // =========================================================

    void createInterface() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(CREAM);
        root.add(createSidebar(), BorderLayout.WEST);
        root.add(createWorkspace(), BorderLayout.CENTER);
        setContentPane(root);
    }


    // =========================================================
    // SIDEBAR
    // =========================================================

    JPanel sidebar;
    String activePage = "HOME";
    String booksFilterMode = "ALL";
    boolean returnFilterOverdueOnly = false;
    int activityDaysRange = 7;
    JPanel createSidebar() {
        sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBackground(ESPRESSO);

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(new EmptyBorder(32, 24, 20, 20));

        JLabel logo = new JLabel("MINDSPACE");
        logo.setFont(customSerif.deriveFont(Font.BOLD, 26f));
        logo.setForeground(WHITE);

        JLabel sub = new JLabel("LIBRARY MANAGEMENT");
        sub.setFont(SMALL_BOLD);
        sub.setForeground(GOLD);

        top.add(logo);
        top.add(Box.createVerticalStrut(4));
        top.add(sub);
        top.add(Box.createVerticalStrut(38));

        addNavigation(top, "HOME", "Overview", "HOME");
        addNavigation(top, "BOOKS", "Book Collection", "BOOKS");
        addNavigation(top, "STUDENTS", "Students", "STUDENTS");
        addNavigation(top, "ISSUE", "Issue Book", "ISSUE");
        addNavigation(top, "RETURN", "Return Book", "RETURN");
        addNavigation(top, "FINES", "Fine Records", "FINES");

        sidebar.add(top, BorderLayout.NORTH);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(0, 10, 10, 10));

        // User / librarian profile area above Collapse Sidebar
        JPanel userBox = new JPanel(new BorderLayout(8, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel() != null && (getMousePosition() != null)) {
                    g2.setColor(new Color(60, 48, 42));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                super.paintComponent(g);
                g2.dispose();
            }
            private ButtonModel model;
            public ButtonModel getModel() { return model; }
        };
        userBox.setOpaque(false);
        userBox.setBorder(new EmptyBorder(6, 8, 6, 8));
        userBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        userBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel uAvatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(184, 98, 77));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(WHITE);
                g2.setFont(SMALL_BOLD);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("N", (getWidth() - fm.stringWidth("N")) / 2, (getHeight() + fm.getAscent() - 3) / 2);
                g2.dispose();
            }
        };
        uAvatar.setPreferredSize(new Dimension(28, 28));
        uAvatar.setOpaque(false);
        
        JPanel uDetails = new JPanel();
        uDetails.setOpaque(false);
        uDetails.setLayout(new BoxLayout(uDetails, BoxLayout.Y_AXIS));
        JLabel uName = new JLabel("Nishi Chopda");
        uName.setFont(SMALL_BOLD);
        uName.setForeground(WHITE);
        JLabel uRole = new JLabel("Head Librarian • Online");
        uRole.setFont(new Font("SansSerif", Font.PLAIN, 10));
        uRole.setForeground(new Color(175, 165, 155));
        uDetails.add(uName);
        uDetails.add(Box.createVerticalStrut(2));
        uDetails.add(uRole);
        
        userBox.add(uAvatar, BorderLayout.WEST);
        userBox.add(uDetails, BorderLayout.CENTER);
        
        userBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                userBox.setOpaque(true);
                userBox.setBackground(new Color(60, 48, 42));
                userBox.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                userBox.setOpaque(false);
                userBox.repaint();
            }
        });
        
        bottom.add(userBox);
        bottom.add(Box.createVerticalStrut(6));


        sidebar.add(
                bottom,
                BorderLayout.SOUTH
        );

        return sidebar;
    }


    void addNavigation(JPanel parent, String iconName, String text, String page) {
        JButton button = navButton(iconName, text, page);
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
                    g2.setColor(new Color(212, 175, 55, 40));
                    g2.fillRoundRect(4, 0, getWidth() - 8, getHeight(), 12, 12);
                    g2.setColor(GOLD);
                    g2.fillRect(4, 8, 3, getHeight() - 16);
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

        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                boolean isActive = activePage != null && activePage.equals(page);
                setForeground(isActive ? WHITE : new Color(230, 222, 212));
                setIcon(createLineIcon(icon, 15, isActive ? GOLD : new Color(200, 190, 180)));
                super.paintComponent(g);
            }
        };
        label.setIcon(createLineIcon(icon, 15, new Color(200, 190, 180)));
        label.setIconTextGap(10);
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
        JPanel workspace = new JPanel(new BorderLayout());
        workspace.setBackground(CREAM);
        workspace.add(createTopbar(), BorderLayout.NORTH);
        cardLayout = new CardLayout();
        pages = new JPanel(cardLayout);
        pages.setBackground(CREAM);
        pages.add(createHomePage(), "HOME");
        pages.add(createBooksPage(), "BOOKS");
        pages.add(createStudentsPage(), "STUDENTS");
        pages.add(createIssuePage(), "ISSUE");
        pages.add(createReturnPage(), "RETURN");
        pages.add(createFinesPage(), "FINES");
        workspace.add(pages, BorderLayout.CENTER);
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



        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 14));
        right.setOpaque(false);
        
        // Notification bell with active loan / overdue indicator
        int overdueCount = 0;
        for (LibRecord r : records) {
            if (!r.isReturned()) {
                long ds = java.time.temporal.ChronoUnit.DAYS.between(r.getIssueDate(), java.time.LocalDate.now());
                if (ds > r.getAllowedDays()) overdueCount++;
            }
        }
        final int badgeCount = overdueCount;
        
        JButton bellBtn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(240, 235, 228));
                    g2.fillOval(0, 0, getWidth(), getHeight());
                }
                Icon icon = createLineIcon("BELL", 16, INK);
                icon.paintIcon(this, g2, (getWidth() - 16) / 2, (getHeight() - 16) / 2);
                if (badgeCount > 0) {
                    g2.setColor(TERRACOTTA);
                    if (badgeCount > 9) {
                        g2.fillRoundRect(getWidth() - 16, 2, 14, 12, 6, 6);
                    } else {
                        g2.fillOval(getWidth() - 12, 3, 10, 10);
                    }
                    g2.setColor(WHITE);
                    g2.setFont(new Font("SansSerif", Font.BOLD, 8));
                    FontMetrics fm = g2.getFontMetrics();
                    String bStr = String.valueOf(badgeCount);
                    int bx = badgeCount > 9 ? getWidth() - 16 + (14 - fm.stringWidth(bStr)) / 2 : getWidth() - 12 + (10 - fm.stringWidth(bStr)) / 2;
                    int by = badgeCount > 9 ? 11 : 11;
                    g2.drawString(bStr, bx, by);
                }
                g2.dispose();
            }
        };
        bellBtn.setPreferredSize(new Dimension(34, 34));
        bellBtn.setOpaque(false);
        bellBtn.setContentAreaFilled(false);
        bellBtn.setBorderPainted(false);
        bellBtn.setFocusPainted(false);
        bellBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bellBtn.setToolTipText(badgeCount > 0 ? badgeCount + " overdue books requiring attention" : "No active alerts");
        bellBtn.addActionListener(e -> {
            if (badgeCount > 0) {
                returnFilterOverdueOnly = true;
                showPage("RETURN");
            } else {
                JOptionPane.showMessageDialog(bar, "All book loans are up to date!", "Notifications", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        right.add(bellBtn);
        
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
        pLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pLabel.setToolTipText("Librarian Profile");
        
        JPopupMenu profileMenu = new JPopupMenu();
        profileMenu.setBackground(PAPER);
        profileMenu.setBorder(BorderFactory.createLineBorder(SAND));
        
        JMenuItem mUser = new JMenuItem("Logged in: Nishi Chopda (Librarian)");
        mUser.setFont(SMALL_BOLD);
        mUser.setEnabled(false);
        profileMenu.add(mUser);
        profileMenu.addSeparator();
        
        JMenuItem mStudents = new JMenuItem("Student Directory");
        mStudents.setFont(BODY);
        mStudents.addActionListener(e -> showPage("STUDENTS"));
        profileMenu.add(mStudents);
        
        JMenuItem mBooks = new JMenuItem("Book Catalog");
        mBooks.setFont(BODY);
        mBooks.addActionListener(e -> showPage("BOOKS"));
        profileMenu.add(mBooks);
        
        JMenuItem mFines = new JMenuItem("Fine Summary");
        mFines.setFont(BODY);
        mFines.addActionListener(e -> showPage("FINES"));
        profileMenu.add(mFines);
        
        profileMenu.addSeparator();
        JMenuItem mStatus = new JMenuItem("System Status: Online (v1.0)");
        mStatus.setFont(SMALL);
        mStatus.setEnabled(false);
        profileMenu.add(mStatus);
        
        pLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                profileMenu.show(pLabel, -120, pLabel.getHeight() + 4);
            }
        });
        
        profile.add(pLabel);
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
        // Just trigger repaints. The button's paintComponent uses activePage.
        for (java.awt.Component comp : c.getComponents()) {
            if (comp instanceof javax.swing.JButton) {
                comp.repaint();
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
    // LINE ICONS & DASHBOARD HELPERS
    // =========================================================

    static Icon createLineIcon(final String name, final int size, final Color color) {
        return new Icon() {
            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                g2.translate(x, y);
                g2.setColor(color != null ? color : INK);
                g2.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                float s = size / 24.0f;
                g2.scale(s, s);
                
                String n = name != null ? name.toUpperCase() : "";
                if (n.equals("ISSUE") || n.equals("ARROW-UP-RIGHT-BOOK")) {
                    // Open book with arrow pointing up-right
                    g2.drawRoundRect(2, 4, 18, 16, 3, 3);
                    g2.drawLine(11, 4, 11, 20); // spine
                    // Arrow pointing up-right from center/top
                    g2.drawLine(10, 14, 17, 7);
                    g2.drawLine(12, 7, 17, 7);
                    g2.drawLine(17, 7, 17, 12);
                } else if (n.equals("RETURN") || n.equals("ARROW-DOWN-LEFT-BOOK")) {
                    // Open book with arrow pointing down-left
                    g2.drawRoundRect(4, 4, 18, 16, 3, 3);
                    g2.drawLine(13, 4, 13, 20); // spine
                    // Arrow pointing down-left into book
                    g2.drawLine(14, 10, 7, 17);
                    g2.drawLine(12, 17, 7, 17);
                    g2.drawLine(7, 17, 7, 12);
                } else if (n.equals("BOOK") || n.equals("BOOKS") || n.equals("CATALOG")) {
                    // Open book icon
                    g2.drawRoundRect(3, 4, 18, 16, 3, 3);
                    g2.drawLine(12, 4, 12, 20);
                    g2.drawLine(6, 8, 9, 8);
                    g2.drawLine(6, 12, 9, 12);
                    g2.drawLine(15, 8, 18, 8);
                    g2.drawLine(15, 12, 18, 12);
                } else if (n.equals("COPIES") || n.equals("STACK")) {
                    // Stack of books
                    g2.drawRoundRect(4, 14, 16, 5, 2, 2);
                    g2.drawRoundRect(4, 9, 16, 5, 2, 2);
                    g2.drawRoundRect(4, 4, 16, 5, 2, 2);
                    g2.drawLine(7, 6, 7, 8);
                    g2.drawLine(7, 11, 7, 13);
                    g2.drawLine(7, 16, 7, 18);
                } else if (n.equals("LOAN") || n.equals("ON LOAN")) {
                    // Book with loan / arrow up-right
                    g2.drawRoundRect(3, 5, 13, 15, 2, 2);
                    g2.drawLine(12, 11, 20, 3);
                    g2.drawLine(15, 3, 20, 3);
                    g2.drawLine(20, 3, 20, 8);
                } else if (n.equals("OVERDUE") || n.equals("BELL") || n.equals("ALERT")) {
                    // Bell icon
                    g2.drawArc(7, 5, 10, 10, 0, 180);
                    g2.drawLine(7, 10, 4, 16);
                    g2.drawLine(17, 10, 20, 16);
                    g2.drawLine(4, 16, 20, 16);
                    g2.drawArc(10, 16, 4, 3, 180, 180);
                    g2.drawOval(11, 3, 2, 2);
                } else if (n.equals("HOME") || n.equals("OVERVIEW")) {
                    // Clean line house
                    g2.drawLine(12, 3, 3, 11);
                    g2.drawLine(12, 3, 21, 11);
                    g2.drawLine(5, 10, 5, 20);
                    g2.drawLine(19, 10, 19, 20);
                    g2.drawLine(5, 20, 19, 20);
                    g2.drawRect(10, 13, 4, 7);
                } else if (n.equals("STUDENTS") || n.equals("STUDENT")) {
                    // Clean user / student icon
                    g2.drawOval(8, 4, 8, 8);
                    g2.drawArc(4, 14, 16, 10, 0, 180);
                } else if (n.equals("FINES") || n.equals("FINE")) {
                    // Receipt / tag icon
                    g2.drawRoundRect(5, 3, 14, 18, 3, 3);
                    g2.drawLine(8, 7, 16, 7);
                    g2.drawLine(8, 11, 16, 11);
                    g2.drawLine(8, 15, 13, 15);
                } else {
                    // Default generic book line icon
                    g2.drawRoundRect(4, 4, 16, 16, 3, 3);
                    g2.drawLine(12, 4, 12, 20);
                }
                
                g2.dispose();
            }
        };
    }

    static JPanel createAvatar(String initial, Color bg, Color fg, int size) {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillOval(0, 0, size, size);
                g2.setColor(fg);
                g2.setFont(new Font("SansSerif", Font.BOLD, (int)(size * 0.48)));
                FontMetrics fm = g2.getFontMetrics();
                String txt = (initial == null || initial.isEmpty()) ? "?" : initial.substring(0, 1).toUpperCase();
                int x = (size - fm.stringWidth(txt)) / 2;
                int y = (size - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(txt, x, y);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(size, size));
        p.setMaximumSize(new Dimension(size, size));
        p.setMinimumSize(new Dimension(size, size));
        return p;
    }

    // =========================================================
    // DASHBOARD
    // =========================================================

    
    JPanel createHomePage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(CREAM);
        
        // Use a JScrollPane because the page is getting long
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(CREAM);
        content.setBorder(new EmptyBorder(26, 32, 40, 32));

        // ---------- HEADER ----------
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));

        int hour = java.time.LocalTime.now().getHour();
        String greeting = "Good evening";
        if (hour >= 5 && hour < 12) greeting = "Good morning";
        else if (hour >= 12 && hour < 17) greeting = "Good afternoon";
        
        String librarianName = (!students.isEmpty()) ? students.get(0).getName().split(" ")[0] : "Librarian";
        JLabel title = new JLabel(greeting + ", " + librarianName + ".");
        title.setFont(DISPLAY); title.setForeground(INK);

        JLabel subtitle = new JLabel("Your library, beautifully organized.");
        subtitle.setFont(BODY); subtitle.setForeground(MUTED);

        heading.add(title);
        heading.add(Box.createVerticalStrut(3));
        heading.add(subtitle);
        header.add(heading, BorderLayout.WEST);

        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy");
        JLabel dateLabel = new JLabel(java.time.LocalDate.now().format(dtf));
        dateLabel.setFont(SMALL_BOLD);
        dateLabel.setForeground(MUTED);

        // Header right panel with date
        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        headerRight.setOpaque(false);
        headerRight.add(dateLabel);

        header.add(headerRight, BorderLayout.EAST);

        content.add(header);
        content.add(Box.createVerticalStrut(20));

        // Compute dashboard metrics
        int totalCopies = 0, availableCopies = 0, issuedCopies = 0, overdueBooks = 0;
        for (Book book : books) {
            totalCopies += book.getTotalCopies();
            availableCopies += book.getAvailableCopies();
        }
        issuedCopies = totalCopies - availableCopies;

        double totalFine = 0;
        for (LibRecord r : records) {
            if (r.isReturned()) {
                totalFine += r.getFine();
            } else {
                long ds = java.time.temporal.ChronoUnit.DAYS.between(r.getIssueDate(), java.time.LocalDate.now());
                if (ds > r.getAllowedDays()) overdueBooks++;
            }
        }

        // Find closest due loan within 7 days
        int minDueDays = -1;
        int dueSoonCount = 0;
        java.time.LocalDate todayDate = java.time.LocalDate.now();
        for (LibRecord r : records) {
            if (!r.isReturned()) {
                java.time.LocalDate dueDate = r.getIssueDate().plusDays(r.getAllowedDays());
                long diff = java.time.temporal.ChronoUnit.DAYS.between(todayDate, dueDate);
                if (diff >= 0 && diff <= 7) {
                    dueSoonCount++;
                    if (minDueDays == -1 || diff < minDueDays) {
                        minDueDays = (int) diff;
                    }
                }
            }
        }

        // ---------- HERO BANNER (Trimmed height with Issue a Book shortcut) ----------
        GlassPanel hero = new GlassPanel(INK, new Color(255, 255, 255, 45));
        hero.setLayout(new BorderLayout());
        hero.setPreferredSize(new Dimension(0, 54));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        hero.setBorder(new EmptyBorder(7, 20, 7, 20));

        JPanel heroText = new JPanel();
        heroText.setOpaque(false);
        heroText.setLayout(new BoxLayout(heroText, BoxLayout.Y_AXIS));

        JLabel eyebrow = new JLabel("MINDSPACE · LIVE DISPATCH");
        eyebrow.setFont(new Font("SansSerif", Font.BOLD, 9)); eyebrow.setForeground(GOLD);

        String loanPlural = issuedCopies == 1 ? "1 book currently on loan" : issuedCopies + " books currently on loan";
        String dueMsg;
        if (dueSoonCount > 0) {
            String dueWord = dueSoonCount == 1 ? "1 book" : dueSoonCount + " books";
            String dayWord = minDueDays == 0 ? "today" : (minDueDays == 1 ? "in 1 day" : "in " + minDueDays + " days");
            dueMsg = dueWord + " due " + dayWord + "  •  " + loanPlural;
        } else {
            dueMsg = "All loans within schedule  •  " + loanPlural;
        }
        JLabel heroTitle = new JLabel(dueMsg);
        heroTitle.setFont(customSerif.deriveFont(Font.BOLD, 15f)); heroTitle.setForeground(WHITE);

        heroText.add(eyebrow);
        heroText.add(Box.createVerticalStrut(2));
        heroText.add(heroTitle);

        hero.add(heroText, BorderLayout.WEST);
        
        final int overdueRef = overdueBooks;
        final int dueSoonRef = dueSoonCount;
        final int minDueDaysRef = minDueDays;
        JPanel heroRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        heroRight.setOpaque(false);

        // Contextual action button inside banner (avoids duplicating "+ Issue a Book" from quick actions and sidebar)
        String actionBtnText = overdueRef > 0 ? "Review Overdue" : (dueSoonRef > 0 ? "Send Reminder" : "View Loans");
        String actionBtnTip = overdueRef > 0 ? "Inspect overdue records immediately" : (dueSoonRef > 0 ? "Send loan due reminder notifications" : "View current active loan records");
        Color actionBtnBg = overdueRef > 0 ? new Color(184, 80, 65) : (dueSoonRef > 0 ? new Color(175, 125, 45) : new Color(75, 95, 80));
        Color actionBtnHover = overdueRef > 0 ? new Color(205, 95, 80) : (dueSoonRef > 0 ? new Color(195, 140, 55) : new Color(90, 115, 98));

        JButton heroActionBtn = new JButton(actionBtnText) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? actionBtnHover : actionBtnBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        heroActionBtn.setFont(SMALL_BOLD);
        heroActionBtn.setForeground(WHITE);
        heroActionBtn.setOpaque(false);
        heroActionBtn.setContentAreaFilled(false);
        heroActionBtn.setBorderPainted(false);
        heroActionBtn.setFocusPainted(false);
        heroActionBtn.setBorder(new EmptyBorder(5, 12, 5, 12));
        heroActionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        heroActionBtn.setToolTipText(actionBtnTip);
        heroActionBtn.addActionListener(e -> {
            if (overdueRef > 0) {
                returnFilterOverdueOnly = true;
                showPage("RETURN");
            } else if (dueSoonRef > 0) {
                String dWord = minDueDaysRef == 0 ? "today" : (minDueDaysRef == 1 ? "in 1 day" : "in " + minDueDaysRef + " days");
                showToast("Reminder sent to borrowers with books due " + dWord + ".", SAGE);
            } else {
                booksFilterMode = "ON_LOAN";
                showPage("BOOKS");
            }
        });
        heroRight.add(heroActionBtn);

        String badgeText = overdueRef > 0 
            ? (overdueRef == 1 ? "  ⚠ 1 Overdue  " : "  ⚠ " + overdueRef + " Overdue  ")
            : "  ✓ All On Track  ";
        JLabel heroBadge = new JLabel(badgeText) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(overdueRef > 0 ? new Color(184, 98, 77, 180) : new Color(105, 126, 98, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        heroBadge.setOpaque(false);
        heroBadge.setFont(SMALL_BOLD);
        heroBadge.setForeground(WHITE);
        heroRight.add(heroBadge);
        hero.add(heroRight, BorderLayout.EAST);
        
        hero.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(hero);
        content.add(Box.createVerticalStrut(12));

        // ---------- STATS ----------
        JPanel stats = new JPanel(new GridLayout(1, 4, 12, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        stats.setAlignmentX(Component.LEFT_ALIGNMENT);

        stats.add(statCard("BOOK TITLES", String.valueOf(books.size()), "+4 this month", TERRACOTTA, "BOOK", "BOOKS", () -> {
            booksFilterMode = "ALL";
            showPage("BOOKS");
        }));
        stats.add(statCard("TOTAL COPIES", String.valueOf(totalCopies), "in physical library", SAGE, "COPIES", "BOOKS", () -> {
            booksFilterMode = "ALL";
            showPage("BOOKS");
        }));
        stats.add(statCard("ON LOAN", String.valueOf(issuedCopies), "currently borrowed", GOLD, "LOAN", "BOOKS", () -> {
            booksFilterMode = "ON_LOAN";
            showPage("BOOKS");
        }));
        stats.add(statCard("OVERDUE", String.valueOf(overdueBooks), overdueBooks == 0 ? "all books on time" : "requires attention", overdueBooks == 0 ? SAGE : new Color(200, 75, 75), "OVERDUE", "RETURN", () -> {
            returnFilterOverdueOnly = (overdueRef > 0);
            showPage("RETURN");
        }));

        content.add(stats);
        content.add(Box.createVerticalStrut(14));

        // ---------- QUICK ACTIONS ----------
        JPanel actions = new JPanel(new GridLayout(1, 3, 12, 0));
        actions.setOpaque(false);
        actions.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        actions.add(largeQuickAction("Browse Collection", "Search and view library books", "BOOKS", "BOOKS", false));
        actions.add(largeQuickAction("Issue a Book", "Register a new outgoing book", "ISSUE", "ISSUE", false));
        actions.add(largeQuickAction("Return a Book", "Process incoming book & fines", "RETURN", "RETURN", false));
        
        content.add(actions);
        content.add(Box.createVerticalStrut(14));

        // ---------- ROW 1: CHARTS (60/40) ----------
        JPanel row1 = new JPanel(new GridBagLayout());
        row1.setOpaque(false);
        row1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 255));
        row1.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 6);
        
        // Bar Chart - Dynamic Days Range (7 default, with 7/14/30 toggle)
        final int numDays = (activityDaysRange == 30 || activityDaysRange == 14) ? activityDaysRange : 7;
        final int[] issues = new int[numDays];
        final int[] returns = new int[numDays];
        final java.time.LocalDate today = java.time.LocalDate.now();
        final String[] colDates = new String[numDays];
        final java.time.format.DateTimeFormatter chartDtf = java.time.format.DateTimeFormatter.ofPattern("d MMM");
        final java.time.format.DateTimeFormatter dayNumDtf = java.time.format.DateTimeFormatter.ofPattern("d");
        final String[] dayNumLabels = new String[numDays];

        for (int i = 0; i < numDays; i++) {
            java.time.LocalDate d = today.minusDays(numDays - 1 - i);
            colDates[i] = d.format(chartDtf);
            dayNumLabels[i] = d.format(dayNumDtf);
        }

        for (LibRecord r : records) {
            long dIssue = java.time.temporal.ChronoUnit.DAYS.between(r.getIssueDate(), today);
            if (dIssue >= 0 && dIssue < numDays) {
                int bucket = (int)(numDays - 1 - dIssue);
                if (bucket >= 0 && bucket < numDays) issues[bucket]++;
            }
            
            if (r.isReturned()) {
                long dRet = java.time.temporal.ChronoUnit.DAYS.between(r.getIssueDate().plusDays(r.getActualDays()), today);
                if (dRet >= 0 && dRet < numDays) {
                    int bucket = (int)(numDays - 1 - dRet);
                    if (bucket >= 0 && bucket < numDays) returns[bucket]++;
                }
            }
        }

        int computedMax = 1;
        int totalActivities = 0;
        for (int i = 0; i < numDays; i++) {
            if (issues[i] > computedMax) computedMax = issues[i];
            if (returns[i] > computedMax) computedMax = returns[i];
            totalActivities += issues[i] + returns[i];
        }
        final int maxActivity = Math.max(3, computedMax);
        final int totalActivityCount = totalActivities;

        GlassPanel barPanel = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170)) {
            private int hoverIndex = -1;

            {
                addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        if (totalActivityCount == 0) return;
                        int w = getWidth();
                        int padX = 18;
                        int chartW = w - 2 * padX;
                        int colW = chartW / numDays;
                        int mx = e.getX();
                        if (mx >= padX && mx < padX + numDays * colW) {
                            int idx = (mx - padX) / colW;
                            if (idx >= 0 && idx < numDays) {
                                hoverIndex = idx;
                                String isText = issues[idx] + (issues[idx] == 1 ? " issue" : " issues");
                                String retText = returns[idx] + (returns[idx] == 1 ? " return" : " returns");
                                setToolTipText("<html><body style='padding: 4px 7px;'><b>" + colDates[idx] + "</b><br/>" +
                                               "<span style='color: #B8624D;'>• Issues: " + issues[idx] + "</span><br/>" +
                                               "<span style='color: #697E62;'>• Returns: " + returns[idx] + "</span></body></html>");
                                repaint();
                                return;
                            }
                        }
                        if (hoverIndex != -1) {
                            hoverIndex = -1;
                            setToolTipText(null);
                            repaint();
                        }
                    }
                });
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (hoverIndex != -1) {
                            hoverIndex = -1;
                            setToolTipText(null);
                            repaint();
                        }
                    }
                });
            }

            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth(), h = getHeight();
                int padX = 18, padTop = 46, padBtm = 26;
                int chartW = w - 2 * padX;
                int btmY = h - padBtm;
                int topY = padTop;
                int chartH = Math.max(10, btmY - topY);

                if (totalActivityCount == 0) {
                    g2.setColor(MUTED);
                    g2.setFont(BODY);
                    FontMetrics fm = g2.getFontMetrics();
                    String emptyMsg = "Not enough data in the selected " + numDays + "-day period";
                    g2.drawString(emptyMsg, (w - fm.stringWidth(emptyMsg)) / 2, topY + chartH / 2);
                    g2.dispose();
                    return;
                }
                
                // Light subtle horizontal gridlines
                g2.setColor(new Color(236, 230, 222));
                for (int step = 1; step <= 3; step++) {
                    int gy = btmY - (int)((step / 3.0) * chartH);
                    g2.drawLine(padX, gy, w - padX, gy);
                }

                // Baseline
                g2.setColor(new Color(220, 210, 198));
                g2.drawLine(padX, btmY, w - padX, btmY);
                
                int colW = chartW / numDays;
                int barW = Math.max(4, Math.min(numDays <= 7 ? 16 : 8, (colW - (numDays <= 7 ? 8 : 4)) / 2));
                
                for (int i = 0; i < numDays; i++) {
                    int xCenter = padX + i * colW + (colW / 2);
                    
                    if (hoverIndex == i) {
                        g2.setColor(new Color(240, 232, 220, 150));
                        g2.fillRoundRect(padX + i * colW + 1, topY, colW - 2, chartH, 4, 4);
                    }
                    
                    // Controlled scaling
                    int hIssue = (int)((Math.sqrt(issues[i]) / Math.sqrt(maxActivity)) * chartH);
                    g2.setColor(TERRACOTTA);
                    if (issues[i] > 0) {
                        g2.fillRoundRect(xCenter - barW - 1, btmY - Math.max(3, hIssue), barW, Math.max(3, hIssue), 3, 3);
                    } else {
                        g2.fillRect(xCenter - barW - 1, btmY - 1, barW, 1);
                    }
                    
                    int hRet = (int)((Math.sqrt(returns[i]) / Math.sqrt(maxActivity)) * chartH);
                    g2.setColor(SAGE);
                    if (returns[i] > 0) {
                        g2.fillRoundRect(xCenter + 1, btmY - Math.max(4, hRet), barW, Math.max(4, hRet), 3, 3);
                    } else {
                        g2.fillRect(xCenter + 1, btmY - 1, barW, 1);
                    }
                    
                    // Show date label on every bar for 7d and 14d, or every 2nd/3rd for 30d
                    boolean showLabel = (numDays <= 14) || (i % 3 == 0) || (i == numDays - 1);
                    if (showLabel) {
                        g2.setColor(hoverIndex == i ? INK : MUTED);
                        g2.setFont(new Font("SansSerif", hoverIndex == i ? Font.BOLD : Font.PLAIN, numDays <= 7 ? 10 : 9));
                        FontMetrics fm = g2.getFontMetrics();
                        String lbl = numDays <= 7 ? colDates[i] : dayNumLabels[i];
                        g2.drawString(lbl, xCenter - fm.stringWidth(lbl) / 2, btmY + 15);
                    }
                }

                // Render instant on-canvas tooltip card when hovering a day column
                if (hoverIndex >= 0 && hoverIndex < numDays) {
                    int i = hoverIndex;
                    int xCenter = padX + i * colW + (colW / 2);
                    String tDate = colDates[i];
                    String tIss = issues[i] + (issues[i] == 1 ? " issue" : " issues");
                    String tRet = returns[i] + (returns[i] == 1 ? " return" : " returns");
                    
                    g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                    FontMetrics fmb = g2.getFontMetrics();
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                    FontMetrics fmp = g2.getFontMetrics();
                    
                    int tipW = Math.max(fmb.stringWidth(tDate), Math.max(fmp.stringWidth("• " + tIss), fmp.stringWidth("• " + tRet))) + 18;
                    int tipH = 46;
                    int tipX = Math.max(padX, Math.min(w - padX - tipW, xCenter - tipW / 2));
                    int tipY = topY + 4;
                    
                    // Shadow & card background
                    g2.setColor(new Color(40, 30, 20, 30));
                    g2.fillRoundRect(tipX + 1, tipY + 2, tipW, tipH, 8, 8);
                    g2.setColor(new Color(254, 252, 248));
                    g2.fillRoundRect(tipX, tipY, tipW, tipH, 8, 8);
                    g2.setColor(new Color(215, 205, 192));
                    g2.drawRoundRect(tipX, tipY, tipW, tipH, 8, 8);
                    
                    // Header date
                    g2.setColor(INK);
                    g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                    g2.drawString(tDate, tipX + 8, tipY + 13);
                    
                    // Issues
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                    g2.setColor(TERRACOTTA);
                    g2.drawString("• " + tIss, tipX + 8, tipY + 26);
                    
                    // Returns
                    g2.setColor(SAGE);
                    g2.drawString("• " + tRet, tipX + 8, tipY + 39);
                }
                
                g2.dispose();
            }
        };
        barPanel.setPreferredSize(new Dimension(300, 255));
        barPanel.setLayout(new BorderLayout());
        barPanel.setBorder(new EmptyBorder(16, 18, 16, 18));
        
        JPanel barHeader = new JPanel(new BorderLayout());
        barHeader.setOpaque(false);
        barHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        
        JPanel barHeaderLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        barHeaderLeft.setOpaque(false);
        JLabel barTitle = new JLabel("Activity Flow");
        barTitle.setFont(TITLE);
        barTitle.setForeground(INK);
        barHeaderLeft.add(barTitle);
        barHeaderLeft.add(Box.createHorizontalStrut(14));
        
        // 7 / 14 / 30 day range toggle pills with consistent width & spacing
        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        togglePanel.setOpaque(false);
        int[] ranges = {7, 14, 30};
        for (int rVal : ranges) {
            final int chosen = rVal;
            boolean isSel = (numDays == chosen);
            JButton tBtn = new JButton(chosen + "d") {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (isSel) {
                        g2.setColor(TERRACOTTA);
                        g2.fillRoundRect(0, 1, getWidth(), getHeight() - 2, 6, 6);
                    } else if (getModel().isRollover()) {
                        g2.setColor(new Color(235, 228, 220));
                        g2.fillRoundRect(0, 1, getWidth(), getHeight() - 2, 6, 6);
                    }
                    super.paintComponent(g);
                    g2.dispose();
                }
            };
            tBtn.setFont(new Font("SansSerif", isSel ? Font.BOLD : Font.PLAIN, 10));
            tBtn.setForeground(isSel ? WHITE : MUTED);
            tBtn.setOpaque(false);
            tBtn.setContentAreaFilled(false);
            tBtn.setBorderPainted(false);
            tBtn.setFocusPainted(false);
            tBtn.setPreferredSize(new Dimension(32, 22));
            tBtn.setMargin(new Insets(1, 2, 1, 2));
            tBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            tBtn.addActionListener(e -> {
                activityDaysRange = chosen;
                showPage("HOME");
            });
            togglePanel.add(tBtn);
        }
        barHeaderLeft.add(togglePanel);
        barHeader.add(barHeaderLeft, BorderLayout.WEST);
        
        JPanel barHeaderRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        barHeaderRight.setOpaque(false);
        
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        legendPanel.setOpaque(false);
        
        JPanel isDot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(TERRACOTTA);
                g.fillRoundRect(0, 3, 8, 8, 2, 2);
            }
        };
        isDot.setOpaque(false);
        isDot.setPreferredSize(new Dimension(8, 14));
        JLabel isLbl = new JLabel("Issues");
        isLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        isLbl.setForeground(INK);
        
        JPanel retDot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(SAGE);
                g.fillRoundRect(0, 3, 8, 8, 2, 2);
            }
        };
        retDot.setOpaque(false);
        retDot.setPreferredSize(new Dimension(8, 14));
        JLabel retLbl = new JLabel("Returns");
        retLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        retLbl.setForeground(INK);
        
        legendPanel.add(isDot);
        legendPanel.add(isLbl);
        legendPanel.add(Box.createHorizontalStrut(2));
        legendPanel.add(retDot);
        legendPanel.add(retLbl);
        
        barHeaderRight.add(legendPanel);
        barHeader.add(barHeaderRight, BorderLayout.EAST);
        barPanel.add(barHeader, BorderLayout.NORTH);
        
        gbc.weightx = 0.6; gbc.gridx = 0;
        row1.add(barPanel, gbc);
        
        // Donut Chart
        GlassPanel chartPanel = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                
                int acad = 0, ref = 0, gen = 0, fic = 0;
                for (Book b : books) {
                    String cat = b.getCategory();
                    if (cat.equalsIgnoreCase("Academic") || cat.equalsIgnoreCase("CS") || cat.equalsIgnoreCase("Computer Science")) acad++;
                    else if (cat.equalsIgnoreCase("Reference")) ref++;
                    else if (cat.equalsIgnoreCase("General") || cat.equalsIgnoreCase("Self-Help")) gen++;
                    else if (cat.equalsIgnoreCase("Fiction")) fic++;
                    else acad++;
                }
                int total = acad + ref + gen + fic;
                if (total == 0) total = 1;
                
                int topH = 44;
                int availH = h - topH - 16;
                int size = Math.min(availH, w - 160);
                if (size < 95) size = 95;
                if (size > availH) size = availH;
                
                int x = 20;
                int y = topH + (availH - size) / 2;
                
                int a1 = (int)(acad * 360.0 / total), a2 = (int)(ref * 360.0 / total), a3 = (int)(gen * 360.0 / total), a4 = 360 - a1 - a2 - a3;
                int sA = 90;
                g2.setColor(SAGE); g2.fillArc(x, y, size, size, sA, a1); sA += a1;
                g2.setColor(INK); g2.fillArc(x, y, size, size, sA, a2); sA += a2;
                g2.setColor(GOLD); g2.fillArc(x, y, size, size, sA, a3); sA += a3;
                g2.setColor(TERRACOTTA); g2.fillArc(x, y, size, size, sA, a4);
                
                int hole = (int)(size * 0.62);
                g2.setColor(new Color(255, 252, 246)); 
                g2.fillOval(x + (size - hole) / 2, y + (size - hole) / 2, hole, hole);
                
                g2.setColor(INK); g2.setFont(BODY_BOLD);
                FontMetrics fm = g2.getFontMetrics();
                String t1 = total + "";
                g2.drawString(t1, x + size/2 - fm.stringWidth(t1)/2, y + size/2 - 2);
                g2.setFont(SMALL); fm = g2.getFontMetrics();
                g2.drawString("titles", x + size/2 - fm.stringWidth("titles")/2, y + size/2 + 12);
                
                int lx = x + size + 20;
                int legendH = 4 * 18 - 8;
                int ly = y + (size - legendH) / 2;
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2.setColor(SAGE); g2.fillRoundRect(lx, ly, 10, 10, 4, 4); g2.setColor(INK); g2.drawString("Academic (" + acad + ")", lx + 18, ly + 9); ly += 18;
                g2.setColor(INK); g2.fillRoundRect(lx, ly, 10, 10, 4, 4); g2.setColor(INK); g2.drawString("Reference (" + ref + ")", lx + 18, ly + 9); ly += 18;
                g2.setColor(GOLD); g2.fillRoundRect(lx, ly, 10, 10, 4, 4); g2.setColor(INK); g2.drawString("General (" + gen + ")", lx + 18, ly + 9); ly += 18;
                g2.setColor(TERRACOTTA); g2.fillRoundRect(lx, ly, 10, 10, 4, 4); g2.setColor(INK); g2.drawString("Fiction (" + fic + ")", lx + 18, ly + 9);
                g2.dispose();
            }
        };
        chartPanel.setPreferredSize(new Dimension(300, 255));
        chartPanel.setLayout(new BorderLayout());
        chartPanel.setBorder(new EmptyBorder(16, 18, 16, 18));
        chartPanel.add(widgetHeader("Categories", "By title count"), BorderLayout.NORTH);
        
        gbc.weightx = 0.4; gbc.gridx = 1; gbc.insets = new Insets(0, 6, 0, 0);
        row1.add(chartPanel, gbc);
        
        content.add(row1);
        content.add(Box.createVerticalStrut(14));

        // ---------- ROW 2: OVERDUE (50) & DUE THIS WEEK (50) ----------
        JPanel row2 = new JPanel(new GridLayout(1, 2, 12, 0));
        row2.setOpaque(false);
        row2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        GlassPanel overdueList = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170));
        overdueList.setLayout(new BoxLayout(overdueList, BoxLayout.Y_AXIS));
        overdueList.setBorder(new EmptyBorder(16, 18, 16, 18));
        overdueList.add(widgetHeader("Action Needed", "Overdue returns"));
        overdueList.add(Box.createVerticalStrut(10));
        int ovrCount = 0;
        for (LibRecord r : records) {
            if (!r.isReturned()) {
                long ds = java.time.temporal.ChronoUnit.DAYS.between(r.getIssueDate(), today);
                int late = (int)ds - r.getAllowedDays();
                if (late > 0) {
                    JPanel row = new JPanel(new BorderLayout());
                    row.setOpaque(false);
                    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                    String titleStr = r.getBook().getTitle();
                    String studentStr = r.getStudent().getName().split(" ")[0];
                    JLabel lblInfo = new JLabel("<html><b>" + titleStr + "</b> <span style='color: #8C8075;'>· " + studentStr + "</span></html>");
                    lblInfo.setFont(SMALL);
                    double fineAmt = LibRecord.calculateFineAmount(late, r.getBook() != null ? r.getBook().getCategory() : "General");
                    JLabel lblDetails = new JLabel(late + "d (₹" + (int)fineAmt + ")  ");
                    lblDetails.setFont(SMALL_BOLD);
                    lblDetails.setForeground(TERRACOTTA);
                    JButton btnReturn = new JButton("Return");
                    btnReturn.setFont(new Font("SansSerif", Font.BOLD, 10));
                    btnReturn.setMargin(new Insets(2, 8, 2, 8));
                    btnReturn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btnReturn.setBackground(new Color(245, 240, 235));
                    btnReturn.setForeground(INK);
                    btnReturn.setFocusPainted(false);
                    btnReturn.addActionListener(e -> showPage("RETURN"));
                    JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
                    right.setOpaque(false);
                    right.add(lblDetails);
                    right.add(btnReturn);
                    row.add(lblInfo, BorderLayout.CENTER);
                    row.add(right, BorderLayout.EAST);
                    overdueList.add(row);
                    overdueList.add(Box.createVerticalStrut(8));
                    ovrCount++;
                    if (ovrCount >= 5) break;
                }
            }
        }
        if (ovrCount == 0) {
            JPanel emptyBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
            emptyBox.setOpaque(false);
            JLabel checkIcon = new JLabel("✓") {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(105, 126, 98, 40));
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    super.paintComponent(g);
                    g2.dispose();
                }
            };
            checkIcon.setPreferredSize(new Dimension(20, 20));
            checkIcon.setHorizontalAlignment(SwingConstants.CENTER);
            checkIcon.setFont(new Font("SansSerif", Font.BOLD, 11));
            checkIcon.setForeground(SAGE);
            checkIcon.setOpaque(false);
            
            JLabel empty = new JLabel("No books are currently overdue · All loans on time");
            empty.setFont(SMALL);
            empty.setForeground(SAGE);
            
            emptyBox.add(checkIcon);
            emptyBox.add(empty);
            overdueList.add(emptyBox);
        }
        row2.add(overdueList);
        
        GlassPanel duePanel = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170));
        duePanel.setLayout(new BoxLayout(duePanel, BoxLayout.Y_AXIS));
        duePanel.setBorder(new EmptyBorder(16, 18, 16, 18));
        duePanel.add(widgetHeader("Due Soon", "Next 7 days"));
        duePanel.add(Box.createVerticalStrut(10));
        int dueCount = 0;
        for (LibRecord r : records) {
            if (!r.isReturned()) {
                java.time.LocalDate dueDate = r.getIssueDate().plusDays(r.getAllowedDays());
                long daysUntil = java.time.temporal.ChronoUnit.DAYS.between(today, dueDate);
                if (daysUntil >= 0 && daysUntil <= 7) {
                    JPanel row = new JPanel(new BorderLayout());
                    row.setOpaque(false);
                    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
                    String titleStr = r.getBook().getTitle();
                    String studentStr = r.getStudent().getName().split(" ")[0];
                    JLabel lblInfo = new JLabel("<html><b>" + titleStr + "</b> <span style='color: #8C8075;'>· " + studentStr + "</span></html>");
                    lblInfo.setFont(SMALL);
                    String daysStr = daysUntil == 0 ? "Today" : "In " + daysUntil + "d";
                    JLabel lblDays = new JLabel(" " + daysStr + " ") {
                        @Override protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(daysUntil <= 1 ? new Color(184, 98, 77, 40) : new Color(175, 138, 70, 40));
                            g2.fillRoundRect(0, 2, getWidth(), getHeight() - 4, 6, 6);
                            super.paintComponent(g);
                            g2.dispose();
                        }
                    };
                    lblDays.setFont(SMALL_BOLD);
                    lblDays.setForeground(daysUntil <= 1 ? new Color(180, 60, 45) : new Color(150, 110, 30));
                    row.add(lblInfo, BorderLayout.CENTER);
                    row.add(lblDays, BorderLayout.EAST);
                    duePanel.add(row);
                    duePanel.add(Box.createVerticalStrut(8));
                    dueCount++;
                    if (dueCount >= 5) break;
                }
            }
        }
        if (dueCount == 0) {
            JPanel emptyDueBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
            emptyDueBox.setOpaque(false);
            JLabel checkIcon2 = new JLabel("✓") {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(105, 126, 98, 40));
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    super.paintComponent(g);
                    g2.dispose();
                }
            };
            checkIcon2.setPreferredSize(new Dimension(20, 20));
            checkIcon2.setHorizontalAlignment(SwingConstants.CENTER);
            checkIcon2.setFont(new Font("SansSerif", Font.BOLD, 11));
            checkIcon2.setForeground(SAGE);
            checkIcon2.setOpaque(false);
            
            JLabel empty = new JLabel("No books due in the next 7 days · Schedule clear");
            empty.setFont(SMALL);
            empty.setForeground(SAGE);
            
            emptyDueBox.add(checkIcon2);
            emptyDueBox.add(empty);
            duePanel.add(emptyDueBox);
        }
        row2.add(duePanel);
        
        content.add(row2);
        content.add(Box.createVerticalStrut(14));

        // ---------- ROW 3: MOST BORROWED (50) & RECENT ACTIVITY (50) ----------
        JPanel row3 = new JPanel(new GridLayout(1, 2, 12, 0));
        row3.setOpaque(false);
        row3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
        row3.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        GlassPanel popularPanel = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170));
        popularPanel.setLayout(new BoxLayout(popularPanel, BoxLayout.Y_AXIS));
        popularPanel.setBorder(new EmptyBorder(16, 18, 16, 18));
        popularPanel.add(widgetHeader("Popular Books", "Most borrowed"));
        popularPanel.add(Box.createVerticalStrut(10));
        java.util.Map<String, Integer> bookCounts = new java.util.HashMap<>();
        for (LibRecord r : records) bookCounts.put(r.getBook().getId(), bookCounts.getOrDefault(r.getBook().getId(), 0) + 1);
        java.util.List<java.util.Map.Entry<String, Integer>> popBooks = new java.util.ArrayList<>(bookCounts.entrySet());
        popBooks.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
        int topCheckIdx = Math.min(popBooks.size() - 1, 4);
        int popMax = popBooks.isEmpty() ? 1 : popBooks.get(0).getValue();
        int popMin = popBooks.isEmpty() ? 1 : popBooks.get(topCheckIdx).getValue();
        boolean hasVariation = popBooks.size() > 1 && popMax > popMin;
        int popCount = 0;
        for (java.util.Map.Entry<String, Integer> entry : popBooks) {
            Book b = null;
            for (Book temp : books) if (temp.getId().equals(entry.getKey())) { b = temp; break; }
            if (b != null) {
                JPanel row = new JPanel(new BorderLayout());
                row.setOpaque(false);
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
                
                JPanel leftGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
                leftGroup.setOpaque(false);
                
                int currentRank = popCount + 1;
                if (hasVariation) {
                    JLabel rankLbl = new JLabel("#" + currentRank, SwingConstants.CENTER) {
                        @Override protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            if (currentRank == 1) {
                                g2.setColor(new Color(212, 175, 55, 65));
                            } else if (currentRank == 2) {
                                g2.setColor(new Color(192, 192, 192, 90));
                            } else if (currentRank == 3) {
                                g2.setColor(new Color(205, 127, 50, 50));
                            } else {
                                g2.setColor(new Color(225, 218, 210, 80));
                            }
                            g2.fillRoundRect(0, 4, getWidth(), getHeight() - 8, 6, 6);
                            super.paintComponent(g);
                            g2.dispose();
                        }
                    };
                    rankLbl.setFont(SMALL_BOLD);
                    if (currentRank == 1) {
                        rankLbl.setForeground(new Color(150, 115, 30));
                    } else if (currentRank == 2) {
                        rankLbl.setForeground(new Color(110, 110, 110));
                    } else if (currentRank == 3) {
                        rankLbl.setForeground(new Color(160, 90, 40));
                    } else {
                        rankLbl.setForeground(MUTED);
                    }
                    rankLbl.setPreferredSize(new Dimension(24, 36));
                    leftGroup.add(rankLbl);
                }
                
                JPanel cover = createCover(b);
                cover.setPreferredSize(new Dimension(24, 36));
                leftGroup.add(cover);
                
                JPanel centerCol = new JPanel();
                centerCol.setLayout(new BoxLayout(centerCol, BoxLayout.Y_AXIS));
                centerCol.setOpaque(false);
                centerCol.setBorder(new EmptyBorder(0, 8, 0, 10));
                JLabel lblTitle = new JLabel("<html><div style='width: 140px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + b.getTitle() + "</div></html>");
                lblTitle.setFont(SMALL); lblTitle.setForeground(INK);
                
                int barW = (int)((entry.getValue() / (double)popMax) * 100);
                JPanel progressBarPanel = new JPanel() {
                    @Override protected void paintComponent(Graphics g) {
                        if (hasVariation) {
                            g.setColor(new Color(220, 215, 210)); g.fillRoundRect(0, 0, 100, 4, 2, 2);
                            g.setColor(GOLD); g.fillRoundRect(0, 0, barW, 4, 2, 2);
                        }
                    }
                };
                progressBarPanel.setPreferredSize(new Dimension(100, 4)); progressBarPanel.setMaximumSize(new Dimension(100, 4));
                progressBarPanel.setOpaque(false); progressBarPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                centerCol.add(lblTitle); 
                centerCol.add(Box.createVerticalStrut(4)); 
                if (hasVariation) {
                    centerCol.add(progressBarPanel);
                }
                
                int issueVal = entry.getValue();
                JLabel lblCount = new JLabel(issueVal + (issueVal == 1 ? " issue" : " issues"));
                lblCount.setFont(new Font("SansSerif", Font.PLAIN, 10)); lblCount.setForeground(MUTED);
                
                row.add(leftGroup, BorderLayout.WEST); 
                row.add(centerCol, BorderLayout.CENTER); 
                row.add(lblCount, BorderLayout.EAST);
                popularPanel.add(row); popularPanel.add(Box.createVerticalStrut(8));
                popCount++; if (popCount >= 5) break;
            }
        }
        if (popCount == 0) {
            JLabel empty = new JLabel("No borrow history yet.");
            empty.setFont(SMALL); empty.setForeground(MUTED);
            popularPanel.add(empty);
        }
        row3.add(popularPanel);
        
        GlassPanel activityPanel = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170));
        activityPanel.setLayout(new BoxLayout(activityPanel, BoxLayout.Y_AXIS));
        activityPanel.setBorder(new EmptyBorder(16, 18, 16, 18));
        activityPanel.add(widgetHeader("Recent Activity", "Latest transactions"));
        activityPanel.add(Box.createVerticalStrut(10));
        int limit = 0;
        for (int i = records.size() - 1; i >= 0 && limit < 8; i--) {
            LibRecord r = records.get(i);
            JPanel row = new JPanel(new BorderLayout(8, 0));
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
            
            // Avatar with action type color
            String stName = r.getStudent().getName();
            String initial = (stName != null && !stName.trim().isEmpty()) ? stName.trim().substring(0, 1) : "S";
            boolean isRet = r.isReturned();
            boolean hasFine = isRet && r.getFine() > 0;
            Color avatarBg = hasFine ? new Color(220, 80, 70, 45) : (isRet ? new Color(105, 126, 98, 45) : new Color(184, 98, 77, 45));
            Color avatarFg = hasFine ? new Color(190, 50, 40) : (isRet ? SAGE : TERRACOTTA);
            JPanel avatar = createAvatar(initial, avatarBg, avatarFg, 24);
            row.add(avatar, BorderLayout.WEST);
            
            // Center description with highlighted action verb
            String actionVerb = isRet ? (hasFine ? "<span style='color: #B23B2A; font-weight: bold;'>returned (late)</span>" : "<span style='color: #4C6546; font-weight: bold;'>returned</span>") : "<span style='color: #A34E35; font-weight: bold;'>issued</span>";
            String actText = r.getStudent().getName().split(" ")[0] + " " + actionVerb + " " + r.getBook().getTitle();
            JLabel lblAct = new JLabel("<html><div style='width: 155px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + actText + "</div></html>");
            lblAct.setFont(SMALL); lblAct.setForeground(INK);
            row.add(lblAct, BorderLayout.CENTER);
            
            // Right relative time and prominent fine badge
            JPanel rightGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
            rightGroup.setOpaque(false);
            
            java.time.LocalDate eventDate = isRet ? r.getIssueDate().plusDays(r.getActualDays()) : r.getIssueDate();
            long daysAgo = java.time.temporal.ChronoUnit.DAYS.between(eventDate, today);
            String relTime;
            if (daysAgo <= 0) {
                if (limit == 0) relTime = "2h ago";
                else if (limit == 1) relTime = "5h ago";
                else relTime = "Today";
            }
            else if (daysAgo == 1) relTime = "Yesterday";
            else if (daysAgo < 7) relTime = daysAgo + "d ago";
            else relTime = eventDate.format(java.time.format.DateTimeFormatter.ofPattern("d MMM"));
            
            JLabel lblTime = new JLabel(relTime);
            lblTime.setFont(new Font("SansSerif", Font.PLAIN, 10));
            lblTime.setForeground(MUTED);
            rightGroup.add(lblTime);
            
            if (hasFine) {
                JLabel fineBadge = new JLabel(" ₹" + (int)r.getFine() + " ") {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(210, 65, 55));
                        g2.fillRoundRect(0, 1, getWidth(), getHeight() - 2, 6, 6);
                        super.paintComponent(g);
                        g2.dispose();
                    }
                };
                fineBadge.setFont(new Font("SansSerif", Font.BOLD, 10));
                fineBadge.setForeground(WHITE);
                fineBadge.setOpaque(false);
                fineBadge.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
                rightGroup.add(fineBadge);
            }
            
            row.add(rightGroup, BorderLayout.EAST);
            activityPanel.add(row);
            activityPanel.add(Box.createVerticalStrut(8));
            limit++;
        }
        if (limit == 0) {
            JLabel empty = new JLabel("No recent activity.");
            empty.setFont(SMALL); empty.setForeground(MUTED);
            activityPanel.add(empty);
        }
        row3.add(activityPanel);
        
        content.add(row3);
        content.add(Box.createVerticalStrut(20));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        page.add(scroll, BorderLayout.CENTER);

        return page;
    }
    
    JPanel largeQuickAction(String titleStr, String subStr, String iconName, String pageCmd, boolean primary) {
        GlassPanel q = new GlassPanel(primary ? TERRACOTTA : new Color(255, 252, 246, 225), 
                                      primary ? new Color(255, 255, 255, 10) : new Color(255, 255, 255, 170));
        q.setLayout(new BorderLayout());
        q.setBorder(new EmptyBorder(16, 20, 16, 20));
        
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        
        JLabel t = new JLabel(titleStr);
        t.setIcon(createLineIcon(iconName, 18, primary ? WHITE : INK));
        t.setIconTextGap(10);
        t.setFont(BODY_BOLD);
        t.setForeground(primary ? WHITE : INK);
        
        JLabel s = new JLabel(subStr);
        s.setFont(SMALL);
        s.setForeground(primary ? new Color(255, 255, 255, 200) : MUTED);
        s.setBorder(new EmptyBorder(0, 28, 0, 0));
        
        textPanel.add(t);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(s);
        
        q.add(textPanel, BorderLayout.CENTER);
        
        JLabel arrow = new JLabel("→");
        arrow.setFont(BODY_BOLD);
        arrow.setForeground(primary ? WHITE : MUTED);
        q.add(arrow, BorderLayout.EAST);
        
        q.setCursor(new Cursor(Cursor.HAND_CURSOR));
        q.addMouseListener(new java.awt.event.MouseAdapter() {
            Border n = new EmptyBorder(16, 20, 16, 20);
            Border h = new EmptyBorder(13, 20, 19, 20);
            public void mouseEntered(java.awt.event.MouseEvent e) { q.setBorder(h); }
            public void mouseExited(java.awt.event.MouseEvent e) { q.setBorder(n); }
            public void mouseClicked(java.awt.event.MouseEvent e) { showPage(pageCmd); }
        });
        
        return q;
    }

    JPanel widgetHeader(String titleStr, String subStr) {
        JPanel h = new JPanel(new BorderLayout());
        h.setOpaque(false);
        h.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        JLabel t = new JLabel(titleStr);
        t.setFont(TITLE); t.setForeground(INK);
        JLabel s = new JLabel(subStr);
        s.setFont(SMALL); s.setForeground(MUTED);
        h.add(t, BorderLayout.WEST);
        h.add(s, BorderLayout.EAST);
        return h;
    }
    
    JPanel quickAction(String titleStr, String subStr, String iconStr, String page) {
        GlassPanel q = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170));
        q.setLayout(new BorderLayout());
        q.setBorder(new EmptyBorder(12, 16, 12, 16));
        
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        
        JLabel t = new JLabel(iconStr + "  " + titleStr);
        t.setFont(SMALL_BOLD);
        t.setForeground(INK);
        
        JLabel s = new JLabel(subStr);
        s.setFont(SMALL);
        s.setForeground(MUTED);
        
        textPanel.add(t);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(s);
        
        q.add(textPanel, BorderLayout.WEST);
        
        q.setCursor(new Cursor(Cursor.HAND_CURSOR));
        q.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showPage(page);
            }
        });
        
        return q;
    }
JPanel statCard(String heading, String value, String caption, Color accent) {
        return statCard(heading, value, caption, accent, null, null);
    }

    JPanel statCard(String heading, String value, String caption, Color accent, String iconName) {
        return statCard(heading, value, caption, accent, iconName, null);
    }

    JPanel statCard(String heading, String value, String caption, Color accent, String iconName, String pageCmd) {
        return statCard(heading, value, caption, accent, iconName, pageCmd, null);
    }

    JPanel statCard(String heading, String value, String caption, Color accent, String iconName, String pageCmd, Runnable onClick) {
        boolean isInteractive = (pageCmd != null || onClick != null);
        GlassPanel card = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170)) {
            private boolean hovered = false;
            {
                if (isInteractive) {
                    addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override public void mouseEntered(java.awt.event.MouseEvent e) { hovered = true; repaint(); }
                        @Override public void mouseExited(java.awt.event.MouseEvent e) { hovered = false; repaint(); }
                    });
                }
            }
            @Override protected void paintComponent(Graphics g) {
                if (hovered && isInteractive) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    // Subtle elevated warm fill
                    g2.setColor(new Color(255, 255, 255, 240));
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                    // Accent border highlight
                    g2.setColor(accent != null ? new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 120) : SAND);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                    g2.dispose();
                } else {
                    super.paintComponent(g);
                }
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(14, 18, 14, 18));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        topRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        JLabel h = new JLabel(heading);
        if (iconName != null && !iconName.isEmpty()) {
            h.setIcon(createLineIcon(iconName, 14, accent != null ? accent : MUTED));
            h.setIconTextGap(6);
        }
        h.setFont(SMALL_BOLD);
        h.setForeground(MUTED);
        topRow.add(h, BorderLayout.WEST);

        if (isInteractive) {
            JLabel arrow = new JLabel("↗");
            arrow.setFont(new Font("SansSerif", Font.BOLD, 12));
            arrow.setForeground(new Color(190, 180, 170));
            topRow.add(arrow, BorderLayout.EAST);
        }

        JLabel v = new JLabel(value);
        v.setFont(customSerif.deriveFont(Font.BOLD, 27f));
        v.setForeground(accent);
        v.setAlignmentX(Component.LEFT_ALIGNMENT);
        v.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel c = new JLabel(caption);
        c.setFont(SMALL);
        c.setForeground(MUTED);
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setHorizontalAlignment(SwingConstants.LEFT);

        card.add(topRow);
        card.add(Box.createVerticalStrut(4));
        card.add(v);
        card.add(Box.createVerticalStrut(2));
        card.add(c);

        if (isInteractive) {
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            card.setToolTipText("Click to view filtered " + heading.toLowerCase() + " details");
            card.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) { 
                    if (onClick != null) {
                        onClick.run();
                    } else if (pageCmd != null) {
                        showPage(pageCmd); 
                    }
                }
            });
        }

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

    File findCoverFile(String bookId) {
        String fileName = bookId + ".png";
        String[] directPaths = {
            "assets/" + fileName, "LibraryFineManagementSystem/assets/" + fileName,
            "../assets/" + fileName, "../LibraryFineManagementSystem/assets/" + fileName, "../../assets/" + fileName
        };
        for (String path : directPaths) {
            File file = new File(path);
            if (file.exists() && file.isFile()) return file;
        }
        File current = new File(System.getProperty("user.dir"));
        for (int i = 0; i < 6 && current != null; i++) {
            File file = new File(current, "assets/" + fileName);
            if (file.exists() && file.isFile()) return file;
            file = new File(current, "LibraryFineManagementSystem/assets/" + fileName);
            if (file.exists() && file.isFile()) return file;
            current = current.getParentFile();
        }
        try {
            File classLocation = new File(GUI.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (classLocation.isFile()) classLocation = classLocation.getParentFile();
            for (int i = 0; i < 6 && classLocation != null; i++) {
                File file = new File(classLocation, "assets/" + fileName);
                if (file.exists() && file.isFile()) return file;
                file = new File(classLocation, "LibraryFineManagementSystem/assets/" + fileName);
                if (file.exists() && file.isFile()) return file;
                classLocation = classLocation.getParentFile();
            }
        } catch (Exception ignored) {}
        return null;
    }


    JPanel createCover(Book book) {
        File imageFile = findCoverFile(book.getId());
        BufferedImage coverImage = null;
        try {
            if (imageFile != null) coverImage = ImageIO.read(imageFile);
        } catch (IOException e) {
            System.out.println("Could not load cover: " + book.getId());
        }
        final BufferedImage finalCoverImage = coverImage;
        JPanel cover = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        int panelWidth =
                                getWidth();

                        int panelHeight = getHeight();

                        int pad = 0; // Remove olive frame padding
                        int pw = panelWidth - 8; // Leave space for shadow
                        int ph = panelHeight - 8;
                        
                        // Soft shadow
                        g2.setColor(new Color(0, 0, 0, 15));
                        g2.fillRoundRect(2, 2, pw, ph, 8, 8);
                        g2.setColor(new Color(0, 0, 0, 10));
                        g2.fillRoundRect(4, 4, pw, ph, 8, 8);

                        java.awt.Shape clip = new java.awt.geom.RoundRectangle2D.Float(0, 0, pw, ph, 8, 8);
                        g2.setClip(clip);

                        g2.setColor(CREAM);
                        g2.fillRect(0, 0, pw, ph);

                        if (finalCoverImage != null) {
                            int imageWidth = finalCoverImage.getWidth();
                            int imageHeight = finalCoverImage.getHeight();
                            double scale = Math.min((double) pw / imageWidth, (double) ph / imageHeight);
                            int drawWidth = Math.max(1, (int) Math.ceil(imageWidth * scale));
                            int drawHeight = Math.max(1, (int) Math.ceil(imageHeight * scale));
                            int x = (pw - drawWidth) / 2;
                            int y = (ph - drawHeight) / 2;
                            g2.drawImage(finalCoverImage, x, y, drawWidth, drawHeight, null);
                        } else {
                            // Styled book jacket for custom books without a pre-rendered cover image
                            Color baseCol = categoryColor(book.getCategory());
                            g2.setColor(baseCol);
                            g2.fillRect(0, 0, pw, ph);

                            // Subtle spine crease effect on the left
                            g2.setColor(new Color(0, 0, 0, 35));
                            g2.fillRect(0, 0, 10, ph);
                            g2.setColor(new Color(255, 255, 255, 25));
                            g2.fillRect(10, 0, 2, ph);

                            // Decorative inner frame
                            g2.setColor(new Color(255, 255, 255, 40));
                            g2.drawRoundRect(16, 12, pw - 24, ph - 24, 6, 6);

                            // Book category badge top
                            g2.setFont(new Font("SansSerif", Font.BOLD, 8));
                            FontMetrics mBadge = g2.getFontMetrics();
                            String catBadge = (book.getCategory() == null ? "GENERAL" : book.getCategory().toUpperCase());
                            g2.setColor(new Color(255, 255, 255, 180));
                            g2.drawString(catBadge, 16 + (pw - 24 - mBadge.stringWidth(catBadge)) / 2, 28);

                            // Title lines (wrapped)
                            g2.setColor(WHITE);
                            g2.setFont(customSerif != null ? customSerif.deriveFont(Font.BOLD, 12f) : new Font("Serif", Font.BOLD, 12));
                            FontMetrics mTitle = g2.getFontMetrics();
                            String bTitle = book.getTitle();
                            java.util.List<String> titleWords = java.util.Arrays.asList(bTitle.split(" "));
                            java.util.List<String> titleLines = new java.util.ArrayList<>();
                            StringBuilder curL = new StringBuilder();
                            for (String wStr : titleWords) {
                                String testL = curL.length() == 0 ? wStr : curL + " " + wStr;
                                if (mTitle.stringWidth(testL) <= (pw - 32) || curL.length() == 0) {
                                    curL = new StringBuilder(testL);
                                } else {
                                    titleLines.add(curL.toString());
                                    curL = new StringBuilder(wStr);
                                    if (titleLines.size() >= 3) break;
                                }
                            }
                            if (curL.length() > 0 && titleLines.size() < 4) titleLines.add(curL.toString());

                            int startY = 48;
                            for (String tLine : titleLines) {
                                int tX = 16 + (pw - 24 - mTitle.stringWidth(tLine)) / 2;
                                g2.drawString(tLine, tX, startY);
                                startY += mTitle.getHeight();
                            }

                            // Thin divider
                            g2.setColor(new Color(255, 255, 255, 70));
                            int divY = Math.max(startY + 4, ph / 2 + 10);
                            g2.drawLine(24, divY, pw - 16, divY);

                            // Author name bottom
                            g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
                            g2.setColor(new Color(255, 255, 255, 220));
                            FontMetrics mAuth = g2.getFontMetrics();
                            String authStr = book.getAuthor() != null && !book.getAuthor().isEmpty() ? book.getAuthor() : "Unknown Author";
                            if (mAuth.stringWidth(authStr) > pw - 28) {
                                while (authStr.length() > 3 && mAuth.stringWidth(authStr + "…") > pw - 28) {
                                    authStr = authStr.substring(0, authStr.length() - 1);
                                }
                                authStr = authStr + "…";
                            }
                            g2.drawString(authStr, 16 + (pw - 24 - mAuth.stringWidth(authStr)) / 2, ph - 20);
                        }
                        
                        g2.setClip(null);
                        
                        // Inner border for polish
                        g2.setColor(new Color(0, 0, 0, 20));
                        g2.drawRoundRect(0, 0, pw, ph, 8, 8);
                        g2.dispose();
                    }
                };

        cover.setOpaque(false);
        return cover;
    }


    Color categoryColor(String category) {
        if (category.equalsIgnoreCase("Academic")) return SAGE;
        if (category.equalsIgnoreCase("Reference")) return INK;
        if (category.equalsIgnoreCase("Fiction")) return TERRACOTTA;
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

        int totalBookCopies = 0;
        for (Book b : books) totalBookCopies += b.getTotalCopies();
        JLabel sub = new JLabel(books.size() + " titles · " + totalBookCopies + " copies");
        sub.setFont(SMALL);
        sub.setForeground(MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(sub);
        
        header.add(titlePanel, BorderLayout.NORTH);
        header.add(Box.createVerticalStrut(20), BorderLayout.CENTER);

        // Filters and Search
        JPanel actionsPanel = new JPanel(new BorderLayout());
        actionsPanel.setOpaque(false);
        
        // Category Chips
        String[] categories = {"All", "Academic", "Reference", "General", "Fiction"};
        JPanel chipsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        chipsPanel.setOpaque(false);
        String[] activeCategory = {"All"};
        java.util.List<JButton> chipButtons = new java.util.ArrayList<>();
        Runnable[] applyFilters = new Runnable[1];
        
        for (String cat : categories) {
            int count = 0;
            if (cat.equals("All")) count = books.size();
            else {
                for (Book b : books) {
                    if (b.getCategory().equalsIgnoreCase(cat)) count++;
                }
            }
            String chipText = cat.equals("All") ? "All " + count : cat + " " + count;
            JButton chip = new JButton(chipText);
            chip.setFont(SMALL_BOLD);
            chip.setForeground(cat.equals("All") ? WHITE : MUTED);
            chip.setBackground(cat.equals("All") ? TERRACOTTA : CREAM);
            chip.setOpaque(true);
            chip.setBorderPainted(true);
            chip.setBorder(BorderFactory.createCompoundBorder(new LineBorder(SAND, 1, true), new EmptyBorder(6, 14, 6, 14)));
            chip.setFocusPainted(false);
            chip.setCursor(new Cursor(Cursor.HAND_CURSOR));
            chip.addActionListener(e -> {
                activeCategory[0] = cat;
                for (JButton b : chipButtons) {
                    boolean isActive = b.getText().startsWith(cat + " ") || (cat.equals("All") && b.getText().startsWith("All "));
                    b.setForeground(isActive ? WHITE : MUTED);
                    b.setBackground(isActive ? TERRACOTTA : CREAM);
                }
                if (applyFilters[0] != null) applyFilters[0].run();
            });
            chipButtons.add(chip);
            chipsPanel.add(chip);
        }
        actionsPanel.add(chipsPanel, BorderLayout.WEST);
        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightActions.setOpaque(false);
        
        // Sort Menu
        String[] sortOptions = {"Sort by Title", "Sort by Author", "Sort by Availability"};
        JComboBox<String> sortMenu = new JComboBox<>(sortOptions);
        sortMenu.setUI(new javax.swing.plaf.basic.BasicComboBoxUI());
        sortMenu.setBackground(CREAM);
        sortMenu.setBorder(BorderFactory.createCompoundBorder(new LineBorder(SAND, 1, true), new EmptyBorder(4, 8, 4, 8)));
        sortMenu.setFont(SMALL);
        rightActions.add(sortMenu);

        // Search box
        JPanel searchBox = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                if (((JTextField)((BorderLayout)getLayout()).getLayoutComponent(BorderLayout.CENTER)).hasFocus()) {
                    g2.setColor(TERRACOTTA);
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
                    int y = (getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();
                    g2.drawString("Search by title, author or ISBN...", getInsets().left, y);
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
        
        rightActions.add(searchBox);

        JButton addBookBtn = new JButton("+ Add Book");
        addBookBtn.setFont(SMALL_BOLD);
        addBookBtn.setForeground(WHITE);
        addBookBtn.setBackground(TERRACOTTA);
        addBookBtn.setOpaque(true);
        addBookBtn.setBorderPainted(false);
        addBookBtn.setFocusPainted(false);
        addBookBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBookBtn.addActionListener(e -> showAddBookDialog(page));
        rightActions.add(addBookBtn);
        
        actionsPanel.add(rightActions, BorderLayout.EAST);

        JPanel headerBottom = new JPanel(new BorderLayout());
        headerBottom.setOpaque(false);
        headerBottom.setBorder(new EmptyBorder(20, 0, 0, 0));
        JLabel resultsCount = new JLabel("Showing " + books.size() + " of " + books.size() + " titles");
        resultsCount.setFont(SMALL);
        resultsCount.setForeground(MUTED);
        headerBottom.add(resultsCount, BorderLayout.WEST);
        
        JPanel filtersAndCount = new JPanel(new BorderLayout());
        filtersAndCount.setOpaque(false);
        filtersAndCount.add(actionsPanel, BorderLayout.CENTER);
        filtersAndCount.add(headerBottom, BorderLayout.SOUTH);
        
        header.add(filtersAndCount, BorderLayout.SOUTH);
        page.add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 6, 20, 32)); // 6 covers per row
        grid.setBackground(CREAM);
        grid.setBorder(new EmptyBorder(10, 35, 30, 35));

        applyFilters[0] = () -> {
            grid.removeAll();
            String query = search.getText().toLowerCase().trim();
            if (query.equals("search by title, author or isbn...")) query = "";
            
            java.util.List<Book> filtered = new java.util.ArrayList<>();
            String cat = activeCategory[0];
            
            for (Book book : books) {
                boolean matchesCat = false;
                String bc = book.getCategory();
                if (cat.equals("All")) matchesCat = true;
                else matchesCat = bc.equalsIgnoreCase(cat);
                
                boolean matchesQuery = true;
                if (!query.isEmpty()) {
                    matchesQuery = book.getTitle().toLowerCase().contains(query) || book.getAuthor().toLowerCase().contains(query);
                }
                
                boolean matchesLoanMode = true;
                if ("ON_LOAN".equals(booksFilterMode)) {
                    matchesLoanMode = (book.getTotalCopies() - book.getAvailableCopies()) > 0;
                }
                
                if (matchesCat && matchesQuery && matchesLoanMode) {
                    filtered.add(book);
                }
            }
            
            int sortIdx = sortMenu.getSelectedIndex();
            if (sortIdx == 0) {
                filtered.sort(java.util.Comparator.comparing(Book::getTitle));
            } else if (sortIdx == 1) {
                filtered.sort(java.util.Comparator.comparing(Book::getAuthor));
            } else if (sortIdx == 2) {
                filtered.sort((a, b) -> Integer.compare(b.getAvailableCopies(), a.getAvailableCopies()));
            }
            
            for (Book book : filtered) {
                grid.add(bookCard(book));
            }
            
            if (filtered.isEmpty()) {
                JLabel empty = new JLabel("ON_LOAN".equals(booksFilterMode) ? "No books are currently on loan." : "No books match your search");
                empty.setFont(BODY);
                empty.setForeground(MUTED);
                grid.add(empty);
            }
            
            if ("ON_LOAN".equals(booksFilterMode)) {
                resultsCount.setText("Showing " + filtered.size() + " books currently on loan  •  [Filtered by On Loan]");
            } else {
                resultsCount.setText("Showing " + filtered.size() + " of " + books.size() + " titles");
            }
            grid.revalidate();
            grid.repaint();
        };

        applyFilters[0].run();

        search.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                applyFilters[0].run();
            }
        });
        
        sortMenu.addActionListener(e -> applyFilters[0].run());

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
        
        JPanel overlayBtns = new JPanel(new GridLayout(3, 1, 0, 7));
        overlayBtns.setOpaque(false);
        JButton details = smallButton("Details");
        details.addActionListener(e -> showBookDetails(book));
        JButton issue = smallButton("Issue");
        issue.setBackground(TERRACOTTA);
        issue.setForeground(WHITE);
        issue.addActionListener(e -> {
            preselectedBook = book.getTitle();
            showPage("ISSUE");
        });
        JButton delete = smallButton("Delete");
        delete.setBackground(new Color(180, 60, 60));
        delete.setForeground(WHITE);
        delete.addActionListener(e -> showDeleteBookConfirm(book));
        overlayBtns.add(details);
        overlayBtns.add(issue);
        overlayBtns.add(delete);
        overlay.add(overlayBtns);
        
        // 2. Main Content
        GlassPanel card = new GlassPanel(new Color(255, 252, 246, 225), new Color(255, 255, 255, 170));
        card.setLayout(new BorderLayout());
        
        JPanel cover = createCover(book);
        cover.setPreferredSize(new Dimension(140, 210));
        
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(new EmptyBorder(12, 10, 15, 10));
        
        JLabel category = new JLabel(book.getCategory().toUpperCase());
        category.setFont(new Font("SansSerif", Font.BOLD, 9));
        category.setForeground(categoryColor(book.getCategory()));
        category.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel title = new JLabel("<html><div style='width:120px; text-align:center'>" + book.getTitle() + "</div></html>");
        title.setFont(BODY_BOLD);
        title.setForeground(INK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String authorText = book.getAuthor();
        if (authorText.length() > 18) authorText = authorText.substring(0, 15) + "...";
        JLabel author = new JLabel(authorText);
        author.setFont(SMALL);
        author.setForeground(MUTED);
        author.setToolTipText(book.getAuthor());
        author.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        int avail = book.getAvailableCopies();
        String availText = avail > 0 ? avail + " of " + book.getTotalCopies() + " available" : "Unavailable";
        JLabel availability = new JLabel(availText);
        availability.setFont(SMALL_BOLD);
        availability.setForeground(avail == 0 ? TERRACOTTA : (avail == 1 ? GOLD : SAGE));
        availability.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        info.add(title);
        info.add(Box.createVerticalStrut(4));
        info.add(category);
        info.add(Box.createVerticalStrut(4));
        info.add(author);
        info.add(Box.createVerticalGlue()); // Push availability to bottom
        info.add(availability);
        
        card.add(cover, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);
        
        MouseAdapter hoverAdapter = new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                overlay.setVisible(true);
                card.setBorder(new EmptyBorder(-2, 0, 2, 0));
                card.revalidate();
            }
            public void mouseExited(MouseEvent e) {
                // Only hide if the pointer truly left the container (not just moved into a child)
                Point p = e.getPoint();
                javax.swing.SwingUtilities.convertPointToScreen(p, (Component) e.getSource());
                javax.swing.SwingUtilities.convertPointFromScreen(p, container);
                if (!container.contains(p)) {
                    overlay.setVisible(false);
                    card.setBorder(null);
                    card.revalidate();
                }
            }
            public void mouseClicked(MouseEvent e) {
                // Only trigger book details if click was on the card background, not a button
                if (e.getSource() == card) showBookDetails(book);
            }
        };
        card.addMouseListener(hoverAdapter);
        overlay.addMouseListener(hoverAdapter);
        // Propagate hover-exit check to every button so moving between buttons doesn't hide overlay
        for (Component btn : overlayBtns.getComponents()) {
            btn.addMouseListener(new MouseAdapter() {
                public void mouseExited(MouseEvent e) {
                    Point p = e.getPoint();
                    javax.swing.SwingUtilities.convertPointToScreen(p, (Component) e.getSource());
                    javax.swing.SwingUtilities.convertPointFromScreen(p, container);
                    if (!container.contains(p)) {
                        overlay.setVisible(false);
                        card.setBorder(null);
                        card.revalidate();
                    }
                }
            });
        }
        
        container.add(overlay);
        container.add(card);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(container, BorderLayout.CENTER); // allow stretch to grid cell height
        
        return wrapper;
    }

    void showDeleteBookConfirm(Book book) {
        // Block deletion if any copies are currently on loan
        if (book.getIssuedCopies() > 0) {
            JOptionPane.showMessageDialog(
                this,
                "<html><b>Cannot delete \"" + book.getTitle() + "\"</b><br>" +
                book.getIssuedCopies() + " cop" + (book.getIssuedCopies() == 1 ? "y is" : "ies are") +
                " currently on loan.<br>All copies must be returned before deletion.</html>",
                "Book On Loan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Styled confirmation dialog
        JDialog dlg = new JDialog(this, "Delete Book", true);
        dlg.setSize(420, 220);
        dlg.setLocationRelativeTo(this);
        dlg.setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(CREAM);

        // Warning header
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(new Color(180, 60, 60));
        hdr.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel hdrLbl = new JLabel("\u26A0  Delete Book");
        hdrLbl.setFont(TITLE);
        hdrLbl.setForeground(WHITE);
        hdr.add(hdrLbl, BorderLayout.CENTER);
        main.add(hdr, BorderLayout.NORTH);

        // Message
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(18, 22, 10, 22));
        JLabel msg1 = new JLabel("<html>Are you sure you want to permanently delete:<br><b>\"" + book.getTitle() + "\"</b> by " + book.getAuthor() + "?</html>");
        msg1.setFont(BODY);
        msg1.setForeground(INK);
        JLabel msg2 = new JLabel("This action cannot be undone.");
        msg2.setFont(SMALL);
        msg2.setForeground(new Color(180, 60, 60));
        body.add(msg1);
        body.add(Box.createVerticalStrut(8));
        body.add(msg2);
        main.add(body, BorderLayout.CENTER);

        // Footer buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, SAND));

        JButton cancel = new JButton("Cancel");
        cancel.setFont(SMALL_BOLD);
        cancel.setForeground(MUTED);
        cancel.setBackground(CREAM);
        cancel.setOpaque(true);
        cancel.setBorderPainted(true);
        cancel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(SAND, 1, true), new EmptyBorder(6, 18, 6, 18)));
        cancel.setFocusPainted(false);
        cancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancel.addActionListener(e -> dlg.dispose());

        JButton confirm = new JButton("Yes, Delete") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(210, 50, 50) : new Color(180, 60, 60));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        confirm.setFont(SMALL_BOLD);
        confirm.setForeground(WHITE);
        confirm.setOpaque(false);
        confirm.setContentAreaFilled(false);
        confirm.setBorderPainted(false);
        confirm.setFocusPainted(false);
        confirm.setBorder(new EmptyBorder(7, 18, 7, 18));
        confirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirm.addActionListener(e -> {
            books.remove(book);
            saveData();
            dlg.dispose();
            showToast("\"" + book.getTitle() + "\" removed from the catalog.", new Color(180, 60, 60));
            rebuildPages();
            showPage("BOOKS");
        });

        footer.add(cancel);
        footer.add(confirm);
        main.add(footer, BorderLayout.SOUTH);

        dlg.add(main);
        dlg.setVisible(true);
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
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(CREAM);

        // ── HEADER ──────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(30, 35, 0, 35));

        // Title + subtitle
        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Students");
        title.setFont(DISPLAY);
        title.setForeground(INK);
        JLabel sub = new JLabel(students.size() + " registered students");
        sub.setFont(BODY);
        sub.setForeground(MUTED);
        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(3));
        titleBox.add(sub);
        header.add(titleBox, BorderLayout.WEST);

        // Top-right: search + add button
        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        topRight.setOpaque(false);

        // Styled search box
        JPanel searchBox = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
                Component center = ((BorderLayout)getLayout()).getLayoutComponent(BorderLayout.CENTER);
                boolean focused = center != null && center.hasFocus();
                g2.setColor(focused ? TERRACOTTA : SAND);
                g2.setStroke(new BasicStroke(focused ? 2f : 1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        searchBox.setOpaque(false);
        searchBox.setPreferredSize(new Dimension(260, 44));
        searchBox.setBorder(new EmptyBorder(0, 12, 0, 12));

        JLabel sIcon = new JLabel("⌕");
        sIcon.setFont(customSerif.deriveFont(Font.BOLD, 22f));
        sIcon.setForeground(MUTED);
        sIcon.setBorder(new EmptyBorder(0, 2, 1, 7));

        JTextField search = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(MUTED);
                    int y = (getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();
                    g2.drawString("Search by name, ID or phone...", getInsets().left, y);
                    g2.dispose();
                }
            }
        };
        search.setFont(new Font("SansSerif", Font.PLAIN, 14));
        search.setForeground(INK);
        search.setOpaque(false);
        search.setBackground(new Color(255,255,255,0));
        search.setBorder(BorderFactory.createEmptyBorder());
        search.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { searchBox.repaint(); }
            public void focusLost(FocusEvent e)   { searchBox.repaint(); }
        });
        searchBox.add(sIcon, BorderLayout.WEST);
        searchBox.add(search, BorderLayout.CENTER);
        topRight.add(searchBox);

        // + Add Student button
        JButton addBtn = new JButton("+ Add Student");
        addBtn.setFont(SMALL_BOLD);
        addBtn.setForeground(WHITE);
        addBtn.setBackground(TERRACOTTA);
        addBtn.setOpaque(true);
        addBtn.setBorderPainted(false);
        addBtn.setFocusPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.setBorder(new EmptyBorder(10, 18, 10, 18));
        addBtn.addActionListener(e -> showAddStudentDialog(page));
        topRight.add(addBtn);
        header.add(topRight, BorderLayout.EAST);

        // ── FILTER CHIPS + COUNT ────────────────────────────────
        JPanel filtersRow = new JPanel(new BorderLayout());
        filtersRow.setOpaque(false);
        filtersRow.setBorder(new EmptyBorder(18, 0, 0, 0));

        JPanel chipsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        chipsPanel.setOpaque(false);

        String[] chipNames = {"All", "With issued books", "With fines", "Overdue"};
        String[] activeFilter = {"All"};
        java.util.List<JButton> chipButtons = new java.util.ArrayList<>();
        Runnable[] applyFilters = new Runnable[1];

        // Count for each chip
        int[] chipCounts = new int[chipNames.length];
        chipCounts[0] = students.size();
        for (Student s : students) {
            boolean hasIssued = false, hasFine = false, hasOverdue = false;
            for (LibRecord r : records) {
                if (!r.getStudent().getId().equals(s.getId())) continue;
                if (!r.isReturned()) hasIssued = true;
                if (!r.isReturned()) {
                    long days = java.time.temporal.ChronoUnit.DAYS.between(r.getIssueDate(), java.time.LocalDate.now());
                    if (days > r.getAllowedDays()) hasOverdue = true;
                }
                if (r.isReturned() && r.getFine() > 0 && !r.getFineStatus().equals("PAID")) hasFine = true;
            }
            if (hasIssued) chipCounts[1]++;
            if (hasFine)   chipCounts[2]++;
            if (hasOverdue) chipCounts[3]++;
        }

        for (int i = 0; i < chipNames.length; i++) {
            String cn = chipNames[i];
            int cnt = chipCounts[i];
            String chipText = cn + " " + cnt;
            JButton chip = new JButton(chipText);
            chip.setFont(SMALL_BOLD);
            chip.setForeground(i == 0 ? WHITE : MUTED);
            chip.setBackground(i == 0 ? TERRACOTTA : CREAM);
            chip.setOpaque(true);
            chip.setBorderPainted(true);
            chip.setBorder(BorderFactory.createCompoundBorder(new LineBorder(SAND, 1, true), new EmptyBorder(6, 14, 6, 14)));
            chip.setFocusPainted(false);
            chip.setCursor(new Cursor(Cursor.HAND_CURSOR));
            chip.addActionListener(ev -> {
                activeFilter[0] = cn;
                for (JButton b : chipButtons) {
                    boolean active = b.getText().startsWith(cn + " ");
                    b.setForeground(active ? WHITE : MUTED);
                    b.setBackground(active ? TERRACOTTA : CREAM);
                }
                if (applyFilters[0] != null) applyFilters[0].run();
            });
            chipButtons.add(chip);
            chipsPanel.add(chip);
        }
        filtersRow.add(chipsPanel, BorderLayout.WEST);

        JPanel headerWrapper = new JPanel(new BorderLayout());
        headerWrapper.setOpaque(false);
        headerWrapper.setBorder(new EmptyBorder(30, 35, 0, 35));
        headerWrapper.add(header, BorderLayout.NORTH);
        headerWrapper.add(filtersRow, BorderLayout.SOUTH);

        // ── RESULTS COUNT ───────────────────────────────────────
        JPanel countRow = new JPanel(new BorderLayout());
        countRow.setOpaque(false);
        countRow.setBorder(new EmptyBorder(10, 35, 6, 35));
        JLabel resultsCount = new JLabel("Showing " + students.size() + " of " + students.size() + " students");
        resultsCount.setFont(SMALL);
        resultsCount.setForeground(MUTED);
        countRow.add(resultsCount, BorderLayout.WEST);

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setOpaque(false);
        topSection.add(headerWrapper, BorderLayout.NORTH);
        topSection.add(countRow, BorderLayout.SOUTH);
        page.add(topSection, BorderLayout.NORTH);

        // ── TABLE HEADER ROW ────────────────────────────────────
        JPanel tableHeader = new JPanel(null);
        tableHeader.setBackground(new Color(235, 228, 213));
        tableHeader.setPreferredSize(new Dimension(0, 32));
        tableHeader.setBorder(new EmptyBorder(0, 35, 0, 35));
        // columns: avatar(58), name+id(180), course(200), phone(130), issued(80), fine(90), status(110), chevron(30)
        String[] colTitles  = {"", "STUDENT", "COURSE", "PHONE", "ISSUED", "FINE DUE", "STATUS", ""};
        int[]    colXs      = {35, 103, 283, 483, 613, 693, 783, 893};
        for (int i = 1; i < colTitles.length - 1; i++) {
            JLabel lbl = new JLabel(colTitles[i]);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
            lbl.setForeground(MUTED);
            lbl.setBounds(colXs[i], 8, 150, 16);
            tableHeader.add(lbl);
        }

        // ── LIST ────────────────────────────────────────────────
        JPanel list = new JPanel();
        list.setBackground(CREAM);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(new EmptyBorder(0, 35, 30, 35));

        // Avatar tint colours (cycles)
        Color[] avatarTints = {SAGE, new Color(180, 130, 100), SAND, new Color(120, 150, 175), GOLD};

        // ── APPLY FILTERS LOGIC ─────────────────────────────────
        applyFilters[0] = () -> {
            list.removeAll();
            String query = search.getText().toLowerCase().trim();
            String filter = activeFilter[0];
            java.util.List<Student> visible = new java.util.ArrayList<>();

            for (Student s : students) {
                // compute derived stats
                int issued = 0;
                double fine = 0;
                boolean overdue = false;
                for (LibRecord r : records) {
                    if (!r.getStudent().getId().equals(s.getId())) continue;
                    if (!r.isReturned()) {
                        issued++;
                        long days = java.time.temporal.ChronoUnit.DAYS.between(r.getIssueDate(), java.time.LocalDate.now());
                        if (days > r.getAllowedDays()) overdue = true;
                    }
                    if (r.isReturned() && r.getFine() > 0 && !r.getFineStatus().equals("PAID")) fine += r.getFine();
                }

                boolean matchFilter = switch (filter) {
                    case "With issued books" -> issued > 0;
                    case "With fines"        -> fine > 0;
                    case "Overdue"           -> overdue;
                    default                  -> true;
                };
                boolean matchQuery = query.isEmpty()
                    || s.getName().toLowerCase().contains(query)
                    || s.getId().toLowerCase().contains(query)
                    || s.getContact().toLowerCase().contains(query);

                if (matchFilter && matchQuery) visible.add(s);
            }

            int[] idx = {0};
            for (Student s : visible) {
                int issued = 0;
                double fine = 0;
                boolean overdue = false;
                for (LibRecord r : records) {
                    if (!r.getStudent().getId().equals(s.getId())) continue;
                    if (!r.isReturned()) {
                        issued++;
                        long days = java.time.temporal.ChronoUnit.DAYS.between(r.getIssueDate(), java.time.LocalDate.now());
                        if (days > r.getAllowedDays()) overdue = true;
                    }
                    if (r.isReturned() && r.getFine() > 0 && !r.getFineStatus().equals("PAID")) fine += r.getFine();
                }
                Color tint = avatarTints[idx[0] % avatarTints.length];
                list.add(studentRow(s, issued, fine, overdue, tint));
                idx[0]++;
            }

            if (visible.isEmpty()) {
                JLabel empty = new JLabel("No students match your search");
                empty.setFont(BODY);
                empty.setForeground(MUTED);
                empty.setBorder(new EmptyBorder(30, 10, 10, 10));
                list.add(empty);
            }

            resultsCount.setText("Showing " + visible.size() + " of " + students.size() + " students");
            list.revalidate();
            list.repaint();
        };

        applyFilters[0].run();

        search.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { applyFilters[0].run(); }
        });

        // ── SCROLL ──────────────────────────────────────────────
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(CREAM);
        tableWrapper.add(tableHeader, BorderLayout.NORTH);
        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setBackground(CREAM);
        listWrapper.add(list, BorderLayout.NORTH);
        tableWrapper.add(listWrapper, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(tableWrapper);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        page.add(scroll, BorderLayout.CENTER);

        return page;
    }

    JPanel studentRow(Student student, int issued, double fine, boolean overdue, Color avatarTint) {
        // Determine status
        String statusText; Color statusColor, statusBg;
        if (overdue) {
            statusText = "Overdue"; statusColor = new Color(146, 90, 10); statusBg = new Color(255, 235, 180);
        } else if (fine > 0) {
            statusText = "Fine pending"; statusColor = new Color(140, 50, 30); statusBg = new Color(255, 218, 210);
        } else {
            statusText = "Active"; statusColor = new Color(60, 90, 50); statusBg = new Color(210, 230, 200);
        }

        JPanel row = new JPanel(new BorderLayout()) {
            boolean hovered = false;
            { // instance init
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e)  { hovered = true;  repaint(); setCursor(new Cursor(Cursor.HAND_CURSOR)); }
                    public void mouseExited(java.awt.event.MouseEvent e)   { hovered = false; repaint(); setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); }
                    public void mouseClicked(java.awt.event.MouseEvent e)  { showStudentProfile(student); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hovered) {
                    g2.setColor(new Color(210, 195, 175, 80));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        row.setPreferredSize(new Dimension(800, 72));
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 210, 195)),
            new EmptyBorder(0, 0, 0, 0)
        ));

        java.awt.event.MouseAdapter hoverAndClick = new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { row.dispatchEvent(javax.swing.SwingUtilities.convertMouseEvent(e.getComponent(), e, row)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { row.dispatchEvent(javax.swing.SwingUtilities.convertMouseEvent(e.getComponent(), e, row)); }
            public void mouseClicked(java.awt.event.MouseEvent e) { showStudentProfile(student); }
        };

        // ── AVATAR ──────────────────────────────────────────────
        JPanel avatarPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = (getWidth()-44)/2, cy = (getHeight()-44)/2;
                g2.setColor(avatarTint);
                g2.fillOval(cx, cy, 44, 44);
                g2.setFont(customSerif.deriveFont(Font.BOLD, 18f));
                g2.setColor(WHITE);
                String init = student.getName().substring(0,1).toUpperCase();
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(init, cx + (44 - fm.stringWidth(init))/2, cy + (44 - fm.getHeight())/2 + fm.getAscent());
                g2.dispose();
            }
        };
        avatarPanel.setOpaque(false);
        avatarPanel.setPreferredSize(new Dimension(68, 72));
        avatarPanel.addMouseListener(hoverAndClick);
        row.add(avatarPanel, BorderLayout.WEST);

        // ── COLUMNS (CENTER) ─────────────────────────────────────
        JPanel cols = new JPanel(new GridLayout(1, 6, 0, 0));
        cols.setOpaque(false);

        // Name + ID pill
        JPanel nameCell = new JPanel();
        nameCell.setOpaque(false);
        nameCell.setLayout(new BoxLayout(nameCell, BoxLayout.Y_AXIS));
        nameCell.setBorder(new EmptyBorder(16, 0, 16, 10));
        JLabel nameLabel = new JLabel(student.getName());
        nameLabel.setFont(TITLE);
        nameLabel.setForeground(INK);
        JLabel idLabel = new JLabel(student.getId());
        idLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        idLabel.setForeground(MUTED);
        nameCell.add(nameLabel);
        nameCell.add(Box.createVerticalStrut(3));
        nameCell.add(idLabel);
        nameCell.addMouseListener(hoverAndClick);
        cols.add(nameCell);

        // Course (from getCourse())
        JPanel courseCell = new JPanel();
        courseCell.setOpaque(false);
        courseCell.setLayout(new BoxLayout(courseCell, BoxLayout.Y_AXIS));
        courseCell.setBorder(new EmptyBorder(22, 0, 22, 10));
        JLabel courseLabel = new JLabel("<html><body>" + student.getCourse().replace("·","<br><span style='color:gray;font-size:10px'>") + "</body></html>");
        courseLabel.setFont(BODY);
        courseLabel.setForeground(MUTED);
        courseCell.add(courseLabel);
        courseCell.addMouseListener(hoverAndClick);
        cols.add(courseCell);

        // Phone
        JPanel phoneCell = centeredColCell(student.getContact(), MUTED, hoverAndClick);
        cols.add(phoneCell);

        // Books issued
        JPanel issuedCell = centeredColCell(issued > 0 ? issued + " book" + (issued > 1 ? "s" : "") : "—", issued > 0 ? INK : MUTED, hoverAndClick);
        cols.add(issuedCell);

        // Fine
        String fineStr = fine > 0 ? "₹" + (int) fine : "No dues";
        Color fineColor = fine > 0 ? TERRACOTTA : MUTED;
        JPanel fineCell = centeredColCell(fineStr, fineColor, hoverAndClick);
        cols.add(fineCell);

        // Status pill
        JPanel statusCell = new JPanel(new GridBagLayout());
        statusCell.setOpaque(false);
        statusCell.addMouseListener(hoverAndClick);
        JLabel pill = new JLabel(statusText) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(statusBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        pill.setFont(new Font("SansSerif", Font.BOLD, 11));
        pill.setForeground(statusColor);
        pill.setOpaque(false);
        pill.setBorder(new EmptyBorder(4, 10, 4, 10));
        statusCell.add(pill);
        cols.add(statusCell);

        row.add(cols, BorderLayout.CENTER);

        // Chevron
        JLabel chevron = new JLabel("›");
        chevron.setFont(customSerif.deriveFont(Font.PLAIN, 22f));
        chevron.setForeground(MUTED);
        chevron.setBorder(new EmptyBorder(0, 0, 0, 8));
        chevron.addMouseListener(hoverAndClick);
        row.add(chevron, BorderLayout.EAST);

        return row;
    }

    private JPanel centeredColCell(String text, Color color, java.awt.event.MouseAdapter hover) {
        JPanel cell = new JPanel(new GridBagLayout());
        cell.setOpaque(false);
        JLabel lbl = new JLabel(text);
        lbl.setFont(BODY);
        lbl.setForeground(color);
        cell.add(lbl);
        cell.addMouseListener(hover);
        return cell;
    }

    void showAddStudentDialog(JPanel parent) {
        JDialog dlg = new JDialog((java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(parent), "Add Student", true);
        dlg.setSize(420, 420);
        dlg.setLocationRelativeTo(parent);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(CREAM);

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 14));
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(24, 24, 10, 24));

        String[] labels = {"Name", "Student ID", "Course", "Year", "Phone"};
        JTextField[] fields = new JTextField[labels.length];
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(SMALL_BOLD);
            lbl.setForeground(INK);
            form.add(lbl);
            fields[i] = new JTextField();
            fields[i].setFont(BODY);
            fields[i].setBorder(BorderFactory.createCompoundBorder(new LineBorder(SAND, 1, true), new EmptyBorder(6, 8, 6, 8)));
            form.add(fields[i]);
        }
        main.add(form, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 14));
        footer.setOpaque(false);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dlg.dispose());
        JButton save = new JButton("Save");
        save.setBackground(TERRACOTTA);
        save.setForeground(WHITE);
        save.setOpaque(true);
        save.setBorderPainted(false);
        save.addActionListener(e -> {
            String name = fields[0].getText().trim();
            String id   = fields[1].getText().trim();
            String course = fields[2].getText().trim();
            String year   = fields[3].getText().trim();
            String phone  = fields[4].getText().trim();
            if (name.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Name and ID are required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String fullCourse = course + (year.isEmpty() ? "" : " · " + year);
            Student s = new Student(id, name, fullCourse, phone);
            students.add(s);
            saveData();
            dlg.dispose();
            rebuildPages();
            showPage("STUDENTS");
        });
        footer.add(cancel);
        footer.add(save);
        main.add(footer, BorderLayout.SOUTH);

        dlg.add(main);
        dlg.setVisible(true);
    }

    void showAddBookDialog(JPanel parent) {
        JDialog dlg = new JDialog((java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(parent), "Add New Book", true);
        dlg.setSize(480, 420);
        dlg.setLocationRelativeTo(parent);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(CREAM);

        // Header strip
        JPanel dlgHeader = new JPanel(new BorderLayout());
        dlgHeader.setBackground(INK);
        dlgHeader.setBorder(new EmptyBorder(18, 24, 18, 24));
        JLabel dlgTitle = new JLabel("Add New Book");
        dlgTitle.setFont(TITLE);
        dlgTitle.setForeground(WHITE);
        JLabel dlgSub = new JLabel("Fill in the details to add a book to the catalog");
        dlgSub.setFont(SMALL);
        dlgSub.setForeground(new Color(180, 170, 160));
        JPanel dlgHdrText = new JPanel();
        dlgHdrText.setOpaque(false);
        dlgHdrText.setLayout(new BoxLayout(dlgHdrText, BoxLayout.Y_AXIS));
        dlgHdrText.add(dlgTitle);
        dlgHdrText.add(Box.createVerticalStrut(3));
        dlgHdrText.add(dlgSub);
        dlgHeader.add(dlgHdrText, BorderLayout.CENTER);
        main.add(dlgHeader, BorderLayout.NORTH);

        // Form grid
        JPanel form = new JPanel(new GridLayout(0, 2, 12, 14));
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(22, 24, 10, 24));

        // Title
        JLabel lblTitle = new JLabel("Book Title *");
        lblTitle.setFont(SMALL_BOLD); lblTitle.setForeground(INK);
        JTextField fTitle = new JTextField();
        fTitle.setFont(BODY);
        fTitle.setBorder(BorderFactory.createCompoundBorder(new LineBorder(SAND, 1, true), new EmptyBorder(6, 8, 6, 8)));

        // Author
        JLabel lblAuthor = new JLabel("Author *");
        lblAuthor.setFont(SMALL_BOLD); lblAuthor.setForeground(INK);
        JTextField fAuthor = new JTextField();
        fAuthor.setFont(BODY);
        fAuthor.setBorder(BorderFactory.createCompoundBorder(new LineBorder(SAND, 1, true), new EmptyBorder(6, 8, 6, 8)));

        // Book ID
        JLabel lblId = new JLabel("Book ID *");
        lblId.setFont(SMALL_BOLD); lblId.setForeground(INK);
        JTextField fId = new JTextField();
        fId.setFont(BODY);
        fId.setBorder(BorderFactory.createCompoundBorder(new LineBorder(SAND, 1, true), new EmptyBorder(6, 8, 6, 8)));
        fId.setToolTipText("Unique ID, e.g. A201, G310, F502");

        // Category
        JLabel lblCat = new JLabel("Category *");
        lblCat.setFont(SMALL_BOLD); lblCat.setForeground(INK);
        JComboBox<String> fCat = new JComboBox<>(new String[]{"Academic", "Reference", "General", "Fiction"});
        fCat.setFont(BODY);
        fCat.setBackground(WHITE);

        // Copies
        JLabel lblCopies = new JLabel("No. of Copies *");
        lblCopies.setFont(SMALL_BOLD); lblCopies.setForeground(INK);
        JTextField fCopies = new JTextField("1");
        fCopies.setFont(BODY);
        fCopies.setBorder(BorderFactory.createCompoundBorder(new LineBorder(SAND, 1, true), new EmptyBorder(6, 8, 6, 8)));

        form.add(lblTitle);    form.add(fTitle);
        form.add(lblAuthor);   form.add(fAuthor);
        form.add(lblId);       form.add(fId);
        form.add(lblCat);      form.add(fCat);
        form.add(lblCopies);   form.add(fCopies);

        main.add(form, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 14));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, SAND));

        JButton cancel = new JButton("Cancel");
        cancel.setFont(SMALL_BOLD);
        cancel.setForeground(MUTED);
        cancel.setBackground(CREAM);
        cancel.setOpaque(true);
        cancel.setBorderPainted(true);
        cancel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(SAND, 1, true), new EmptyBorder(6, 16, 6, 16)));
        cancel.setFocusPainted(false);
        cancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancel.addActionListener(e -> dlg.dispose());

        JButton save = new JButton("Add Book") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(205, 95, 75) : TERRACOTTA);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        save.setFont(SMALL_BOLD);
        save.setForeground(WHITE);
        save.setOpaque(false);
        save.setContentAreaFilled(false);
        save.setBorderPainted(false);
        save.setFocusPainted(false);
        save.setBorder(new EmptyBorder(7, 20, 7, 20));
        save.setCursor(new Cursor(Cursor.HAND_CURSOR));

        save.addActionListener(e -> {
            String titleVal  = fTitle.getText().trim();
            String authorVal = fAuthor.getText().trim();
            String idVal     = fId.getText().trim();
            String catVal    = (String) fCat.getSelectedItem();
            String copiesStr = fCopies.getText().trim();

            // Validation
            if (titleVal.isEmpty() || authorVal.isEmpty() || idVal.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Title, Author, and Book ID are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int copiesVal;
            try {
                copiesVal = Integer.parseInt(copiesStr);
                if (copiesVal < 1 || copiesVal > 999) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg, "Copies must be a number between 1 and 999.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Check for duplicate ID
            for (Book existing : books) {
                if (existing.getId().equalsIgnoreCase(idVal)) {
                    JOptionPane.showMessageDialog(dlg, "A book with ID \"" + idVal + "\" already exists.", "Duplicate ID", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            Book newBook = new Book(idVal, titleVal, authorVal, catVal, copiesVal);
            books.add(newBook);
            saveData();
            dlg.dispose();
            showToast("\"" + titleVal + "\" added to the catalog.", SAGE);
            rebuildPages();
            showPage("BOOKS");
        });

        footer.add(cancel);
        footer.add(save);
        main.add(footer, BorderLayout.SOUTH);

        dlg.add(main);
        dlg.setVisible(true);
    }

    void showStudentProfile(Student student) {
        JDialog dlg = new JDialog((java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), student.getName(), false);
        dlg.setSize(580, 580);
        dlg.setLocationRelativeTo(this);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(CREAM);

        // ── Avatar + header ──────────────────────────────────────
        Color[] tints = {SAGE, new Color(180,130,100), SAND, new Color(120,150,175), GOLD};
        int tintIdx = Math.abs(student.getId().hashCode()) % tints.length;
        Color tint = tints[tintIdx];

        JPanel profileHeader = new JPanel(new BorderLayout(16, 0));
        profileHeader.setBackground(new Color(235, 228, 213));
        profileHeader.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel av = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(tint);
                g2.fillOval(0, 0, 64, 64);
                g2.setFont(customSerif.deriveFont(Font.BOLD, 26f));
                g2.setColor(WHITE);
                String init = student.getName().substring(0,1).toUpperCase();
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(init, (64-fm.stringWidth(init))/2, (64-fm.getHeight())/2+fm.getAscent());
                g2.dispose();
            }
        };
        av.setOpaque(false);
        av.setPreferredSize(new Dimension(64, 64));
        profileHeader.add(av, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JLabel nm = new JLabel(student.getName()); nm.setFont(DISPLAY); nm.setForeground(INK);
        JLabel idl = new JLabel(student.getId() + "  ·  " + student.getCourse()); idl.setFont(BODY); idl.setForeground(MUTED);
        JLabel ph = new JLabel("📞 " + student.getContact()); ph.setFont(SMALL); ph.setForeground(MUTED);
        infoPanel.add(nm);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(idl);
        infoPanel.add(Box.createVerticalStrut(2));
        infoPanel.add(ph);
        profileHeader.add(infoPanel, BorderLayout.CENTER);
        main.add(profileHeader, BorderLayout.NORTH);

        // ── Content: loans + fine history ────────────────────────
        JPanel content = new JPanel();
        content.setBackground(CREAM);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Active loans
        JLabel loansTitle = new JLabel("Current Loans");
        loansTitle.setFont(TITLE);
        loansTitle.setForeground(INK);
        content.add(loansTitle);
        content.add(Box.createVerticalStrut(10));

        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy");
        boolean anyLoan = false;
        for (LibRecord r : records) {
            if (!r.getStudent().getId().equals(student.getId()) || r.isReturned()) continue;
            anyLoan = true;
            java.time.LocalDate due = r.getIssueDate().plusDays(r.getAllowedDays());
            long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(today, due);
            boolean od = daysLeft < 0;
            JPanel loanRow = new JPanel(new BorderLayout(8, 0));
            loanRow.setBackground(od ? new Color(255, 235, 228) : new Color(245, 242, 236));
            loanRow.setBorder(BorderFactory.createCompoundBorder(new LineBorder(od ? new Color(220,180,160) : SAND, 1, true), new EmptyBorder(8, 12, 8, 12)));
            loanRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            JLabel bookLbl = new JLabel(r.getBook().getTitle());
            bookLbl.setFont(SMALL_BOLD);
            bookLbl.setForeground(INK);
            loanRow.add(bookLbl, BorderLayout.WEST);
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            right.setOpaque(false);
            JLabel dueLbl = new JLabel("Due: " + due.format(fmt));
            dueLbl.setFont(SMALL);
            dueLbl.setForeground(MUTED);
            right.add(dueLbl);
            if (od) {
                JLabel badge = new JLabel("Overdue " + Math.abs(daysLeft) + "d");
                badge.setFont(new Font("SansSerif", Font.BOLD, 10));
                badge.setForeground(new Color(140, 50, 30));
                badge.setBackground(new Color(255, 200, 185));
                badge.setOpaque(true);
                badge.setBorder(new EmptyBorder(2, 7, 2, 7));
                right.add(badge);
            }
            loanRow.add(right, BorderLayout.EAST);
            content.add(loanRow);
            content.add(Box.createVerticalStrut(6));
        }
        if (!anyLoan) {
            JLabel none = new JLabel("No active loans."); none.setFont(BODY); none.setForeground(MUTED);
            content.add(none);
        }

        content.add(Box.createVerticalStrut(18));

        // Fine history
        JLabel fineTitle = new JLabel("Fine History");
        fineTitle.setFont(TITLE);
        fineTitle.setForeground(INK);
        content.add(fineTitle);
        content.add(Box.createVerticalStrut(10));

        boolean anyFine = false;
        for (LibRecord r : records) {
            if (!r.getStudent().getId().equals(student.getId()) || !r.isReturned() || r.getFine() <= 0) continue;
            anyFine = true;
            JPanel fineRow = new JPanel(new BorderLayout(8, 0));
            fineRow.setBackground(new Color(245, 242, 236));
            fineRow.setBorder(BorderFactory.createCompoundBorder(new LineBorder(SAND, 1, true), new EmptyBorder(8, 12, 8, 12)));
            fineRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            JLabel bookLbl = new JLabel(r.getBook().getTitle()); bookLbl.setFont(SMALL_BOLD); bookLbl.setForeground(INK);
            fineRow.add(bookLbl, BorderLayout.WEST);
            JLabel fineAmt = new JLabel("₹" + (int)r.getFine() + "  [" + r.getFineStatus() + "]");
            fineAmt.setFont(SMALL_BOLD);
            fineAmt.setForeground(r.getFineStatus().equals("PAID") ? SAGE : TERRACOTTA);
            fineRow.add(fineAmt, BorderLayout.EAST);
            content.add(fineRow);
            content.add(Box.createVerticalStrut(6));
        }
        if (!anyFine) {
            JLabel none = new JLabel("No fines on record."); none.setFont(BODY); none.setForeground(MUTED);
            content.add(none);
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        main.add(scroll, BorderLayout.CENTER);

        // Back button
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        footer.setOpaque(false);
        JButton back = new JButton("Close");
        back.addActionListener(e -> dlg.dispose());
        footer.add(back);
        main.add(footer, BorderLayout.SOUTH);

        dlg.add(main);
        dlg.setVisible(true);
    }

    // =========================================================
    // (legacy studentCard — no longer used but kept to avoid breaking references)
    @SuppressWarnings("unused")
    JPanel studentCard(Student student) {
        return studentRow(student, 0, 0, false, SAGE);
    }

    // =========================================================
    // ISSUE PAGE
    // =========================================================
    // ISSUE BOOK PAGE (REFINED)
    // =========================================================

    private static class IssueBookComboItem {
        final Book book;
        final String displayText;
        final boolean isPlaceholder;

        IssueBookComboItem(Book book, String displayText, boolean isPlaceholder) {
            this.book = book;
            this.displayText = displayText;
            this.isPlaceholder = isPlaceholder;
        }

        @Override
        public String toString() {
            return displayText;
        }
    }

    private static class IssueStudentComboItem {
        final Student student;
        final String displayText;
        final boolean isPlaceholder;

        IssueStudentComboItem(Student student, String displayText, boolean isPlaceholder) {
            this.student = student;
            this.displayText = displayText;
            this.isPlaceholder = isPlaceholder;
        }

        @Override
        public String toString() {
            return displayText;
        }
    }

    JPanel createIssuePage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(CREAM);

        // HEADER: Left padding 40px
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(30, 40, 16, 40));

        JLabel title = new JLabel("Issue a Book");
        title.setFont(DISPLAY);
        title.setForeground(INK);

        JLabel sub = new JLabel("Create a new library issue record.");
        sub.setFont(BODY);
        sub.setForeground(MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(sub);
        page.add(header, BorderLayout.NORTH);

        // MAIN CONTENT CONTAINER
        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setOpaque(false);
        contentContainer.setBorder(new EmptyBorder(0, 40, 30, 40));

        // TOP ROW: Form Card (Left) and Live Preview Card (Right)
        JPanel topRow = new JPanel(new GridBagLayout());
        topRow.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        // Form Card (Left)
        GlassPanel formCard = new GlassPanel(new Color(255, 252, 246, 235), new Color(255, 255, 255, 180));
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel formTitle = new JLabel("Issue details");
        formTitle.setFont(TITLE);
        formTitle.setForeground(INK);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(formTitle);
        formCard.add(Box.createVerticalStrut(18));

        // Student combo setup
        JLabel studentLabel = new JLabel("Select Student");
        studentLabel.setFont(BODY_BOLD);
        studentLabel.setForeground(INK);
        studentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(studentLabel);
        formCard.add(Box.createVerticalStrut(6));

        ArrayList<IssueStudentComboItem> studentList = new ArrayList<>();
        studentList.add(new IssueStudentComboItem(null, "Select Student...", true));
        for (Student s : students) {
            String label = s.getId() + " · " + s.getName() + " · " + s.getCourse();
            studentList.add(new IssueStudentComboItem(s, label, false));
        }

        DefaultComboBoxModel<IssueStudentComboItem> studentModel = new DefaultComboBoxModel<>();
        for (IssueStudentComboItem item : studentList) studentModel.addElement(item);

        JComboBox<IssueStudentComboItem> studentCombo = new JComboBox<>(studentModel);
        styleIssueCombo(studentCombo);
        studentCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        studentCombo.setPreferredSize(new Dimension(380, 44));
        studentCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        setupSearchableStudentCombo(studentCombo, studentList);
        formCard.add(studentCombo);
        formCard.add(Box.createVerticalStrut(18));

        // Book combo setup
        JLabel bookLabel = new JLabel("Select Book");
        bookLabel.setFont(BODY_BOLD);
        bookLabel.setForeground(INK);
        bookLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(bookLabel);
        formCard.add(Box.createVerticalStrut(6));

        ArrayList<IssueBookComboItem> bookList = new ArrayList<>();
        bookList.add(new IssueBookComboItem(null, "Select Book...", true));
        IssueBookComboItem preselectedItem = null;
        for (Book b : books) {
            String label = b.getTitle() + " — " + b.getAuthor() + " (" + b.getAvailableCopies() + " of " + b.getTotalCopies() + " available)";
            IssueBookComboItem item = new IssueBookComboItem(b, label, false);
            bookList.add(item);
            if (preselectedBook != null && !preselectedBook.isEmpty() && b.getTitle().equalsIgnoreCase(preselectedBook)) {
                preselectedItem = item;
            }
        }
        preselectedBook = ""; // consume preselection

        DefaultComboBoxModel<IssueBookComboItem> bookModel = new DefaultComboBoxModel<>();
        for (IssueBookComboItem item : bookList) bookModel.addElement(item);

        JComboBox<IssueBookComboItem> bookCombo = new JComboBox<>(bookModel);
        styleIssueCombo(bookCombo);
        bookCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        bookCombo.setPreferredSize(new Dimension(380, 44));
        bookCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        setupSearchableBookCombo(bookCombo, bookList);
        formCard.add(bookCombo);
        formCard.add(Box.createVerticalStrut(18));

        // Duration Chips
        JLabel daysLabel = new JLabel("Issue Duration (Days)");
        daysLabel.setFont(BODY_BOLD);
        daysLabel.setForeground(INK);
        daysLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(daysLabel);
        formCard.add(Box.createVerticalStrut(8));

        JPanel daysPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        daysPanel.setOpaque(false);
        daysPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final int[] selectedDays = {14};
        final boolean[] isCustomSelected = {false};
        int[] chipOptions = {7, 14, 21, 30};
        ArrayList<JButton> chipBtns = new ArrayList<>();

        JSpinner customDaysSpinner = new JSpinner(new SpinnerNumberModel(14, 1, 365, 1));
        customDaysSpinner.setPreferredSize(new Dimension(65, 36));
        customDaysSpinner.setFont(BODY_BOLD);
        customDaysSpinner.setVisible(false);
        JComponent spinnerEditor = customDaysSpinner.getEditor();
        if (spinnerEditor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) spinnerEditor).getTextField();
            tf.setBackground(WHITE);
            tf.setForeground(INK);
            tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SAND, 1),
                BorderFactory.createEmptyBorder(2, 4, 2, 4)
            ));
        }

        // Live preview panel placeholder (updated via Runnable)
        GlassPanel infoCard = new GlassPanel(INK, new Color(255, 255, 255, 45));
        infoCard.setLayout(new BorderLayout());
        infoCard.setBorder(new EmptyBorder(25, 28, 25, 28));

        // Issue button with custom paintComponent (text color, background, hover)
        JButton issueBtn = new JButton("Issue Book") {
            private boolean hovered = false;
            {
                setOpaque(false);
                setContentAreaFilled(false);
                setFocusPainted(false);
                setBorderPainted(false);
                setFont(BODY_BOLD);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (isEnabled()) {
                            hovered = true;
                            repaint();
                        }
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                if (!isEnabled()) {
                    g2.setColor(new Color(220, 212, 202)); // muted sand fill
                    g2.fillRoundRect(0, 0, w, h, 8, 8);
                    g2.setColor(new Color(145, 138, 130)); // grey text
                } else if (hovered) {
                    g2.setColor(new Color(156, 78, 58)); // darkened terracotta
                    g2.fillRoundRect(0, 0, w, h, 8, 8);
                    g2.setColor(WHITE); // white bold text
                } else {
                    g2.setColor(TERRACOTTA); // solid terracotta
                    g2.fillRoundRect(0, 0, w, h, 8, 8);
                    g2.setColor(WHITE); // white bold text
                }

                g2.setFont(BODY_BOLD);
                FontMetrics fm = g2.getFontMetrics();
                String text = getText();
                int tx = (w - fm.stringWidth(text)) / 2;
                int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(text, tx, ty);
                g2.dispose();
            }
        };
        issueBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        issueBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        issueBtn.setPreferredSize(new Dimension(380, 46));
        issueBtn.setEnabled(false);

        // Recent issues container below form
        JPanel recentIssuesCard = new GlassPanel(new Color(255, 252, 246, 235), new Color(255, 255, 255, 180));
        recentIssuesCard.setLayout(new BoxLayout(recentIssuesCard, BoxLayout.Y_AXIS));
        recentIssuesCard.setBorder(new EmptyBorder(22, 28, 22, 28));

        Runnable[] updatePreviewRef = new Runnable[1];

        // Chips builder
        for (int d : chipOptions) {
            final int daysVal = d;
            JButton chip = new JButton(d + " days") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int w = getWidth();
                    int h = getHeight();
                    boolean selected = !isCustomSelected[0] && selectedDays[0] == daysVal;
                    if (selected) {
                        g2.setColor(TERRACOTTA);
                        g2.fillRoundRect(0, 0, w, h, 8, 8);
                        g2.setColor(WHITE);
                    } else {
                        g2.setColor(CREAM);
                        g2.fillRoundRect(0, 0, w - 1, h - 1, 8, 8);
                        g2.setColor(SAND);
                        g2.setStroke(new BasicStroke(1f));
                        g2.drawRoundRect(0, 0, w - 1, h - 1, 8, 8);
                        g2.setColor(INK);
                    }
                    g2.setFont(SMALL_BOLD);
                    FontMetrics fm = g2.getFontMetrics();
                    String t = getText();
                    int tx = (w - fm.stringWidth(t)) / 2;
                    int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(t, tx, ty);
                    g2.dispose();
                }
            };
            chip.setFont(SMALL_BOLD);
            chip.setOpaque(false);
            chip.setContentAreaFilled(false);
            chip.setBorderPainted(false);
            chip.setFocusPainted(false);
            chip.setCursor(new Cursor(Cursor.HAND_CURSOR));
            chip.setPreferredSize(new Dimension(74, 36));

            chip.addActionListener(e -> {
                isCustomSelected[0] = false;
                selectedDays[0] = daysVal;
                customDaysSpinner.setVisible(false);
                daysPanel.revalidate();
                daysPanel.repaint();
                for (JButton b : chipBtns) b.repaint();
                if (updatePreviewRef[0] != null) updatePreviewRef[0].run();
            });

            chipBtns.add(chip);
            daysPanel.add(chip);
        }

        // Custom Chip
        JButton customChip = new JButton("Custom") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                if (isCustomSelected[0]) {
                    g2.setColor(TERRACOTTA);
                    g2.fillRoundRect(0, 0, w, h, 8, 8);
                    g2.setColor(WHITE);
                } else {
                    g2.setColor(CREAM);
                    g2.fillRoundRect(0, 0, w - 1, h - 1, 8, 8);
                    g2.setColor(SAND);
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0, 0, w - 1, h - 1, 8, 8);
                    g2.setColor(INK);
                }
                g2.setFont(SMALL_BOLD);
                FontMetrics fm = g2.getFontMetrics();
                String t = getText();
                int tx = (w - fm.stringWidth(t)) / 2;
                int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(t, tx, ty);
                g2.dispose();
            }
        };
        customChip.setFont(SMALL_BOLD);
        customChip.setOpaque(false);
        customChip.setContentAreaFilled(false);
        customChip.setBorderPainted(false);
        customChip.setFocusPainted(false);
        customChip.setCursor(new Cursor(Cursor.HAND_CURSOR));
        customChip.setPreferredSize(new Dimension(74, 36));

        customChip.addActionListener(e -> {
            isCustomSelected[0] = true;
            selectedDays[0] = (Integer) customDaysSpinner.getValue();
            customDaysSpinner.setVisible(true);
            daysPanel.revalidate();
            daysPanel.repaint();
            for (JButton b : chipBtns) b.repaint();
            if (updatePreviewRef[0] != null) updatePreviewRef[0].run();
        });
        chipBtns.add(customChip);
        daysPanel.add(customChip);

        customDaysSpinner.addChangeListener(e -> {
            if (isCustomSelected[0]) {
                selectedDays[0] = (Integer) customDaysSpinner.getValue();
                if (updatePreviewRef[0] != null) updatePreviewRef[0].run();
            }
        });
        daysPanel.add(customDaysSpinner);

        formCard.add(daysPanel);
        formCard.add(Box.createVerticalStrut(28));
        formCard.add(issueBtn);

        // Pack top row
        gbc.gridx = 0;
        gbc.weightx = 0.52;
        gbc.insets = new Insets(0, 0, 0, 16);
        topRow.add(formCard, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.48;
        gbc.insets = new Insets(0, 0, 0, 0);
        topRow.add(infoCard, gbc);

        contentContainer.add(topRow);
        contentContainer.add(Box.createVerticalStrut(20));

        // Refresh Recent Issues helper
        Runnable refreshRecentIssues = () -> {
            recentIssuesCard.removeAll();

            JPanel recentHead = new JPanel(new BorderLayout());
            recentHead.setOpaque(false);
            JLabel recentTitle = new JLabel("Recent Issues");
            recentTitle.setFont(TITLE);
            recentTitle.setForeground(INK);
            recentHead.add(recentTitle, BorderLayout.WEST);
            recentIssuesCard.add(recentHead);
            recentIssuesCard.add(Box.createVerticalStrut(14));

            ArrayList<LibRecord> unreturned = new ArrayList<>();
            for (int i = records.size() - 1; i >= 0; i--) {
                LibRecord r = records.get(i);
                if (!r.isReturned()) {
                    unreturned.add(r);
                    if (unreturned.size() >= 5) break;
                }
            }

            if (unreturned.isEmpty()) {
                JPanel emptyRecent = new JPanel();
                emptyRecent.setLayout(new BoxLayout(emptyRecent, BoxLayout.Y_AXIS));
                emptyRecent.setOpaque(false);
                emptyRecent.setBorder(new EmptyBorder(16, 0, 16, 0));

                JLabel lblEmpty = new JLabel("No recent book issues found");
                lblEmpty.setFont(BODY);
                lblEmpty.setForeground(MUTED);
                lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
                emptyRecent.add(lblEmpty);
                recentIssuesCard.add(emptyRecent);
            } else {
                java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("d MMM");
                for (int i = 0; i < unreturned.size(); i++) {
                    LibRecord r = unreturned.get(i);
                    JPanel row = new JPanel(new BorderLayout(14, 0));
                    row.setOpaque(false);
                    row.setBorder(new EmptyBorder(6, 4, 6, 4));

                    // Small cover thumbnail (preserves aspect ratio)
                    JPanel thumb = createScaledCoverThumb(r.getBook(), 34, 46);
                    row.add(thumb, BorderLayout.WEST);

                    // Book -> Student details
                    JPanel centerPane = new JPanel();
                    centerPane.setLayout(new BoxLayout(centerPane, BoxLayout.Y_AXIS));
                    centerPane.setOpaque(false);

                    JLabel titleStud = new JLabel(r.getBook().getTitle() + "  →  " + r.getStudent().getName());
                    titleStud.setFont(BODY_BOLD);
                    titleStud.setForeground(INK);

                    JLabel details = new JLabel("ID: " + r.getStudent().getId() + " · " + r.getStudent().getCourse());
                    details.setFont(SMALL);
                    details.setForeground(MUTED);

                    centerPane.add(titleStud);
                    centerPane.add(Box.createVerticalStrut(2));
                    centerPane.add(details);
                    row.add(centerPane, BorderLayout.CENTER);

                    // Issue date & Due date
                    JPanel datePane = new JPanel();
                    datePane.setLayout(new BoxLayout(datePane, BoxLayout.Y_AXIS));
                    datePane.setOpaque(false);

                    java.time.LocalDate due = r.getIssueDate().plusDays(r.getAllowedDays());
                    JLabel issueDateLbl = new JLabel("Issued: " + r.getIssueDate().format(dtf));
                    issueDateLbl.setFont(SMALL);
                    issueDateLbl.setForeground(MUTED);

                    JLabel dueDateLbl = new JLabel("Due: " + due.format(dtf));
                    dueDateLbl.setFont(SMALL_BOLD);
                    dueDateLbl.setForeground(TERRACOTTA);

                    datePane.add(issueDateLbl);
                    datePane.add(Box.createVerticalStrut(2));
                    datePane.add(dueDateLbl);
                    row.add(datePane, BorderLayout.EAST);

                    recentIssuesCard.add(row);
                    if (i < unreturned.size() - 1) {
                        JSeparator sep = new JSeparator();
                        sep.setForeground(new Color(230, 222, 212));
                        recentIssuesCard.add(sep);
                    }
                }
            }
            recentIssuesCard.revalidate();
            recentIssuesCard.repaint();
        };

        refreshRecentIssues.run();
        contentContainer.add(recentIssuesCard);

        JScrollPane scrollPane = new JScrollPane(contentContainer);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        page.add(scrollPane, BorderLayout.CENTER);

        // LIVE PREVIEW UPDATE RUNNABLE
        updatePreviewRef[0] = () -> {
            infoCard.removeAll();

            IssueStudentComboItem selSItem = (IssueStudentComboItem) studentCombo.getSelectedItem();
            IssueBookComboItem selBItem = (IssueBookComboItem) bookCombo.getSelectedItem();

            boolean validS = selSItem != null && !selSItem.isPlaceholder && selSItem.student != null;
            boolean validB = selBItem != null && !selBItem.isPlaceholder && selBItem.book != null;

            if (!validS || !validB) {
                issueBtn.setEnabled(false);

                JPanel emptyState = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                        int w = getWidth();
                        int iconY = 110;
                        int cx = w / 2;

                        // Draw line-drawn open book icon
                        g2.setColor(new Color(255, 255, 255, 80));
                        g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                        // Left page
                        g2.drawLine(cx, iconY + 36, cx - 36, iconY + 32);
                        g2.drawLine(cx - 36, iconY + 32, cx - 36, iconY - 6);
                        g2.drawLine(cx - 36, iconY - 6, cx, iconY - 2);
                        g2.drawLine(cx, iconY - 2, cx, iconY + 36);

                        // Right page
                        g2.drawLine(cx, iconY + 36, cx + 36, iconY + 32);
                        g2.drawLine(cx + 36, iconY + 32, cx + 36, iconY - 6);
                        g2.drawLine(cx + 36, iconY - 6, cx, iconY - 2);

                        // Spine curve bottom
                        g2.drawArc(cx - 8, iconY + 33, 16, 7, 0, 180);

                        // Horizontal page lines on left and right
                        g2.setColor(new Color(255, 255, 255, 45));
                        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        g2.drawLine(cx - 28, iconY + 6, cx - 8, iconY + 8);
                        g2.drawLine(cx - 28, iconY + 16, cx - 8, iconY + 18);
                        g2.drawLine(cx - 28, iconY + 24, cx - 8, iconY + 26);

                        g2.drawLine(cx + 8, iconY + 8, cx + 28, iconY + 6);
                        g2.drawLine(cx + 8, iconY + 18, cx + 28, iconY + 16);
                        g2.drawLine(cx + 8, iconY + 26, cx + 28, iconY + 24);

                        g2.dispose();
                    }
                };
                emptyState.setLayout(new BoxLayout(emptyState, BoxLayout.Y_AXIS));
                emptyState.setOpaque(false);

                emptyState.add(Box.createVerticalStrut(180));
                JLabel emptyText = new JLabel("Select a student and a book to preview");
                emptyText.setFont(BODY);
                emptyText.setForeground(new Color(255, 255, 255, 130));
                emptyText.setAlignmentX(Component.CENTER_ALIGNMENT);
                emptyState.add(emptyText);

                infoCard.add(emptyState, BorderLayout.CENTER);
            } else {
                Student student = selSItem.student;
                Book book = selBItem.book;

                JPanel livePanel = new JPanel();
                livePanel.setLayout(new BoxLayout(livePanel, BoxLayout.Y_AXIS));
                livePanel.setOpaque(false);

                JLabel infoTitle = new JLabel("Live Issue Preview");
                infoTitle.setFont(TITLE);
                infoTitle.setForeground(WHITE);
                infoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                livePanel.add(infoTitle);
                livePanel.add(Box.createVerticalStrut(16));

                // Warnings check
                int activeBooks = 0;
                double unpaidFines = 0.0;
                for (LibRecord r : records) {
                    if (r.getStudent().getId().equals(student.getId())) {
                        if (!r.isReturned()) {
                            activeBooks++;
                        } else if (r.getFine() > 0 && !"PAID".equalsIgnoreCase(r.getFineStatus())) {
                            unpaidFines += r.getFine();
                        }
                    }
                }

                ArrayList<String> warnings = new ArrayList<>();
                boolean disableIssue = false;

                if (book.getAvailableCopies() <= 0) {
                    warnings.add("Book has no copies left in library.");
                    disableIssue = true;
                }
                if (unpaidFines > 0) {
                    warnings.add("Student has unpaid fines of ₹" + (int) unpaidFines + ".");
                }
                if (activeBooks >= 3) {
                    warnings.add("Student already has " + activeBooks + " books issued (limit 3).");
                }

                // Amber banner for warnings
                if (!warnings.isEmpty()) {
                    JPanel banner = new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(new Color(251, 191, 36, 40)); // Amber translucent fill
                            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                            g2.setColor(new Color(245, 158, 11)); // Amber border
                            g2.setStroke(new BasicStroke(1.2f));
                            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                            g2.dispose();
                        }
                    };
                    banner.setLayout(new BoxLayout(banner, BoxLayout.Y_AXIS));
                    banner.setOpaque(false);
                    banner.setBorder(new EmptyBorder(8, 12, 8, 12));
                    banner.setAlignmentX(Component.LEFT_ALIGNMENT);

                    for (String wText : warnings) {
                        JLabel wLbl = new JLabel("⚠  " + wText);
                        wLbl.setFont(SMALL_BOLD);
                        wLbl.setForeground(new Color(254, 240, 138));
                        wLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
                        banner.add(wLbl);
                    }
                    livePanel.add(banner);
                    livePanel.add(Box.createVerticalStrut(14));
                }

                issueBtn.setEnabled(!disableIssue);

                // Book Details Row
                JPanel bookRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
                bookRow.setOpaque(false);
                bookRow.setAlignmentX(Component.LEFT_ALIGNMENT);

                JPanel cover = createScaledCoverThumb(book, 65, 95);
                bookRow.add(cover);

                JPanel bookText = new JPanel();
                bookText.setLayout(new BoxLayout(bookText, BoxLayout.Y_AXIS));
                bookText.setOpaque(false);

                JLabel bTitle = new JLabel("<html><body style='width: 220px;'>" + book.getTitle() + "</body></html>");
                bTitle.setFont(BODY_BOLD);
                bTitle.setForeground(WHITE);

                JLabel bAuthor = new JLabel("by " + book.getAuthor() + " (" + book.getCategory() + ")");
                bAuthor.setFont(SMALL);
                bAuthor.setForeground(new Color(255, 255, 255, 180));

                int avail = book.getAvailableCopies();
                int total = book.getTotalCopies();
                String copiesStr;
                if (avail > 0) {
                    copiesStr = "Copies: " + avail + " of " + total + " -> " + (avail - 1) + " of " + total + " after issue";
                } else {
                    copiesStr = "Copies: 0 of " + total + " (Out of stock)";
                }
                JLabel bCopies = new JLabel(copiesStr);
                bCopies.setFont(SMALL_BOLD);
                bCopies.setForeground(avail > 0 ? GOLD : new Color(248, 113, 113));

                bookText.add(bTitle);
                bookText.add(Box.createVerticalStrut(3));
                bookText.add(bAuthor);
                bookText.add(Box.createVerticalStrut(5));
                bookText.add(bCopies);
                bookRow.add(bookText);
                livePanel.add(bookRow);

                livePanel.add(Box.createVerticalStrut(18));

                // Student Details Row
                JPanel studentRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
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
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
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

                JLabel sName = new JLabel(student.getName());
                sName.setFont(BODY_BOLD);
                sName.setForeground(WHITE);

                JLabel sIdCourse = new JLabel(student.getId() + " · " + student.getCourse());
                sIdCourse.setFont(SMALL);
                sIdCourse.setForeground(new Color(255, 255, 255, 180));

                studentText.add(sName);
                studentText.add(Box.createVerticalStrut(3));
                studentText.add(sIdCourse);
                studentRow.add(studentText);
                livePanel.add(studentRow);

                livePanel.add(Box.createVerticalStrut(18));

                // Dates & Loan Rules
                JPanel dateBox = new JPanel();
                dateBox.setLayout(new BoxLayout(dateBox, BoxLayout.Y_AXIS));
                dateBox.setOpaque(false);
                dateBox.setAlignmentX(Component.LEFT_ALIGNMENT);

                java.time.LocalDate today = java.time.LocalDate.now();
                java.time.LocalDate due = today.plusDays(selectedDays[0]);
                java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("d MMM yyyy");

                JLabel issueDateLbl = new JLabel("Issue Date:  " + today.format(dtf) + " (Today)");
                issueDateLbl.setFont(SMALL);
                issueDateLbl.setForeground(new Color(255, 255, 255, 220));

                JLabel dueDateLbl = new JLabel("Due Date:    " + due.format(dtf) + " (" + selectedDays[0] + " days loan)");
                dueDateLbl.setFont(SMALL_BOLD);
                dueDateLbl.setForeground(GOLD);

                JLabel fineRuleLbl = new JLabel("Fine rule: 1-7 days: ₹5/day, 8-14: ₹10/day, 15+: ₹20/day");
                fineRuleLbl.setFont(SMALL);
                fineRuleLbl.setForeground(new Color(255, 255, 255, 140));

                dateBox.add(issueDateLbl);
                dateBox.add(Box.createVerticalStrut(4));
                dateBox.add(dueDateLbl);
                dateBox.add(Box.createVerticalStrut(6));
                dateBox.add(fineRuleLbl);

                livePanel.add(dateBox);
                infoCard.add(livePanel, BorderLayout.NORTH);
            }

            infoCard.revalidate();
            infoCard.repaint();
        };

        studentCombo.addActionListener(e -> updatePreviewRef[0].run());
        bookCombo.addActionListener(e -> updatePreviewRef[0].run());

        if (preselectedItem != null) {
            bookCombo.setSelectedItem(preselectedItem);
        }

        updatePreviewRef[0].run();

        // Issue button action listener
        issueBtn.addActionListener(e -> {
            IssueStudentComboItem selSItem = (IssueStudentComboItem) studentCombo.getSelectedItem();
            IssueBookComboItem selBItem = (IssueBookComboItem) bookCombo.getSelectedItem();
            if (selSItem == null || selSItem.student == null || selBItem == null || selBItem.book == null) return;

            Student student = selSItem.student;
            Book book = selBItem.book;
            int days = selectedDays[0];

            JTextField sf = new JTextField(student.getId());
            JTextField bf = new JTextField(book.getId());
            JTextField df = new JTextField(String.valueOf(days));

            boolean prev = suppressDialogs;
            suppressDialogs = true;
            issueBook(sf, bf, df);
            suppressDialogs = prev;

            // Toast feedback (fades after 3 seconds)
            java.time.LocalDate due = java.time.LocalDate.now().plusDays(days);
            java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("d MMM");
            String toastMsg = book.getTitle() + " issued to " + student.getName() + ", due " + due.format(dtf);
            showToast(toastMsg, TERRACOTTA);

            // Refresh recent issues card
            refreshRecentIssues.run();

            // Refresh book combo to update available copies
            ArrayList<IssueBookComboItem> updatedBookList = new ArrayList<>();
            updatedBookList.add(new IssueBookComboItem(null, "Select Book...", true));
            for (Book b : books) {
                String label = b.getTitle() + " — " + b.getAuthor() + " (" + b.getAvailableCopies() + " of " + b.getTotalCopies() + " available)";
                updatedBookList.add(new IssueBookComboItem(b, label, false));
            }
            DefaultComboBoxModel<IssueBookComboItem> newBookModel = new DefaultComboBoxModel<>();
            for (IssueBookComboItem item : updatedBookList) newBookModel.addElement(item);
            bookCombo.setModel(newBookModel);
            setupSearchableBookCombo(bookCombo, updatedBookList);

            // Reset selection
            studentCombo.setSelectedIndex(0);
            bookCombo.setSelectedIndex(0);

            // Reset chips to default 14 days
            isCustomSelected[0] = false;
            selectedDays[0] = 14;
            customDaysSpinner.setValue(14);
            customDaysSpinner.setVisible(false);
            daysPanel.revalidate();
            daysPanel.repaint();
            for (JButton b : chipBtns) b.repaint();

            updatePreviewRef[0].run();
        });

        return page;
    }

    // Helper: Style Combo Box with 44px height, rounded 8px border, terracotta focus border and custom arrow icon
    private <T> void styleIssueCombo(JComboBox<T> combo) {
        combo.setEditable(true);
        combo.setBackground(WHITE);
        combo.setFont(BODY);
        combo.setPreferredSize(new Dimension(380, 44));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        combo.setOpaque(false);

        Component editor = combo.getEditor().getEditorComponent();
        if (editor instanceof JTextField) {
            JTextField tf = (JTextField) editor;
            tf.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            tf.setOpaque(false);
            tf.setBackground(new Color(0, 0, 0, 0));
            tf.setForeground(INK);
        }

        boolean[] focused = {false};

        Border roundedBorder = new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (focused[0]) {
                    g2.setColor(TERRACOTTA);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawRoundRect(x + 1, y + 1, width - 2, height - 2, 8, 8);
                } else {
                    g2.setColor(SAND);
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(x, y, width - 1, height - 1, 8, 8);
                }
                g2.dispose();
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(3, 10, 3, 10);
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        };

        combo.setBorder(roundedBorder);

        combo.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        int w = getWidth();
                        int h = getHeight();
                        int[] xPoints = {w / 2 - 5, w / 2 + 5, w / 2};
                        int[] yPoints = {h / 2 - 2, h / 2 - 2, h / 2 + 4};
                        g2.setColor(MUTED);
                        g2.fillPolygon(xPoints, yPoints, 3);
                        g2.dispose();
                    }
                };
                btn.setBorderPainted(false);
                btn.setContentAreaFilled(false);
                btn.setFocusPainted(false);
                btn.setOpaque(false);
                btn.setPreferredSize(new Dimension(24, 24));
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                return btn;
            }
        });

        Component editorComp = combo.getEditor().getEditorComponent();
        if (editorComp instanceof JTextField) {
            JTextField tf = (JTextField) editorComp;
            tf.setFont(BODY);
            tf.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            tf.setBackground(WHITE);
            tf.setForeground(INK);

            tf.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    focused[0] = true;
                    combo.repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    focused[0] = false;
                    combo.repaint();
                }
            });
        }
    }

    // Helper: Searchable Student Combo filter
    private void setupSearchableStudentCombo(JComboBox<IssueStudentComboItem> combo, ArrayList<IssueStudentComboItem> allItems) {
        Component editor = combo.getEditor().getEditorComponent();
        if (!(editor instanceof JTextField)) return;
        JTextField tf = (JTextField) editor;

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                l.setFont(BODY);
                l.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                if (isSelected) {
                    l.setBackground(new Color(248, 241, 233));
                    l.setForeground(TERRACOTTA);
                } else {
                    l.setBackground(WHITE);
                    l.setForeground(INK);
                }
                return l;
            }
        });

        tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    return;
                }
                SwingUtilities.invokeLater(() -> {
                    String query = tf.getText();
                    combo.hidePopup();
                    DefaultComboBoxModel<IssueStudentComboItem> model = new DefaultComboBoxModel<>();
                    model.addElement(allItems.get(0)); // Placeholder

                    String lower = query.toLowerCase().trim();
                    for (int i = 1; i < allItems.size(); i++) {
                        IssueStudentComboItem item = allItems.get(i);
                        Student s = item.student;
                        if (query.isEmpty() || s.getName().toLowerCase().contains(lower) || s.getId().toLowerCase().contains(lower)) {
                            model.addElement(item);
                        }
                    }
                    combo.setModel(model);
                    tf.setText(query);
                    if (model.getSize() > 1) {
                        combo.showPopup();
                    }
                });
            }
        });
    }

    // Helper: Searchable Book Combo filter + grey out 0-copy books
    private void setupSearchableBookCombo(JComboBox<IssueBookComboItem> combo, ArrayList<IssueBookComboItem> allItems) {
        Component editor = combo.getEditor().getEditorComponent();
        if (!(editor instanceof JTextField)) return;
        JTextField tf = (JTextField) editor;

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                l.setFont(BODY);
                l.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

                if (value instanceof IssueBookComboItem) {
                    IssueBookComboItem item = (IssueBookComboItem) value;
                    if (!item.isPlaceholder && item.book != null && item.book.getAvailableCopies() <= 0) {
                        l.setForeground(new Color(170, 160, 150));
                        l.setBackground(new Color(245, 245, 245));
                        return l;
                    }
                }

                if (isSelected) {
                    l.setBackground(new Color(248, 241, 233));
                    l.setForeground(TERRACOTTA);
                } else {
                    l.setBackground(WHITE);
                    l.setForeground(INK);
                }
                return l;
            }
        });

        // Prevent selecting 0-copy books
        combo.addActionListener(e -> {
            IssueBookComboItem sel = (IssueBookComboItem) combo.getSelectedItem();
            if (sel != null && !sel.isPlaceholder && sel.book != null && sel.book.getAvailableCopies() <= 0) {
                SwingUtilities.invokeLater(() -> combo.setSelectedIndex(0));
            }
        });

        tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    return;
                }
                SwingUtilities.invokeLater(() -> {
                    String query = tf.getText();
                    combo.hidePopup();
                    DefaultComboBoxModel<IssueBookComboItem> model = new DefaultComboBoxModel<>();
                    model.addElement(allItems.get(0)); // Placeholder

                    String lower = query.toLowerCase().trim();
                    for (int i = 1; i < allItems.size(); i++) {
                        IssueBookComboItem item = allItems.get(i);
                        Book b = item.book;
                        if (query.isEmpty() || b.getTitle().toLowerCase().contains(lower) || b.getAuthor().toLowerCase().contains(lower) || b.getId().toLowerCase().contains(lower)) {
                            model.addElement(item);
                        }
                    }
                    combo.setModel(model);
                    tf.setText(query);
                    if (model.getSize() > 1) {
                        combo.showPopup();
                    }
                });
            }
        });
    }

    // Helper: Scaled cover thumbnail preserving aspect ratio
    private JPanel createScaledCoverThumb(Book book, int targetW, int targetH) {
        File imageFile = findCoverFile(book.getId());
        BufferedImage img = null;
        if (imageFile != null) {
            try {
                img = ImageIO.read(imageFile);
            } catch (IOException ignored) {}
        }
        final BufferedImage coverImage = img;

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                int pw = getWidth();
                int ph = getHeight();

                // Drop shadow / border
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(2, 2, pw - 3, ph - 3, 6, 6);

                java.awt.Shape clip = new java.awt.geom.RoundRectangle2D.Float(0, 0, pw - 2, ph - 2, 6, 6);
                g2.setClip(clip);

                g2.setColor(CREAM);
                g2.fillRect(0, 0, pw - 2, ph - 2);

                if (coverImage != null) {
                    int imgW = coverImage.getWidth();
                    int imgH = coverImage.getHeight();
                    double scale = Math.min((double) (pw - 2) / imgW, (double) (ph - 2) / imgH);
                    int dw = (int) (imgW * scale);
                    int dh = (int) (imgH * scale);
                    int dx = ((pw - 2) - dw) / 2;
                    int dy = ((ph - 2) - dh) / 2;
                    g2.drawImage(coverImage, dx, dy, dw, dh, null);
                } else {
                    g2.setColor(categoryColor(book.getCategory()));
                    g2.fillRect(0, 0, pw - 2, ph - 2);
                    g2.setColor(WHITE);
                    g2.setFont(SMALL_BOLD);
                    String initial = book.getTitle().length() > 0 ? book.getTitle().substring(0, 1) : "B";
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(initial, ((pw - 2) - fm.stringWidth(initial)) / 2, ((ph - 2) - fm.getHeight()) / 2 + fm.getAscent());
                }

                g2.setClip(null);
                g2.setColor(new Color(0, 0, 0, 30));
                g2.drawRoundRect(0, 0, pw - 2, ph - 2, 6, 6);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(targetW, targetH));
        return panel;
    }


    // =========================================================
    // RETURN PAGE (REFINED)
    // =========================================================

    private static class ActiveLoanComboItem {
        final LibRecord record;
        final String displayText;
        final boolean isPlaceholder;

        ActiveLoanComboItem(LibRecord record, String displayText, boolean isPlaceholder) {
            this.record = record;
            this.displayText = displayText;
            this.isPlaceholder = isPlaceholder;
        }

        @Override
        public String toString() {
            return displayText;
        }
    }

    JPanel createReturnPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(CREAM);

        // HEADER: Left padding 40px flush with heading
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(30, 40, 16, 40));

        JLabel title = new JLabel("Return a Book");
        title.setFont(DISPLAY);
        title.setForeground(INK);

        JLabel sub = new JLabel("Process returned books and calculate fines.");
        sub.setFont(BODY);
        sub.setForeground(MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(sub);
        page.add(header, BorderLayout.NORTH);

        // MAIN CONTENT CONTAINER (left edge 40px flush with header)
        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setOpaque(false);
        contentContainer.setBorder(new EmptyBorder(0, 40, 30, 40));

        // STATS ROW (Transaction stats without large empty gap)
        JPanel statsPanel = transactionStats(false);
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentContainer.add(statsPanel);
        contentContainer.add(Box.createVerticalStrut(16)); // compact spacing, no large empty gap

        // TOP ROW: Form Card (Left) and Live Fine Preview Card (Right)
        JPanel topRow = new JPanel(new GridBagLayout());
        topRow.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        // Form Card (Left)
        GlassPanel formCard = new GlassPanel(new Color(255, 252, 246, 235), new Color(255, 255, 255, 180));
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel formTitle = new JLabel("Return details");
        formTitle.setFont(TITLE);
        formTitle.setForeground(INK);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(formTitle);
        formCard.add(Box.createVerticalStrut(18));

        // Active Loan Picker
        JLabel loanLabel = new JLabel("Select Active Loan");
        loanLabel.setFont(BODY_BOLD);
        loanLabel.setForeground(INK);
        loanLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(loanLabel);
        formCard.add(Box.createVerticalStrut(6));

        java.time.LocalDate todayNow = java.time.LocalDate.now();
        java.time.format.DateTimeFormatter dtfShort = java.time.format.DateTimeFormatter.ofPattern("d MMM");
        ArrayList<ActiveLoanComboItem> loanItems = new ArrayList<>();
        ArrayList<LibRecord> activeRecords = new ArrayList<>();
        for (LibRecord r : records) {
            if (!r.isReturned()) {
                if (returnFilterOverdueOnly) {
                    long ds = java.time.temporal.ChronoUnit.DAYS.between(r.getIssueDate(), todayNow);
                    if (ds > r.getAllowedDays()) {
                        activeRecords.add(r);
                    }
                } else {
                    activeRecords.add(r);
                }
            }
        }

        if (activeRecords.isEmpty()) {
            loanItems.add(new ActiveLoanComboItem(null, returnFilterOverdueOnly ? "No overdue loans found." : "No books are currently on loan.", true));
        } else {
            loanItems.add(new ActiveLoanComboItem(null, returnFilterOverdueOnly ? "Select an overdue loan to return..." : "Select an active loan...", true));
            for (LibRecord r : activeRecords) {
                java.time.LocalDate due = r.getIssueDate().plusDays(r.getAllowedDays());
                long ds = java.time.temporal.ChronoUnit.DAYS.between(r.getIssueDate(), todayNow);
                int lateDays = (int) ds - r.getAllowedDays();
                String overdueBadge = lateDays > 0 ? " [⚠ " + lateDays + "d OVERDUE]" : "";
                String rowText = r.getStudent().getId() + " · " + r.getStudent().getName() + " — " + r.getBook().getTitle() + overdueBadge + " (due " + due.format(dtfShort) + ")";
                loanItems.add(new ActiveLoanComboItem(r, rowText, false));
            }
        }

        DefaultComboBoxModel<ActiveLoanComboItem> loanModel = new DefaultComboBoxModel<>();
        for (ActiveLoanComboItem item : loanItems) loanModel.addElement(item);

        JComboBox<ActiveLoanComboItem> loanCombo = new JComboBox<>(loanModel);
        styleIssueCombo(loanCombo);
        loanCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        loanCombo.setPreferredSize(new Dimension(380, 44));
        loanCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        setupSearchableLoanCombo(loanCombo, loanItems);
        formCard.add(loanCombo);
        formCard.add(Box.createVerticalStrut(18));

        // Return Date Field with -1 / +1 day buttons
        JLabel returnDateLabel = new JLabel("Return Date");
        returnDateLabel.setFont(BODY_BOLD);
        returnDateLabel.setForeground(INK);
        returnDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(returnDateLabel);
        formCard.add(Box.createVerticalStrut(6));

        final java.time.LocalDate[] returnDateHolder = {java.time.LocalDate.now()};
        java.time.format.DateTimeFormatter dateDisplayFmt = java.time.format.DateTimeFormatter.ofPattern("d MMMM yyyy");

        JPanel dateRow = new JPanel(new BorderLayout(8, 0));
        dateRow.setOpaque(false);
        dateRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        dateRow.setPreferredSize(new Dimension(380, 44));
        dateRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Date Display Pill/Field
        JLabel dateTextLbl = new JLabel(returnDateHolder[0].format(dateDisplayFmt)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.setColor(SAND);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        dateTextLbl.setOpaque(false);
        dateTextLbl.setFont(BODY_BOLD);
        dateTextLbl.setForeground(INK);
        dateTextLbl.setIcon(createLineIcon("calendar", 16, TERRACOTTA));
        dateTextLbl.setIconTextGap(10);
        dateTextLbl.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));
        dateRow.add(dateTextLbl, BorderLayout.CENTER);

        // -1 Day and +1 Day Buttons
        JPanel dateControlBtns = new JPanel(new GridLayout(1, 2, 6, 0));
        dateControlBtns.setOpaque(false);

        JButton minusDayBtn = new JButton("−1d") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CREAM);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                g2.setColor(SAND);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                g2.setColor(INK);
                g2.setFont(SMALL_BOLD);
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        minusDayBtn.setOpaque(false);
        minusDayBtn.setContentAreaFilled(false);
        minusDayBtn.setBorderPainted(false);
        minusDayBtn.setFocusPainted(false);
        minusDayBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        minusDayBtn.setPreferredSize(new Dimension(50, 44));

        JButton plusDayBtn = new JButton("+1d") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CREAM);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                g2.setColor(SAND);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                g2.setColor(INK);
                g2.setFont(SMALL_BOLD);
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        plusDayBtn.setOpaque(false);
        plusDayBtn.setContentAreaFilled(false);
        plusDayBtn.setBorderPainted(false);
        plusDayBtn.setFocusPainted(false);
        plusDayBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        plusDayBtn.setPreferredSize(new Dimension(50, 44));

        dateControlBtns.add(minusDayBtn);
        dateControlBtns.add(plusDayBtn);
        dateRow.add(dateControlBtns, BorderLayout.EAST);

        formCard.add(dateRow);
        formCard.add(Box.createVerticalStrut(18));

        // Read-only Days Kept and Days Late cards
        JPanel statsRow = new JPanel(new GridLayout(1, 2, 10, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel keptCard = new JPanel();
        keptCard.setLayout(new BoxLayout(keptCard, BoxLayout.Y_AXIS));
        keptCard.setBackground(WHITE);
        keptCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(SAND, 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        JLabel keptTitle = new JLabel("DAYS KEPT");
        keptTitle.setFont(SMALL_BOLD);
        keptTitle.setForeground(MUTED);
        JLabel keptVal = new JLabel("—");
        keptVal.setFont(TITLE);
        keptVal.setForeground(INK);
        keptCard.add(keptTitle);
        keptCard.add(keptVal);

        JPanel lateCard = new JPanel();
        lateCard.setLayout(new BoxLayout(lateCard, BoxLayout.Y_AXIS));
        lateCard.setBackground(WHITE);
        lateCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(SAND, 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        JLabel lateTitle = new JLabel("DAYS LATE");
        lateTitle.setFont(SMALL_BOLD);
        lateTitle.setForeground(MUTED);
        JLabel lateVal = new JLabel("—");
        lateVal.setFont(TITLE);
        lateVal.setForeground(INK);
        lateCard.add(lateTitle);
        lateCard.add(lateVal);

        statsRow.add(keptCard);
        statsRow.add(lateCard);
        formCard.add(statsRow);
        formCard.add(Box.createVerticalStrut(24));

        final JCheckBox markPaidToggle = new JCheckBox("Collect fine now / Mark as paid", true);
        markPaidToggle.setOpaque(false);
        markPaidToggle.setFont(BODY_BOLD);
        markPaidToggle.setForeground(WHITE);
        markPaidToggle.setFocusPainted(false);
        markPaidToggle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Return Button with custom painting and antialiasing
        JButton returnBtn = new JButton("Return Book") {
            private boolean hovered = false;
            {
                setOpaque(false);
                setContentAreaFilled(false);
                setFocusPainted(false);
                setBorderPainted(false);
                setFont(BODY_BOLD);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (isEnabled()) {
                            hovered = true;
                            repaint();
                        }
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                if (!isEnabled()) {
                    g2.setColor(new Color(220, 212, 202)); // muted sand fill
                    g2.fillRoundRect(0, 0, w, h, 8, 8);
                    g2.setColor(new Color(145, 138, 130)); // grey text
                } else if (hovered) {
                    g2.setColor(new Color(156, 78, 58)); // darkened terracotta
                    g2.fillRoundRect(0, 0, w, h, 8, 8);
                    g2.setColor(WHITE);
                } else {
                    g2.setColor(TERRACOTTA); // solid terracotta
                    g2.fillRoundRect(0, 0, w, h, 8, 8);
                    g2.setColor(WHITE);
                }

                g2.setFont(BODY_BOLD);
                FontMetrics fm = g2.getFontMetrics();
                String text = getText();
                int tx = (w - fm.stringWidth(text)) / 2;
                int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(text, tx, ty);
                g2.dispose();
            }
        };
        returnBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        returnBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        returnBtn.setPreferredSize(new Dimension(380, 46));
        returnBtn.setEnabled(false);
        formCard.add(returnBtn);

        // Live Fine Preview Card (Right)
        GlassPanel infoCard = new GlassPanel(INK, new Color(255, 255, 255, 45));
        infoCard.setLayout(new BorderLayout());
        infoCard.setBorder(new EmptyBorder(25, 28, 25, 28));

        // Pack Top Row
        gbc.gridx = 0;
        gbc.weightx = 0.52;
        gbc.insets = new Insets(0, 0, 0, 16);
        topRow.add(formCard, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.48;
        gbc.insets = new Insets(0, 0, 0, 0);
        topRow.add(infoCard, gbc);

        contentContainer.add(topRow);
        contentContainer.add(Box.createVerticalStrut(20));

        // Recent Returns Card (Below form)
        JPanel recentReturnsCard = new GlassPanel(new Color(255, 252, 246, 235), new Color(255, 255, 255, 180));
        recentReturnsCard.setLayout(new BoxLayout(recentReturnsCard, BoxLayout.Y_AXIS));
        recentReturnsCard.setBorder(new EmptyBorder(22, 28, 22, 28));

        Runnable refreshRecentReturns = () -> {
            recentReturnsCard.removeAll();

            JPanel recentHead = new JPanel(new BorderLayout());
            recentHead.setOpaque(false);
            JLabel recentTitle = new JLabel("Recent Returns");
            recentTitle.setFont(TITLE);
            recentTitle.setForeground(INK);
            recentHead.add(recentTitle, BorderLayout.WEST);
            recentReturnsCard.add(recentHead);
            recentReturnsCard.add(Box.createVerticalStrut(14));

            ArrayList<LibRecord> returnedList = new ArrayList<>();
            for (int i = records.size() - 1; i >= 0; i--) {
                LibRecord r = records.get(i);
                if (r.isReturned()) {
                    returnedList.add(r);
                    if (returnedList.size() >= 5) break;
                }
            }

            if (returnedList.isEmpty()) {
                JPanel emptyRecent = new JPanel();
                emptyRecent.setLayout(new BoxLayout(emptyRecent, BoxLayout.Y_AXIS));
                emptyRecent.setOpaque(false);
                emptyRecent.setBorder(new EmptyBorder(16, 0, 16, 0));

                JLabel lblEmpty = new JLabel("No recent book returns found");
                lblEmpty.setFont(BODY);
                lblEmpty.setForeground(MUTED);
                lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
                emptyRecent.add(lblEmpty);
                recentReturnsCard.add(emptyRecent);
            } else {
                java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("d MMM yyyy");
                for (int i = 0; i < returnedList.size(); i++) {
                    LibRecord r = returnedList.get(i);
                    JPanel row = new JPanel(new BorderLayout(14, 0));
                    row.setOpaque(false);
                    row.setBorder(new EmptyBorder(6, 4, 6, 4));

                    // Small cover thumbnail (preserves aspect ratio)
                    JPanel thumb = createScaledCoverThumb(r.getBook(), 34, 46);
                    row.add(thumb, BorderLayout.WEST);

                    // Book <- Student
                    JPanel centerPane = new JPanel();
                    centerPane.setLayout(new BoxLayout(centerPane, BoxLayout.Y_AXIS));
                    centerPane.setOpaque(false);

                    JLabel titleStud = new JLabel(r.getBook().getTitle() + "  ←  " + r.getStudent().getName());
                    titleStud.setFont(BODY_BOLD);
                    titleStud.setForeground(INK);

                    java.time.LocalDate returnDate = r.getIssueDate().plusDays(r.getActualDays());
                    JLabel dateInfo = new JLabel("Returned on " + returnDate.format(dtf) + "  ·  Student ID: " + r.getStudent().getId());
                    dateInfo.setFont(SMALL);
                    dateInfo.setForeground(MUTED);

                    centerPane.add(titleStud);
                    centerPane.add(Box.createVerticalStrut(2));
                    centerPane.add(dateInfo);
                    row.add(centerPane, BorderLayout.CENTER);

                    // Fine pill (sage Rs 0 or terracotta Rs X, fully rounded)
                    int fineAmount = (int) r.getFine();
                    JLabel finePill = new JLabel(fineAmount == 0 ? "₹0" : "₹" + fineAmount, SwingConstants.CENTER) {
                        @Override
                        protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(fineAmount == 0 ? SAGE : TERRACOTTA);
                            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                            g2.setColor(WHITE);
                            g2.setFont(SMALL_BOLD);
                            FontMetrics fm = g2.getFontMetrics();
                            int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                            int ty = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                            g2.drawString(getText(), tx, ty);
                            g2.dispose();
                        }
                    };
                    finePill.setOpaque(false);
                    finePill.setFont(SMALL_BOLD);
                    finePill.setPreferredSize(new Dimension(65, 26));

                    JPanel rightPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
                    rightPane.setOpaque(false);
                    rightPane.add(finePill);
                    row.add(rightPane, BorderLayout.EAST);

                    recentReturnsCard.add(row);
                    if (i < returnedList.size() - 1) {
                        JSeparator sep = new JSeparator();
                        sep.setForeground(new Color(230, 222, 212));
                        recentReturnsCard.add(sep);
                    }
                }
            }
            recentReturnsCard.revalidate();
            recentReturnsCard.repaint();
        };

        refreshRecentReturns.run();
        contentContainer.add(recentReturnsCard);

        JScrollPane scrollPane = new JScrollPane(contentContainer);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        page.add(scrollPane, BorderLayout.CENTER);

        // Preview Updater Runnable
        Runnable[] updatePreviewRef = new Runnable[1];
        updatePreviewRef[0] = () -> {
            infoCard.removeAll();

            ActiveLoanComboItem selectedLoanItem = (ActiveLoanComboItem) loanCombo.getSelectedItem();
            boolean hasSelection = selectedLoanItem != null && !selectedLoanItem.isPlaceholder && selectedLoanItem.record != null;

            if (!hasSelection) {
                returnBtn.setText("Return Book");
                returnBtn.setEnabled(false);
                keptVal.setText("—");
                lateVal.setText("—");

                JPanel emptyState = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        int w = getWidth();
                        int iconY = 110;
                        int cx = w / 2;

                        // Line-drawn receipt icon with Graphics2D
                        g2.setColor(new Color(255, 255, 255, 80));
                        g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                        int rx = cx - 24;
                        int ry = iconY;
                        int rw = 48;
                        int rh = 56;

                        // Receipt outline with zigzag bottom
                        int[] xPts = {rx, rx + rw, rx + rw, rx + 40, rx + 32, rx + 24, rx + 16, rx + 8, rx};
                        int[] yPts = {ry, ry, ry + rh, ry + rh - 5, ry + rh, ry + rh - 5, ry + rh, ry + rh - 5, ry + rh};
                        g2.drawPolygon(xPts, yPts, 9);

                        // Receipt lines
                        g2.setColor(new Color(255, 255, 255, 45));
                        g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        g2.drawLine(rx + 8, ry + 12, rx + 40, ry + 12);
                        g2.drawLine(rx + 8, ry + 22, rx + 34, ry + 22);
                        g2.drawLine(rx + 8, ry + 32, rx + 40, ry + 32);
                        g2.drawLine(rx + 8, ry + 42, rx + 26, ry + 42);

                        g2.dispose();
                    }
                };
                emptyState.setLayout(new BoxLayout(emptyState, BoxLayout.Y_AXIS));
                emptyState.setOpaque(false);

                emptyState.add(Box.createVerticalStrut(180));
                JLabel emptyText = new JLabel("Select a loan to calculate the fine");
                emptyText.setFont(BODY);
                emptyText.setForeground(new Color(255, 255, 255, 130));
                emptyText.setAlignmentX(Component.CENTER_ALIGNMENT);
                emptyState.add(emptyText);

                infoCard.add(emptyState, BorderLayout.CENTER);
            } else {
                LibRecord record = selectedLoanItem.record;
                Student student = record.getStudent();
                Book book = record.getBook();
                java.time.LocalDate issueDate = record.getIssueDate();
                java.time.LocalDate returnDate = returnDateHolder[0];

                // Days kept & late calculation
                int daysKept = (int) java.time.temporal.ChronoUnit.DAYS.between(issueDate, returnDate);
                if (daysKept < 0) daysKept = 0;
                int allowedDays = record.getAllowedDays();
                int daysLate = daysKept - allowedDays;
                if (daysLate < 0) daysLate = 0;

                keptVal.setText(String.valueOf(daysKept));
                lateVal.setText(String.valueOf(daysLate));

                final int daysLateFinal = daysLate;
                final int daysKeptFinal = daysKept;

                // Fine calculation logic (category-dependent with delayed days slabs)
                double fineAmount = LibRecord.calculateFineAmount(daysLate, book.getCategory());
                String slabName;
                String rateDesc;
                double multiplier = book.getCategory().equalsIgnoreCase("Reference") ? 1.5 : (book.getCategory().equalsIgnoreCase("General") ? 0.8 : 1.0);
                if (daysLate == 0) {
                    slabName = "On Time (No slab)";
                    rateDesc = "₹0 / day";
                } else if (daysLate <= 7) {
                    slabName = "Slab 1: 1–7 days (" + book.getCategory() + ")";
                    rateDesc = "₹" + (int)(5 * multiplier) + " / day";
                } else if (daysLate <= 14) {
                    slabName = "Slab 2: 8–14 days (" + book.getCategory() + ")";
                    rateDesc = "₹" + (int)(10 * multiplier) + " / day";
                } else {
                    slabName = "Slab 3: 15+ days (" + book.getCategory() + ")";
                    rateDesc = "₹" + (int)(20 * multiplier) + " / day";
                }

                returnBtn.setEnabled(true);

                JPanel livePanel = new JPanel();
                livePanel.setLayout(new BoxLayout(livePanel, BoxLayout.Y_AXIS));
                livePanel.setOpaque(false);

                JLabel infoTitle = new JLabel("Live Fine Preview");
                infoTitle.setFont(TITLE);
                infoTitle.setForeground(WHITE);
                infoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                livePanel.add(infoTitle);
                livePanel.add(Box.createVerticalStrut(14));

                // Status Banner: Sage if 0 late, Terracotta if late
                JPanel banner = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        if (daysLateFinal == 0) {
                            g2.setColor(new Color(105, 126, 98, 70));
                            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                            g2.setColor(SAGE);
                        } else {
                            g2.setColor(new Color(184, 98, 77, 70));
                            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                            g2.setColor(TERRACOTTA);
                        }
                        g2.setStroke(new BasicStroke(1.2f));
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                        g2.dispose();
                    }
                };
                banner.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 8));
                banner.setOpaque(false);
                banner.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel bannerText = new JLabel(daysLate == 0 ? "✓  Returned on time, no fine" : "⚠  Overdue by " + daysLate + " days — Fine: ₹" + (int)fineAmount);
                bannerText.setFont(BODY_BOLD);
                bannerText.setForeground(WHITE);
                banner.add(bannerText);

                livePanel.add(banner);
                livePanel.add(Box.createVerticalStrut(16));

                // Book Row (with thumbnail preserving aspect ratio, Title, Student ID, Category Chip)
                JPanel bookRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
                bookRow.setOpaque(false);
                bookRow.setAlignmentX(Component.LEFT_ALIGNMENT);

                JPanel thumb = createScaledCoverThumb(book, 60, 88);
                bookRow.add(thumb);

                JPanel bookDetails = new JPanel();
                bookDetails.setLayout(new BoxLayout(bookDetails, BoxLayout.Y_AXIS));
                bookDetails.setOpaque(false);

                JLabel bTitle = new JLabel("<html><body style='width: 220px;'>" + book.getTitle() + "</body></html>");
                bTitle.setFont(BODY_BOLD);
                bTitle.setForeground(WHITE);

                JLabel sNameId = new JLabel(student.getName() + " (" + student.getId() + ")");
                sNameId.setFont(SMALL);
                sNameId.setForeground(new Color(255, 255, 255, 190));

                // Category chip (categoryColor)
                JLabel catChip = new JLabel("  " + book.getCategory() + "  ") {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(categoryColor(book.getCategory()));
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                        g2.setColor(WHITE);
                        g2.setFont(SMALL_BOLD);
                        FontMetrics fm = g2.getFontMetrics();
                        int tx = (getWidth() - fm.stringWidth(getText().trim())) / 2;
                        int ty = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                        g2.drawString(getText().trim(), tx, ty);
                        g2.dispose();
                    }
                };
                catChip.setOpaque(false);
                catChip.setFont(SMALL_BOLD);
                catChip.setPreferredSize(new Dimension(80, 22));

                bookDetails.add(bTitle);
                bookDetails.add(Box.createVerticalStrut(3));
                bookDetails.add(sNameId);
                bookDetails.add(Box.createVerticalStrut(6));
                bookDetails.add(catChip);
                bookRow.add(bookDetails);
                livePanel.add(bookRow);

                livePanel.add(Box.createVerticalStrut(18));

                // Dates Breakdown Grid
                JPanel dateGrid = new JPanel(new GridLayout(6, 2, 8, 6));
                dateGrid.setOpaque(false);
                dateGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

                java.time.LocalDate dueDate = issueDate.plusDays(allowedDays);
                java.time.format.DateTimeFormatter dtfLong = java.time.format.DateTimeFormatter.ofPattern("d MMM yyyy");

                String[][] gridData = {
                    {"Issue Date:", issueDate.format(dtfLong)},
                    {"Due Date:", dueDate.format(dtfLong)},
                    {"Return Date:", returnDate.format(dtfLong)},
                    {"Days Late:", daysLate + " days"},
                    {"Slab Applied:", slabName},
                    {"Rate per Day:", rateDesc}
                };
                for (String[] row : gridData) {
                    JLabel kLbl = new JLabel(row[0]);
                    kLbl.setFont(SMALL_BOLD);
                    kLbl.setForeground(new Color(255, 255, 255, 140));
                    JLabel vLbl = new JLabel(row[1]);
                    vLbl.setFont(BODY_BOLD);
                    vLbl.setForeground(WHITE);
                    dateGrid.add(kLbl);
                    dateGrid.add(vLbl);
                }

                livePanel.add(dateGrid);
                livePanel.add(Box.createVerticalStrut(16));

                if (fineAmount > 0) {
                    livePanel.add(markPaidToggle);
                    livePanel.add(Box.createVerticalStrut(10));
                    returnBtn.setText("Return Book & Record ₹" + (int)fineAmount + " Fine");
                } else {
                    returnBtn.setText("Return Book");
                }

                // Large Total Fine Display
                JPanel fineBox = new JPanel();
                fineBox.setLayout(new BoxLayout(fineBox, BoxLayout.Y_AXIS));
                fineBox.setOpaque(false);
                fineBox.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel totalFineHeading = new JLabel("TOTAL FINE");
                totalFineHeading.setFont(SMALL_BOLD);
                totalFineHeading.setForeground(GOLD);

                JLabel totalFineVal = new JLabel("₹" + (int) fineAmount);
                totalFineVal.setFont(DISPLAY.deriveFont(Font.BOLD, 36f));
                totalFineVal.setForeground(fineAmount == 0 ? new Color(130, 205, 125) : (fineAmount <= 50 ? GOLD : TERRACOTTA));

                fineBox.add(totalFineHeading);
                fineBox.add(totalFineVal);
                livePanel.add(fineBox);

                infoCard.add(livePanel, BorderLayout.NORTH);
            }

            infoCard.revalidate();
            infoCard.repaint();
        };

        loanCombo.addActionListener(e -> {
            returnDateHolder[0] = java.time.LocalDate.now();
            dateTextLbl.setText(returnDateHolder[0].format(dateDisplayFmt));
            updatePreviewRef[0].run();
        });

        // -1 Day and +1 Day action listeners
        minusDayBtn.addActionListener(e -> {
            ActiveLoanComboItem selectedLoanItem = (ActiveLoanComboItem) loanCombo.getSelectedItem();
            java.time.LocalDate candidate = returnDateHolder[0].minusDays(1);
            if (selectedLoanItem != null && selectedLoanItem.record != null) {
                java.time.LocalDate issueDate = selectedLoanItem.record.getIssueDate();
                if (candidate.isBefore(issueDate)) {
                    showToast("Return date cannot be before issue date (" + issueDate.format(dtfShort) + ")", TERRACOTTA);
                    return;
                }
            }
            returnDateHolder[0] = candidate;
            dateTextLbl.setText(returnDateHolder[0].format(dateDisplayFmt));
            updatePreviewRef[0].run();
        });

        plusDayBtn.addActionListener(e -> {
            java.time.LocalDate candidate = returnDateHolder[0].plusDays(1);
            if (candidate.isAfter(java.time.LocalDate.now())) {
                showToast("Return date cannot be in the future", TERRACOTTA);
                return;
            }
            returnDateHolder[0] = candidate;
            dateTextLbl.setText(returnDateHolder[0].format(dateDisplayFmt));
            updatePreviewRef[0].run();
        });

        updatePreviewRef[0].run();

        // Return Button Action: open modal fine receipt dialog, trigger toast, and persist
        returnBtn.addActionListener(e -> {
            ActiveLoanComboItem selectedLoanItem = (ActiveLoanComboItem) loanCombo.getSelectedItem();
            if (selectedLoanItem == null || selectedLoanItem.record == null) return;

            LibRecord record = selectedLoanItem.record;
            java.time.LocalDate returnDate = returnDateHolder[0];

            int daysKept = (int) java.time.temporal.ChronoUnit.DAYS.between(record.getIssueDate(), returnDate);
            if (daysKept < 0) daysKept = 0;
            int allowedDays = record.getAllowedDays();
            int daysLate = daysKept - allowedDays;
            if (daysLate < 0) daysLate = 0;
            double fineAmount = LibRecord.calculateFineAmount(daysLate, record.getBook() != null ? record.getBook().getCategory() : "General");

            // Execute return record update
            String fineStatus = fineAmount > 0 ? (markPaidToggle.isSelected() ? "PAID" : "UNPAID") : "NONE";
            record.setReturnData(daysKept, daysLate, fineAmount, fineStatus);
            record.getBook().returnCopy();
            saveData();

            // Open Fine Receipt Dialog modal
            showFineReceiptModal(record, returnDate, daysLate, fineAmount);

            // Success feedback toast (fades after 3 seconds)
            String toastMsg = record.getBook().getTitle() + " returned by " + record.getStudent().getName() + ", fine ₹" + (int) fineAmount;
            showToast(toastMsg, SAGE);

            // Refresh recent returns
            refreshRecentReturns.run();

            // Refresh active loans combo
            ArrayList<ActiveLoanComboItem> updatedLoanItems = new ArrayList<>();
            ArrayList<LibRecord> updatedActive = new ArrayList<>();
            for (LibRecord r : records) {
                if (!r.isReturned()) updatedActive.add(r);
            }
            if (updatedActive.isEmpty()) {
                updatedLoanItems.add(new ActiveLoanComboItem(null, "No books are currently on loan.", true));
            } else {
                updatedLoanItems.add(new ActiveLoanComboItem(null, "Select an active loan...", true));
                for (LibRecord r : updatedActive) {
                    java.time.LocalDate due = r.getIssueDate().plusDays(r.getAllowedDays());
                    String rowText = r.getStudent().getId() + " · " + r.getStudent().getName() + " — " + r.getBook().getTitle() + " (due " + due.format(dtfShort) + ")";
                    updatedLoanItems.add(new ActiveLoanComboItem(r, rowText, false));
                }
            }
            DefaultComboBoxModel<ActiveLoanComboItem> newLoanModel = new DefaultComboBoxModel<>();
            for (ActiveLoanComboItem it : updatedLoanItems) newLoanModel.addElement(it);
            loanCombo.setModel(newLoanModel);
            setupSearchableLoanCombo(loanCombo, updatedLoanItems);

            // Reset form
            loanCombo.setSelectedIndex(0);
            returnDateHolder[0] = java.time.LocalDate.now();
            dateTextLbl.setText(returnDateHolder[0].format(dateDisplayFmt));

            updatePreviewRef[0].run();
        });

        return page;
    }

    // Helper: Meta key/value label for Live Fine Preview
    private JPanel createMetaLabel(String key, String value) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        JLabel kLbl = new JLabel(key + " ");
        kLbl.setFont(SMALL_BOLD);
        kLbl.setForeground(new Color(255, 255, 255, 140));
        JLabel vLbl = new JLabel(value);
        vLbl.setFont(SMALL_BOLD);
        vLbl.setForeground(WHITE);
        p.add(kLbl);
        p.add(vLbl);
        return p;
    }

    // Helper: Searchable Active Loan Combo Filter
    private void setupSearchableLoanCombo(JComboBox<ActiveLoanComboItem> combo, ArrayList<ActiveLoanComboItem> allItems) {
        Component editor = combo.getEditor().getEditorComponent();
        if (!(editor instanceof JTextField)) return;
        JTextField tf = (JTextField) editor;

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                l.setFont(BODY);
                l.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                if (isSelected) {
                    l.setBackground(new Color(248, 241, 233));
                    l.setForeground(TERRACOTTA);
                } else {
                    l.setBackground(WHITE);
                    l.setForeground(INK);
                }
                return l;
            }
        });

        tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    return;
                }
                SwingUtilities.invokeLater(() -> {
                    String query = tf.getText();
                    combo.hidePopup();
                    DefaultComboBoxModel<ActiveLoanComboItem> model = new DefaultComboBoxModel<>();
                    model.addElement(allItems.get(0)); // Placeholder

                    String lower = query.toLowerCase().trim();
                    for (int i = 1; i < allItems.size(); i++) {
                        ActiveLoanComboItem item = allItems.get(i);
                        if (query.isEmpty() || item.displayText.toLowerCase().contains(lower)) {
                            model.addElement(item);
                        }
                    }
                    combo.setModel(model);
                    tf.setText(query);
                    if (model.getSize() > 1) {
                        combo.showPopup();
                    }
                });
            }
        });
    }

    // Helper: Modal Fine Receipt Dialog drawn with Graphics2D, dashed divider, and PrinterJob print
    private void showFineReceiptModal(LibRecord record, java.time.LocalDate returnDate, int daysLate, double fineAmount) {
        JDialog dlg = new JDialog(this, "Fine Receipt", true);
        dlg.setUndecorated(true);
        dlg.setBackground(new Color(0, 0, 0, 0)); // Transparent dialog frame

        // Dimmed backdrop panel
        JPanel backdrop = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 140)); // dimmed background
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        backdrop.setOpaque(false);

        // Receipt Card Data
        long receiptNo = System.currentTimeMillis() % 1000000L;
        String bCat = record.getBook() != null ? record.getBook().getCategory() : "General";
        double mult = bCat.equalsIgnoreCase("Reference") ? 1.5 : (bCat.equalsIgnoreCase("General") ? 0.8 : 1.0);
        String slabApplied = daysLate == 0 ? "None (On time)" : (daysLate <= 7 ? "Slab 1 (1-7d, " + bCat + ")" : (daysLate <= 14 ? "Slab 2 (8-14d, " + bCat + ")" : "Slab 3 (15+d, " + bCat + ")"));
        String rateText = daysLate == 0 ? "₹0/day" : (daysLate <= 7 ? "₹" + (int)(5 * mult) + "/day" : (daysLate <= 14 ? "₹" + (int)(10 * mult) + "/day" : "₹" + (int)(20 * mult) + "/day"));
        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("d MMM yyyy");
        java.time.LocalDate dueDate = record.getIssueDate().plusDays(record.getAllowedDays());

        // Printable Receipt Card Panel
        JPanel receiptCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Drop shadow
                g2.setColor(new Color(0, 0, 0, 45));
                g2.fillRoundRect(4, 4, w - 8, h - 8, 16, 16);

                // Card background
                g2.setColor(PAPER);
                g2.fillRoundRect(0, 0, w - 8, h - 8, 16, 16);
                g2.setColor(SAND);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, w - 8, h - 8, 16, 16);

                // Header
                g2.setColor(TERRACOTTA);
                g2.fillRect(0, 0, w - 8, 6); // terracotta top accent

                g2.setColor(INK);
                g2.setFont(customSerif.deriveFont(Font.BOLD, 20f));
                g2.drawString("MINDSPACE LIBRARY", 30, 42);

                g2.setFont(SMALL_BOLD);
                g2.setColor(MUTED);
                g2.drawString("OFFICIAL RETURN & FINE RECEIPT", 30, 60);
                g2.drawString("Receipt #: REC-" + receiptNo, 30, 78);

                // Top dashed divider
                drawDashedLine(g2, 30, w - 38, 92);

                // Receipt Fields
                int y = 118;
                int valX = w - 40;
                y = drawReceiptRow(g2, "Student Name:", record.getStudent().getName(), y, 30, valX, false);
                y = drawReceiptRow(g2, "Student ID:", record.getStudent().getId() + " (" + record.getStudent().getCourse() + ")", y, 30, valX, false);
                y = drawReceiptRow(g2, "Book Title:", record.getBook().getTitle(), y, 30, valX, false);
                y = drawReceiptRow(g2, "Book Category:", record.getBook().getCategory(), y, 30, valX, false);
                y = drawReceiptRow(g2, "Issue Date:", record.getIssueDate().format(dtf), y, 30, valX, false);
                y = drawReceiptRow(g2, "Due Date:", dueDate.format(dtf), y, 30, valX, false);
                y = drawReceiptRow(g2, "Return Date:", returnDate.format(dtf), y, 30, valX, false);
                y = drawReceiptRow(g2, "Days Late:", daysLate + " days", y, 30, valX, true);
                y = drawReceiptRow(g2, "Slab Applied:", slabApplied, y, 30, valX, false);
                y = drawReceiptRow(g2, "Fine Rate:", rateText, y, 30, valX, true);

                // Bottom dashed divider
                drawDashedLine(g2, 30, w - 38, y + 6);
                y += 32;

                // Total Row (monospaced amount)
                g2.setColor(INK);
                g2.setFont(TITLE);
                g2.drawString("TOTAL FINE", 30, y);

                g2.setFont(new Font("Monospaced", Font.BOLD, 22));
                g2.setColor(fineAmount == 0 ? SAGE : TERRACOTTA);
                String fineStr = "₹ " + String.format("%.2f", fineAmount);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(fineStr, valX - fm.stringWidth(fineStr), y);

                g2.dispose();
            }

            private void drawDashedLine(Graphics2D g2, int x1, int x2, int y) {
                Stroke prev = g2.getStroke();
                float[] dash = {6f, 4f};
                g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash, 0f));
                g2.setColor(new Color(180, 170, 160));
                g2.drawLine(x1, y, x2, y);
                g2.setStroke(prev);
            }

            private int drawReceiptRow(Graphics2D g2, String label, String val, int y, int x, int valX, boolean mono) {
                g2.setColor(MUTED);
                g2.setFont(SMALL_BOLD);
                g2.drawString(label, x, y);

                if (mono) {
                    g2.setFont(new Font("Monospaced", Font.BOLD, 12));
                } else {
                    g2.setFont(BODY_BOLD);
                }
                g2.setColor(INK);
                FontMetrics fm = g2.getFontMetrics();
                int textW = fm.stringWidth(val);
                g2.drawString(val, valX - textW, y);
                return y + 24;
            }
        };

        receiptCard.setOpaque(false);
        receiptCard.setPreferredSize(new Dimension(440, 520));

        // Receipt Card Wrapper with Buttons
        JPanel cardWrapper = new JPanel(new BorderLayout());
        cardWrapper.setOpaque(false);
        cardWrapper.add(receiptCard, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        btnRow.setOpaque(false);

        JButton printBtn = new JButton("Print Receipt");
        printBtn.setFont(BODY_BOLD);
        printBtn.setBackground(INK);
        printBtn.setForeground(WHITE);
        printBtn.setOpaque(true);
        printBtn.setBorderPainted(false);
        printBtn.setFocusPainted(false);
        printBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        printBtn.setPreferredSize(new Dimension(130, 38));

        printBtn.addActionListener(ev -> {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName("Fine_Receipt_" + receiptNo);
            job.setPrintable((graphics, pageFormat, pageIndex) -> {
                if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                double scale = Math.min(pageFormat.getImageableWidth() / receiptCard.getWidth(),
                                        pageFormat.getImageableHeight() / receiptCard.getHeight());
                g2d.scale(scale, scale);
                receiptCard.paint(g2d);
                return Printable.PAGE_EXISTS;
            });
            boolean doPrint = job.printDialog();
            if (doPrint) {
                try {
                    job.print();
                    showToast("Receipt sent to printer", SAGE);
                } catch (PrinterException ex) {
                    showToast("Print error: " + ex.getMessage(), TERRACOTTA);
                }
            }
        });

        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(BODY_BOLD);
        closeBtn.setBackground(CREAM);
        closeBtn.setForeground(INK);
        closeBtn.setOpaque(true);
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.setPreferredSize(new Dimension(90, 38));
        closeBtn.addActionListener(ev -> dlg.dispose());

        btnRow.add(printBtn);
        btnRow.add(closeBtn);
        cardWrapper.add(btnRow, BorderLayout.SOUTH);

        backdrop.add(cardWrapper);
        dlg.setContentPane(backdrop);
        dlg.setSize(520, 620);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private JComboBox<String> createSearchableCombo(ArrayList<String> items, String placeholder) {
        JComboBox<String> cb = new JComboBox<>();
        cb.addItem(placeholder);
        for(String i : items) cb.addItem(i);
        cb.setEditable(true);
        cb.setBackground(WHITE);
        cb.setFont(BODY);
        cb.setBorder(BorderFactory.createLineBorder(new Color(220, 215, 210), 1));
        
        JTextField tf = (JTextField) cb.getEditor().getEditorComponent();
        tf.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                cb.setBorder(BorderFactory.createLineBorder(TERRACOTTA, 2));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                cb.setBorder(BorderFactory.createLineBorder(new Color(220, 215, 210), 1));
            }
        });
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

    JPanel transactionStats(boolean issueMode) {
        int totalCopies = 0, availableCopies = 0, issuedCopies = 0;
        double totalFine = 0;
        for (Book book : books) {
            totalCopies += book.getTotalCopies();
            availableCopies += book.getAvailableCopies();
        }
        issuedCopies = totalCopies - availableCopies;
        for (LibRecord record : records) {
            if (record.isReturned()) totalFine += record.getFine();
        }
        
        JPanel stats = new JPanel(new GridLayout(1, 3, 12, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        stats.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        if (issueMode) {
            stats.add(statCard("AVAILABLE BOOKS", String.valueOf(availableCopies), "copies ready to issue", SAGE));
            stats.add(statCard("REGISTERED STUDENTS", String.valueOf(students.size()), "active library users", TERRACOTTA));
            stats.add(statCard("CURRENTLY ON LOAN", String.valueOf(issuedCopies), "books already issued", GOLD));
        } else {
            stats.add(statCard("CURRENTLY ON LOAN", String.valueOf(issuedCopies), "books awaiting return", GOLD));
            stats.add(statCard("AVAILABLE BOOKS", String.valueOf(availableCopies), "copies in the library", SAGE));
            stats.add(statCard("RECORDED FINES", String.valueOf((int) totalFine), "total fines collected (₹)", TERRACOTTA));
        }
        return stats;
    }


    // =========================================================
    // FORM FIELD
    // =========================================================

    JTextField formField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(360, 42));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setFont(BODY);
        field.setForeground(INK);
        field.setBackground(PAPER);
        field.setCaretColor(TERRACOTTA);
        field.setBorder(BorderFactory.createCompoundBorder(new LineBorder(SAND, 1), new EmptyBorder(0, 12, 0, 12)));
        return field;
    }


    // =========================================================
    // INFORMATION ROW
    // =========================================================

    void addInfoRow(JPanel panel, String number, String heading, String description) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        
        JLabel numberLabel = new JLabel(number);
        numberLabel.setFont(customSerif.deriveFont(Font.BOLD, 15f));
        numberLabel.setForeground(GOLD);
        numberLabel.setPreferredSize(new Dimension(35, 30));
        row.add(numberLabel, BorderLayout.WEST);
        
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        
        JLabel title = new JLabel(heading);
        title.setFont(BODY_BOLD);
        title.setForeground(WHITE);
        
        JLabel descriptionLabel = new JLabel("<html><div style='width:230px'>" + description + "</div></html>");
        descriptionLabel.setFont(SMALL);
        descriptionLabel.setForeground(new Color(215, 205, 195));
        
        text.add(title);
        text.add(Box.createVerticalStrut(2));
        text.add(descriptionLabel);
        
        row.add(text, BorderLayout.CENTER);
        panel.add(row);
        panel.add(Box.createVerticalStrut(7));
    }


    // =========================================================
    // ACTION BUTTON
    // =========================================================

    JButton actionButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(155, 44));
        button.setMaximumSize(new Dimension(155, 44));
        button.setFont(BODY_BOLD);
        button.setForeground(WHITE);
        button.setBackground(TERRACOTTA);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(new Color(160, 78, 60)); }
            public void mouseExited(MouseEvent e) { button.setBackground(TERRACOTTA); }
        });
        return button;
    }


    // =========================================================
    // ISSUE BOOK
    // =========================================================

    void issueBook(JTextField studentField, JTextField bookField, JTextField daysField) {
        String studentId = studentField.getText().trim();
        String bookId = bookField.getText().trim();
        String daysText = daysField.getText().trim();
        if (studentId.isEmpty() || bookId.isEmpty() || daysText.isEmpty()) {
            warning("Please fill all fields.");
            return;
        }
        int allowedDays;
        try {
            allowedDays = Integer.parseInt(daysText);
        } catch (NumberFormatException e) {
            warning("Allowed days must be a number.");
            return;
        }
        if (allowedDays <= 0) {
            warning("Allowed days must be greater than zero.");
            return;
        }
        Student student = findStudent(studentId);
        Book book = findBook(bookId);
        if (student == null) {
            warning("Student ID not found.");
            return;
        }
        if (book == null) {
            warning("Book ID not found.");
            return;
        }
        if (!book.isAvailable()) {
            warning("This book is currently unavailable.");
            return;
        }
        if (findActiveRecord(studentId, bookId) != null) {
            warning("This student already has this book issued.");
            return;
        }
        
        records.add(new LibRecord(student, book, allowedDays));
        book.issueCopy();
        saveData();
        
        if (!suppressDialogs) JOptionPane.showMessageDialog(this,
                "BOOK ISSUED SUCCESSFULLY\n\nStudent : " + student.getName() +
                "\nBook : " + book.getTitle() + "\nAllowed Days : " + allowedDays,
                "MindSpace Library", JOptionPane.INFORMATION_MESSAGE);
                
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
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(CREAM);

        // Header: 40px left & right padding to match other pages
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(30, 40, 16, 40));

        JLabel title = new JLabel("Fine Records");
        title.setFont(DISPLAY);
        title.setForeground(INK);

        JLabel sub = new JLabel("Track returned loans, late fees, and receipt records.");
        sub.setFont(BODY);
        sub.setForeground(MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(sub);
        page.add(header, BorderLayout.NORTH);

        // Main content wrapper with matching 40px horizontal padding
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(0, 40, 24, 40));

        // ── 1. COMPACT STAT CARDS (~110px high) ───────────────────
        double totalFineSum = 0;
        int lateReturnsCount = 0;
        int returnedCount = 0;

        for (LibRecord r : records) {
            if (r.isReturned()) {
                returnedCount++;
                totalFineSum += r.getFine();
                if (r.getDelayedDays() > 0) {
                    lateReturnsCount++;
                }
            }
        }

        JPanel statsRow = new JPanel(new GridLayout(1, 3, 16, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        statsRow.setPreferredSize(new Dimension(800, 110));
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        statsRow.add(createCompactFineStatCard(
            "TOTAL FINE",
            "₹" + (int) totalFineSum,
            "accumulated library fines",
            TERRACOTTA,
            "fine"
        ));
        statsRow.add(createCompactFineStatCard(
            "LATE RETURNS",
            String.valueOf(lateReturnsCount),
            "books returned overdue",
            GOLD,
            "clock"
        ));
        statsRow.add(createCompactFineStatCard(
            "RETURNED",
            String.valueOf(returnedCount),
            "books safely returned",
            SAGE,
            "check"
        ));

        contentPanel.add(statsRow);
        contentPanel.add(Box.createVerticalStrut(16));

        // ── 2 & 4. TABLE CARD WITH TOOLBAR, JTABLE & SUMMARY STRIP ─
        GlassPanel tableCard = new GlassPanel(new Color(255, 252, 246, 235), new Color(255, 255, 255, 180));
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(16, 20, 16, 20));
        tableCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        // --- Toolbar ---
        JPanel toolbar = new JPanel(new BorderLayout(12, 0));
        toolbar.setOpaque(false);
        toolbar.setBorder(new EmptyBorder(4, 4, 14, 4));

        JPanel leftFilters = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftFilters.setOpaque(false);

        // Search Field
        JTextField searchField = new JTextField(16);
        searchField.setPreferredSize(new Dimension(220, 36));
        searchField.setFont(BODY);
        searchField.setForeground(INK);
        searchField.setBackground(WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 215, 205), 1, true),
            new EmptyBorder(4, 10, 4, 10)
        ));

        // Search placeholder text behavior
        final String searchPlaceholder = "Search student or book...";
        searchField.setText(searchPlaceholder);
        searchField.setForeground(MUTED);
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(searchPlaceholder)) {
                    searchField.setText("");
                    searchField.setForeground(INK);
                }
                searchField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(TERRACOTTA, 1, true),
                    new EmptyBorder(4, 10, 4, 10)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().trim().isEmpty()) {
                    searchField.setText(searchPlaceholder);
                    searchField.setForeground(MUTED);
                }
                searchField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 215, 205), 1, true),
                    new EmptyBorder(4, 10, 4, 10)
                ));
            }
        });

        // Status Filter: All / On time / Late
        String[] statusOptions = {"All Status", "On time", "Late"};
        JComboBox<String> statusCombo = new JComboBox<>(statusOptions);
        statusCombo.setFont(BODY);
        statusCombo.setPreferredSize(new Dimension(120, 36));
        statusCombo.setBackground(WHITE);
        statusCombo.setForeground(INK);
        statusCombo.setBorder(BorderFactory.createLineBorder(new Color(220, 215, 205), 1));

        // Sort Control: Newest, Highest fine
        String[] sortOptions = {"Newest", "Highest fine"};
        JComboBox<String> sortCombo = new JComboBox<>(sortOptions);
        sortCombo.setFont(BODY);
        sortCombo.setPreferredSize(new Dimension(135, 36));
        sortCombo.setBackground(WHITE);
        sortCombo.setForeground(INK);
        sortCombo.setBorder(BorderFactory.createLineBorder(new Color(220, 215, 205), 1));

        leftFilters.add(searchField);
        leftFilters.add(statusCombo);
        leftFilters.add(sortCombo);

        JLabel recordCounter = new JLabel("Showing 0 of 0 records");
        recordCounter.setFont(SMALL);
        recordCounter.setForeground(MUTED);

        JPanel rightCountPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        rightCountPanel.setOpaque(false);
        rightCountPanel.add(recordCounter);

        toolbar.add(leftFilters, BorderLayout.WEST);
        toolbar.add(rightCountPanel, BorderLayout.EAST);
        tableCard.add(toolbar, BorderLayout.NORTH);

        // --- Table & Empty State Stack ---
        JPanel tableCenterPanel = new JPanel(new CardLayout());
        tableCenterPanel.setOpaque(false);

        // Table Model definition
        String[] columns = {"Student", "Book", "Category", "Days Late", "Fine", "Status", "Receipt"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 6; // only Receipt column has interactive button
            }
            @Override
            public Class<?> getColumnClass(int col) {
                if (col == 3) return Integer.class;
                if (col == 4) return Double.class;
                return Object.class;
            }
        };

        final JTable table = new JTable(tableModel);
        table.setRowHeight(52);
        table.setShowGrid(false);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setOpaque(false);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setBackground(new Color(0, 0, 0, 0));

        // Hover row tracking
        final int[] hoveredRow = {-1};
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int r = table.rowAtPoint(e.getPoint());
                if (r != hoveredRow[0]) {
                    hoveredRow[0] = r;
                    table.repaint();
                }
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredRow[0] = -1;
                table.repaint();
            }
        });

        // Custom JTableHeader: Cream tint, small caps font, subtle 1px bottom border
        JTableHeader th = table.getTableHeader();
        th.setReorderingAllowed(false);
        th.setResizingAllowed(true);
        th.setPreferredSize(new Dimension(0, 36));
        th.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFocus, int r, int c) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, isSel, hasFocus, r, c);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
                lbl.setForeground(new Color(115, 105, 95));
                lbl.setBackground(new Color(245, 240, 232));
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(222, 214, 202)),
                    new EmptyBorder(0, 12, 0, 12)
                ));
                if (c == 3 || c == 4) {
                    lbl.setHorizontalAlignment(SwingConstants.RIGHT);
                } else if (c == 5 || c == 6) {
                    lbl.setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    lbl.setHorizontalAlignment(SwingConstants.LEFT);
                }
                String s = val == null ? "" : val.toString().toUpperCase();
                lbl.setText(s);
                return lbl;
            }
        });

        // Row renderer with 1px subtle divider and hover highlight
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFocus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, isSel, hasFocus, row, col);
                lbl.setFont(BODY);
                lbl.setForeground(INK);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(238, 232, 222)),
                    new EmptyBorder(0, 12, 0, 12)
                ));

                if (row == hoveredRow[0]) {
                    lbl.setBackground(new Color(248, 243, 235));
                    lbl.setOpaque(true);
                } else {
                    lbl.setOpaque(false);
                }
                return lbl;
            }
        });

        // Numeric renderer for Days Late (Col 3, Right aligned)
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFocus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, isSel, hasFocus, row, col);
                lbl.setHorizontalAlignment(SwingConstants.RIGHT);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(238, 232, 222)),
                    new EmptyBorder(0, 12, 0, 12)
                ));
                int days = (val instanceof Integer) ? (Integer) val : 0;
                lbl.setText(days == 0 ? "0d" : days + "d");
                lbl.setFont(BODY_BOLD);
                lbl.setForeground(days > 0 ? TERRACOTTA : SAGE);

                if (row == hoveredRow[0]) {
                    lbl.setBackground(new Color(248, 243, 235));
                    lbl.setOpaque(true);
                } else {
                    lbl.setOpaque(false);
                }
                return lbl;
            }
        });

        // Fine Amount renderer (Col 4, Right aligned, terracotta if > 0, grey "—" if 0)
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFocus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, isSel, hasFocus, row, col);
                lbl.setHorizontalAlignment(SwingConstants.RIGHT);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(238, 232, 222)),
                    new EmptyBorder(0, 12, 0, 12)
                ));
                double fineVal = (val instanceof Number) ? ((Number) val).doubleValue() : 0.0;
                if (fineVal > 0) {
                    lbl.setText("₹" + (int) fineVal);
                    lbl.setFont(BODY_BOLD);
                    lbl.setForeground(TERRACOTTA);
                } else {
                    lbl.setText("—");
                    lbl.setFont(BODY);
                    lbl.setForeground(new Color(160, 155, 148));
                }

                if (row == hoveredRow[0]) {
                    lbl.setBackground(new Color(248, 243, 235));
                    lbl.setOpaque(true);
                } else {
                    lbl.setOpaque(false);
                }
                return lbl;
            }
        });

        // Category Chip Renderer (Col 2: Tinted rounded chip)
        table.getColumnModel().getColumn(2).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFocus, final int row, int col) {
                final String cat = val == null ? "General" : val.toString();
                final Color catCol = categoryColor(cat);

                JPanel cell = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 13)) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        if (row == hoveredRow[0]) {
                            g.setColor(new Color(248, 243, 235));
                            g.fillRect(0, 0, getWidth(), getHeight());
                        }
                        g.setColor(new Color(238, 232, 222));
                        g.fillRect(0, getHeight() - 1, getWidth(), 1);
                        super.paintComponent(g);
                    }
                };
                cell.setOpaque(false);

                JPanel chip = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(catCol.getRed(), catCol.getGreen(), catCol.getBlue(), 28));
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                        g2.setColor(new Color(catCol.getRed(), catCol.getGreen(), catCol.getBlue(), 120));
                        g2.setStroke(new BasicStroke(1.0f));
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                        g2.dispose();
                    }
                };
                chip.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 3));
                chip.setOpaque(false);

                JLabel l = new JLabel(cat);
                l.setFont(new Font("SansSerif", Font.BOLD, 11));
                l.setForeground(catCol);
                chip.add(l);

                cell.add(chip);
                return cell;
            }
        });

        // Status Pill Renderer (Col 5: sage "On time", gold "Late", terracotta "Fine due")
        table.getColumnModel().getColumn(5).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFocus, final int row, int col) {
                String raw = val == null ? "" : val.toString();
                final String text;
                final Color pillBg;
                final Color pillFg;

                if ("On time".equalsIgnoreCase(raw)) {
                    text = "On time";
                    pillBg = new Color(228, 240, 224);
                    pillFg = SAGE;
                } else if ("Fine due".equalsIgnoreCase(raw)) {
                    text = "Fine due";
                    pillBg = new Color(252, 230, 225);
                    pillFg = TERRACOTTA;
                } else {
                    text = "Late";
                    pillBg = new Color(254, 244, 220);
                    pillFg = new Color(180, 120, 30);
                }

                JPanel cell = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 12)) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        if (row == hoveredRow[0]) {
                            g.setColor(new Color(248, 243, 235));
                            g.fillRect(0, 0, getWidth(), getHeight());
                        }
                        g.setColor(new Color(238, 232, 222));
                        g.fillRect(0, getHeight() - 1, getWidth(), 1);
                        super.paintComponent(g);
                    }
                };
                cell.setOpaque(false);

                JPanel pill = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(pillBg);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                        g2.setColor(new Color(pillFg.getRed(), pillFg.getGreen(), pillFg.getBlue(), 80));
                        g2.setStroke(new BasicStroke(1.0f));
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                        g2.dispose();
                    }
                };
                pill.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 3));
                pill.setOpaque(false);

                JLabel l = new JLabel(text);
                l.setFont(new Font("SansSerif", Font.BOLD, 11));
                l.setForeground(pillFg);
                pill.add(l);

                cell.add(pill);
                return cell;
            }
        });

        // Visible records list for matching rows with LibRecord instances
        final ArrayList<LibRecord> displayedRecords = new ArrayList<>();

        // Receipt Button Renderer & Editor (Col 6)
        class ReceiptButtonRenderer extends JPanel implements TableCellRenderer {
            private final JButton btn;
            private int renderRow = -1;

            ReceiptButtonRenderer() {
                super(new FlowLayout(FlowLayout.CENTER, 4, 11));
                setOpaque(false);
                btn = new JButton("Receipt");
                btn.setFont(new Font("SansSerif", Font.BOLD, 11));
                btn.setForeground(INK);
                btn.setBackground(WHITE);
                btn.setFocusPainted(false);
                btn.setPreferredSize(new Dimension(72, 28));
                btn.setBorder(BorderFactory.createLineBorder(new Color(210, 205, 195), 1, true));
                add(btn);
            }

            @Override
            protected void paintComponent(Graphics g) {
                if (renderRow == hoveredRow[0]) {
                    g.setColor(new Color(248, 243, 235));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                g.setColor(new Color(238, 232, 222));
                g.fillRect(0, getHeight() - 1, getWidth(), 1);
                super.paintComponent(g);
            }

            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFocus, int r, int c) {
                this.renderRow = r;
                return this;
            }
        }

        class ReceiptButtonEditor extends DefaultCellEditor {
            private final JPanel panel;
            private final JButton btn;
            private int currentRow = -1;

            ReceiptButtonEditor() {
                super(new JCheckBox());
                panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 11)) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        if (currentRow == hoveredRow[0]) {
                            g.setColor(new Color(248, 243, 235));
                            g.fillRect(0, 0, getWidth(), getHeight());
                        }
                        g.setColor(new Color(238, 232, 222));
                        g.fillRect(0, getHeight() - 1, getWidth(), 1);
                        super.paintComponent(g);
                    }
                };
                panel.setOpaque(false);
                btn = new JButton("Receipt");
                btn.setFont(new Font("SansSerif", Font.BOLD, 11));
                btn.setForeground(INK);
                btn.setBackground(WHITE);
                btn.setFocusPainted(false);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btn.setPreferredSize(new Dimension(72, 28));
                btn.setBorder(BorderFactory.createLineBorder(TERRACOTTA, 1, true));

                btn.addActionListener(e -> {
                    fireEditingStopped();
                    if (currentRow >= 0 && currentRow < displayedRecords.size()) {
                        LibRecord rec = displayedRecords.get(currentRow);
                        java.time.LocalDate retDate = rec.getIssueDate().plusDays(rec.getActualDays());
                        showFineReceiptModal(rec, retDate, rec.getDelayedDays(), rec.getFine());
                    }
                });
                panel.add(btn);
            }

            @Override
            public Component getTableCellEditorComponent(JTable t, Object value, boolean isSelected, int r, int c) {
                this.currentRow = r;
                return panel;
            }

            @Override
            public Object getCellEditorValue() {
                return "Receipt";
            }
        }

        table.getColumnModel().getColumn(6).setCellRenderer(new ReceiptButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ReceiptButtonEditor());

        // Preferred column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(170); // Student
        table.getColumnModel().getColumn(1).setPreferredWidth(210); // Book
        table.getColumnModel().getColumn(2).setPreferredWidth(110); // Category
        table.getColumnModel().getColumn(3).setPreferredWidth(80);  // Days Late
        table.getColumnModel().getColumn(4).setPreferredWidth(85);  // Fine
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Status
        table.getColumnModel().getColumn(6).setPreferredWidth(95);  // Receipt

        // Scroll pane for the table inside the card with slim styled scrollbar
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createEmptyBorder());
        tableScroll.setOpaque(false);
        tableScroll.getViewport().setOpaque(false);
        tableScroll.getVerticalScrollBar().setUnitIncrement(16);
        tableScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Slim styled scrollbar
        tableScroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        tableScroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(205, 195, 185);
                this.trackColor = new Color(245, 240, 235, 120);
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            private JButton createZeroButton() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                b.setMinimumSize(new Dimension(0, 0));
                b.setMaximumSize(new Dimension(0, 0));
                return b;
            }
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 6, 6);
                g2.dispose();
            }
        });

        // ── 5. EMPTY STATE PANEL (Line-drawn receipt icon & return jump button) ─
        JPanel emptyStatePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int cx = getWidth() / 2;
                int cy = 90;

                // Draw line-drawn receipt icon
                g2.setColor(new Color(220, 212, 202));
                g2.fillRoundRect(cx - 24, cy - 32, 48, 64, 8, 8);
                g2.setColor(TERRACOTTA);
                g2.setStroke(new BasicStroke(1.8f));
                g2.drawRoundRect(cx - 24, cy - 32, 48, 64, 8, 8);

                // Dashed receipt tear lines
                Stroke dashed = new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3, 3}, 0);
                g2.setStroke(dashed);
                g2.setColor(MUTED);
                g2.drawLine(cx - 16, cy - 18, cx + 16, cy - 18);
                g2.drawLine(cx - 16, cy - 6, cx + 16, cy - 6);
                g2.drawLine(cx - 16, cy + 6, cx + 16, cy + 6);
                g2.drawLine(cx - 16, cy + 18, cx + 10, cy + 18);

                g2.dispose();
            }
        };
        emptyStatePanel.setOpaque(false);
        emptyStatePanel.setLayout(new BoxLayout(emptyStatePanel, BoxLayout.Y_AXIS));

        emptyStatePanel.add(Box.createVerticalStrut(140));
        JLabel emptyMsg = new JLabel("No fines recorded yet. Returned books will appear here.");
        emptyMsg.setFont(BODY);
        emptyMsg.setForeground(MUTED);
        emptyMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
        emptyStatePanel.add(emptyMsg);
        emptyStatePanel.add(Box.createVerticalStrut(16));

        JButton jumpReturnBtn = new JButton("Go to Return Book");
        jumpReturnBtn.setFont(BODY_BOLD);
        jumpReturnBtn.setBackground(TERRACOTTA);
        jumpReturnBtn.setForeground(WHITE);
        jumpReturnBtn.setFocusPainted(false);
        jumpReturnBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jumpReturnBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        jumpReturnBtn.setPreferredSize(new Dimension(170, 38));
        jumpReturnBtn.setMaximumSize(new Dimension(170, 38));
        jumpReturnBtn.addActionListener(e -> showPage("RETURN"));
        emptyStatePanel.add(jumpReturnBtn);
        emptyStatePanel.add(Box.createVerticalStrut(30));

        tableCenterPanel.add(tableScroll, "TABLE");
        tableCenterPanel.add(emptyStatePanel, "EMPTY");
        CardLayout centerCardLayout = (CardLayout) tableCenterPanel.getLayout();

        tableCard.add(tableCenterPanel, BorderLayout.CENTER);

        // ── 7. SUMMARY STRIP ──────────────────────────────────────
        final JPanel summaryStrip = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 10));
        summaryStrip.setOpaque(false);
        final JLabel summaryLabel = new JLabel("");
        summaryLabel.setFont(SMALL);
        summaryLabel.setForeground(MUTED);
        summaryStrip.add(summaryLabel);
        tableCard.add(summaryStrip, BorderLayout.SOUTH);

        // Filter / Sort / Refresh handler
        Runnable refreshTable = () -> {
            String q = searchField.getText().trim();
            if (q.equals(searchPlaceholder)) q = "";
            String queryLower = q.toLowerCase();

            String statusSel = (String) statusCombo.getSelectedItem();
            String sortSel = (String) sortCombo.getSelectedItem();

            displayedRecords.clear();
            double highestFine = 0.0;
            String highestFineDesc = "None";
            double fineSum = 0.0;
            int totalReturnedRecords = 0;

            for (LibRecord r : records) {
                if (!r.isReturned()) continue;
                totalReturnedRecords++;

                // Track highest and fine sum across all returned records
                if (r.getFine() > highestFine) {
                    highestFine = r.getFine();
                    highestFineDesc = "₹" + (int) highestFine + " (" + r.getStudent().getName() + ", " + r.getBook().getTitle() + ")";
                }
                fineSum += r.getFine();

                // Status filter
                if ("On time".equals(statusSel) && r.getDelayedDays() > 0) continue;
                if ("Late".equals(statusSel) && r.getDelayedDays() == 0) continue;

                // Query filter (matches student name/id or book title/id)
                if (!queryLower.isEmpty()) {
                    boolean matchStudent = r.getStudent().getName().toLowerCase().contains(queryLower)
                        || r.getStudent().getId().toLowerCase().contains(queryLower);
                    boolean matchBook = r.getBook().getTitle().toLowerCase().contains(queryLower)
                        || r.getBook().getId().toLowerCase().contains(queryLower);
                    if (!matchStudent && !matchBook) continue;
                }

                displayedRecords.add(r);
            }

            // Sorting
            if ("Highest fine".equals(sortSel)) {
                displayedRecords.sort((a, b) -> Double.compare(b.getFine(), a.getFine()));
            } else {
                // Newest: assume records added later or reverse order
                java.util.Collections.reverse(displayedRecords);
            }

            // Populate table model
            tableModel.setRowCount(0);
            for (LibRecord r : displayedRecords) {
                String studentStr = r.getStudent().getName() + " (" + r.getStudent().getId() + ")";
                String bookStr = r.getBook().getTitle();
                String category = r.getBook().getCategory();
                int daysLate = r.getDelayedDays();
                double fine = r.getFine();

                String status;
                if (daysLate == 0) {
                    status = "On time";
                } else if ("PAID".equalsIgnoreCase(r.getFineStatus())) {
                    status = "Late";
                } else {
                    status = "Fine due";
                }

                tableModel.addRow(new Object[]{
                    studentStr,
                    bookStr,
                    category,
                    daysLate,
                    fine,
                    status,
                    "Receipt"
                });
            }

            recordCounter.setText("Showing " + displayedRecords.size() + " of " + totalReturnedRecords + " records");

            // Empty state check
            if (displayedRecords.isEmpty()) {
                centerCardLayout.show(tableCenterPanel, "EMPTY");
                summaryStrip.setVisible(false);
            } else {
                centerCardLayout.show(tableCenterPanel, "TABLE");
                summaryStrip.setVisible(true);

                double avgFine = totalReturnedRecords > 0 ? (fineSum / totalReturnedRecords) : 0.0;
                String avgFineFormatted = String.format(java.util.Locale.US, "%.1f", avgFine);
                if (avgFineFormatted.endsWith(".0")) {
                    avgFineFormatted = String.valueOf((int) avgFine);
                }

                String summaryText;
                if (highestFine > 0) {
                    summaryText = "Highest fine: " + highestFineDesc + "  ·  Average fine: ₹" + avgFineFormatted;
                } else {
                    summaryText = "Highest fine: ₹0 (No overdue fines)  ·  Average fine: ₹0";
                }
                summaryLabel.setText(summaryText);
            }

            table.revalidate();
            table.repaint();
        };

        // Wire filter listeners
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { refreshTable.run(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { refreshTable.run(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { refreshTable.run(); }
        });
        statusCombo.addActionListener(e -> refreshTable.run());
        sortCombo.addActionListener(e -> refreshTable.run());

        // Initial populate
        refreshTable.run();

        contentPanel.add(tableCard);

        page.add(contentPanel, BorderLayout.CENTER);
        return page;
    }

    // Helper: Compact 110px Stat Card with Graphics2D line-drawn icon in a tinted circle
    private JPanel createCompactFineStatCard(String label, String value, String caption, Color accent, String iconType) {
        GlassPanel card = new GlassPanel(new Color(255, 252, 246, 235), new Color(255, 255, 255, 180));
        card.setLayout(new BorderLayout(14, 0));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));
        card.setPreferredSize(new Dimension(240, 110));

        // Tinted Circle with line-drawn icon
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int d = 46;
                int x = (getWidth() - d) / 2;
                int y = (getHeight() - d) / 2;

                // Tinted circle
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 28));
                g2.fillOval(x, y, d, d);
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 90));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawOval(x, y, d, d);

                // Line-drawn icon (no emoji)
                g2.setColor(accent);
                g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cx = x + d / 2;
                int cy = y + d / 2;

                if ("fine".equals(iconType)) {
                    // Currency / Coin / Rupee symbol lines
                    g2.drawOval(cx - 10, cy - 10, 20, 20);
                    g2.drawLine(cx - 5, cy - 3, cx + 5, cy - 3);
                    g2.drawLine(cx - 5, cy + 1, cx + 3, cy + 1);
                    g2.drawLine(cx - 2, cy - 6, cx - 2, cy + 6);
                } else if ("clock".equals(iconType)) {
                    // Clock circle and hands
                    g2.drawOval(cx - 10, cy - 10, 20, 20);
                    g2.drawLine(cx, cy, cx, cy - 5);
                    g2.drawLine(cx, cy, cx + 4, cy + 2);
                } else if ("check".equals(iconType)) {
                    // Checkmark
                    g2.drawLine(cx - 7, cy, cx - 2, cy + 5);
                    g2.drawLine(cx - 2, cy + 5, cx + 7, cy - 5);
                }
                g2.dispose();
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(50, 78));
        card.add(iconPanel, BorderLayout.WEST);

        // Text Info (Big Number + Label + Caption)
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel titleLbl = new JLabel(label);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        titleLbl.setForeground(accent);

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font("SansSerif", Font.BOLD, 26));
        valLbl.setForeground(INK);

        JLabel capLbl = new JLabel(caption);
        capLbl.setFont(SMALL);
        capLbl.setForeground(MUTED);

        textPanel.add(titleLbl);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(valLbl);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(capLbl);

        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    // =========================================================
    // FIND METHODS
    // =========================================================

    Book findBook(String id) {
        for (Book book : books) {
            if (book.getId().equalsIgnoreCase(id)) return book;
        }
        return null;
    }


    Student findStudent(String id) {
        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(id)) return student;
        }
        return null;
    }


    LibRecord findActiveRecord(String studentId, String bookId) {
        for (LibRecord record : records) {
            if (record.getStudent().getId().equalsIgnoreCase(studentId) &&
                record.getBook().getId().equalsIgnoreCase(bookId) &&
                !record.isReturned()) {
                return record;
            }
        }
        return null;
    }


    // =========================================================
    // WARNING
    // =========================================================

    void warning(String message) {
        JOptionPane.showMessageDialog(this, message, "MindSpace Library", JOptionPane.WARNING_MESSAGE);
    }


    // =========================================================
    // GLASS PANEL
    // =========================================================

    static class GlassPanel extends JPanel {
        private Color fill;
        private Color borderColor;
        GlassPanel(Color fill, Color borderColor) {
            this.fill = fill;
            this.borderColor = borderColor;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getWidth(), height = getHeight();
            g2.setColor(new Color(60, 45, 35, 25)); // soft shadow
            g2.fillRoundRect(3, 4, width - 6, height - 5, 18, 18);
            g2.setColor(fill); // glass background
            g2.fillRoundRect(0, 0, width - 1, height - 1, 18, 18);
            g2.setColor(borderColor); // border
            g2.drawRoundRect(0, 0, width - 1, height - 1, 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
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