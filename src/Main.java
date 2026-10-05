import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Student> students;
    static ArrayList<Book> books;
    static ArrayList<LibRecord> records;

    // Start library
    static void startLibrary() {
        FileManager.setupFiles();

        students = FileManager.loadStudents();
        books = FileManager.loadBooks();
        records = FileManager.loadRecords(students, books);
    }

    // Main menu
    static void showMenu() {

        System.out.println("\n==========================================");
        System.out.println("           MINDSPACE LIBRARY");
        System.out.println("        FINE MANAGEMENT SYSTEM");
        System.out.println("==========================================");
        System.out.println("1. View Book Catalog");
        System.out.println("2. Search Book");
        System.out.println("3. Issue Book");
        System.out.println("4. Return Book");
        System.out.println("5. Add New Book");
        System.out.println("6. Search Student");
        System.out.println("7. View Fine Receipts");
        System.out.println("8. Library Summary");
        System.out.println("9. Exit");
        System.out.println("==========================================");
    }

    // View all books
    static void viewBooks() {

        System.out.println("\n============= BOOK CATALOG =============");

        for (Book book : books) {
            book.display();
        }

        System.out.println("------------------------------------------");
    }

    // Search book by name
    static void searchBook() {

        System.out.println("\n------------- SEARCH BOOK ----------------");

        System.out.print("Enter book name: ");
        String name = sc.nextLine().toLowerCase();

        boolean found = false;

        for (Book book : books) {

            if (book.getTitle().toLowerCase().contains(name)) {

                System.out.println("\nBook Found!");
                book.display();

                found = true;
            }
        }

        if (!found) {
            System.out.println("Book not found.");
        }
    }

    // Issue book
    static void issueBook() {

        System.out.println("\n------------- ISSUE BOOK ----------------");

        System.out.print("Enter Student ID: ");
        String studentId = sc.nextLine();

        Student student = findStudent(studentId);

        if (student == null) {

            System.out.println("\nNew Student");

            System.out.print("Enter Student Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Course: ");
            String course = sc.nextLine();

            System.out.print("Enter Contact: ");
            String contact = sc.nextLine();

            student = new Student(
                    studentId,
                    name,
                    course,
                    contact
            );

            students.add(student);

            FileManager.saveStudents(students);

        } else {

            System.out.println("\nStudent Found!");
            student.display();
        }

        System.out.print("\nEnter Book ID: ");
        String bookId = sc.nextLine();

        Book book = findBook(bookId);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        System.out.println("\nBook Information:");
        book.display();

        if (!book.isAvailable()) {
            System.out.println("\nSorry! This book is currently unavailable.");
            return;
        }

        for (LibRecord record : records) {

            if (record.getStudent().getId().equals(studentId)
                    && record.getBook().getId().equals(bookId)
                    && !record.isReturned()) {

                System.out.println(
                        "\nThis student already has this book issued."
                );

                return;
            }
        }

        System.out.print("\nEnter allowed days: ");
        int allowedDays = sc.nextInt();
        sc.nextLine();

        LibRecord record =
                new LibRecord(student, book, allowedDays);

        records.add(record);

        book.issueCopy();

        FileManager.saveBooks(books);
        FileManager.saveRecords(records);

        System.out.println("\n==========================================");
        System.out.println("       BOOK ISSUED SUCCESSFULLY");
        System.out.println("==========================================");
        System.out.println("Student       : " + student.getName());
        System.out.println("Book          : " + book.getTitle());
        System.out.println("Available     : " + book.getAvailableCopies());
        System.out.println("Issued        : " + book.getIssuedCopies());

        if (book.isAvailable()) {
            System.out.println("Status        : AVAILABLE");
        } else {
            System.out.println("Status        : UNAVAILABLE");
        }

        System.out.println("==========================================");
    }

    // Return book
    static void returnBook() {

        System.out.println("\n------------- RETURN BOOK ----------------");

        System.out.print("Enter Student ID: ");
        String studentId = sc.nextLine();

        System.out.print("Enter Book ID: ");
        String bookId = sc.nextLine();

        LibRecord record = findActiveRecord(studentId, bookId);

        if (record == null) {
            System.out.println("\nNo active issue record found.");
            return;
        }

        Book book = record.getBook();

        System.out.println("\nBook Found!");
        System.out.println("Book Name    : " + book.getTitle());
        System.out.println("Allowed Days : " + record.getAllowedDays());

        System.out.print("Enter actual days: ");
        sc.nextInt(); // consume actual days (unused, automatically uses today's date)
        sc.nextLine();

        record.returnBook(java.time.LocalDate.now());

        book.returnCopy();

        FileManager.saveBooks(books);
        FileManager.saveRecords(records);

        System.out.println("\n==========================================");
        System.out.println("       BOOK RETURNED SUCCESSFULLY");
        System.out.println("==========================================");
        System.out.println("Book             : " + book.getTitle());
        System.out.println("Delayed Days     : " + record.getDelayedDays());
        System.out.println("Fine Amount      : ₹" + record.getFine());
        System.out.println("Available Copies : " + book.getAvailableCopies());
        System.out.println("==========================================");

        
    }

    // Add new book
    static void addBook() {

        System.out.println("\n------------- ADD BOOK ----------------");

        System.out.print("Enter Book ID: ");
        String id = sc.nextLine();

        if (findBook(id) != null) {
            System.out.println("Book ID already exists.");
            return;
        }

        System.out.print("Enter Book Title: ");
        String title = sc.nextLine();

        System.out.print("Enter Author: ");
        String author = sc.nextLine();

        System.out.println("\nCategories:");
        System.out.println("1. General");
        System.out.println("2. Academic");
        System.out.println("3. Reference");
        System.out.println("4. Fiction");

        System.out.print("Enter category: ");
        int choice = sc.nextInt();
        sc.nextLine();

        String category;

        switch (choice) {

            case 1:
                category = "General";
                break;

            case 2:
                category = "Academic";
                break;

            case 3:
                category = "Reference";
                break;

            case 4:
                category = "Fiction";
                break;

            default:
                System.out.println("Invalid category.");
                return;
        }

        System.out.print("Enter number of copies: ");
        int copies = sc.nextInt();
        sc.nextLine();

        if (copies <= 0) {
            System.out.println("Copies must be greater than zero.");
            return;
        }

        Book book =
                new Book(id, title, author, category, copies);

        books.add(book);

        FileManager.saveBooks(books);

        System.out.println("\nBook added successfully!");
        book.display();
    }

    // Search student
    static void searchStudent() {

        System.out.println("\n------------- SEARCH STUDENT ----------------");

        System.out.print("Enter Student ID: ");
        String id = sc.nextLine();

        Student student = findStudent(id);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("\nStudent Found!");
        student.display();

        System.out.println("\nLibrary Records:");

        boolean found = false;

        for (LibRecord record : records) {

            if (record.getStudent().getId().equals(id)) {

                System.out.println("\nBook : " + record.getBook().getTitle());

                if (record.isReturned()) {
                    System.out.println("Status : Returned");
                    System.out.println("Fine   : ₹" + record.getFine());
                } else {
                    System.out.println("Status : Issued");
                }

                found = true;
            }
        }

        if (!found) {
            System.out.println("No book records found.");
        }
    }

    // View receipts
    static void viewReceipts() {
        System.out.println("\n------------- FINE RECEIPTS ----------------");
        boolean found = false;
        for (LibRecord record : records) {
            if (record.isReturned()) {
                record.displayReceipt();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No returned books or fine receipts.");
        }
    }

    // Library summary
    static void summary() {

        int issued = 0;
        int returned = 0;
        int overdue = 0;
        int totalCopies = 0;
        int availableCopies = 0;
        double totalFine = 0;

        for (Book book : books) {
            totalCopies += book.getTotalCopies();
            availableCopies += book.getAvailableCopies();
        }

        for (LibRecord record : records) {

            if (record.isReturned()) {

                returned++;
                totalFine += record.getFine();

                if (record.getDelayedDays() > 0) {
                    overdue++;
                }

            } else {
                issued++;
            }
        }

        System.out.println("\n==========================================");
        System.out.println("          MINDSPACE LIBRARY");
        System.out.println("              SUMMARY");
        System.out.println("==========================================");
        System.out.println("Total Books       : " + books.size());
        System.out.println("Total Copies      : " + totalCopies);
        System.out.println("Available Copies  : " + availableCopies);
        System.out.println("Issued Copies     : " +
                (totalCopies - availableCopies));
        System.out.println("Active Issues     : " + issued);
        System.out.println("Returned Books    : " + returned);
        System.out.println("Overdue Returns   : " + overdue);
        System.out.println("Total Fine        : ₹" + totalFine);
        System.out.println("==========================================");
    }

    // Find book by ID
    static Book findBook(String id) {

        for (Book book : books) {

            if (book.getId().equalsIgnoreCase(id)) {
                return book;
            }
        }

        return null;
    }

    // Find student
    static Student findStudent(String id) {

        for (Student student : students) {

            if (student.getId().equalsIgnoreCase(id)) {
                return student;
            }
        }

        return null;
    }

    // Find active record
    static LibRecord findActiveRecord(String studentId, String bookId) {

        for (LibRecord record : records) {

            if (record.getStudent().getId().equalsIgnoreCase(studentId)
                    && record.getBook().getId().equalsIgnoreCase(bookId)
                    && !record.isReturned()) {

                return record;
            }
        }

        return null;
    }

    public static void main(String[] args) {

        startLibrary();

        int choice;

        System.out.println("\n==========================================");
        System.out.println("       WELCOME TO MINDSPACE LIBRARY");
        System.out.println("        Fine Management System");
        System.out.println("==========================================");

        do {

            showMenu();

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    viewBooks();
                    break;

                case 2:
                    searchBook();
                    break;

                case 3:
                    issueBook();
                    break;

                case 4:
                    returnBook();
                    break;

                case 5:
                    addBook();
                    break;

                case 6:
                    searchStudent();
                    break;

                case 7:
                    viewReceipts();
                    break;

                case 8:
                    summary();
                    break;

                case 9:
                    System.out.println(
                            "\nThank you for using MindSpace Library!"
                    );
                    break;

                default:
                    System.out.println(
                            "\nInvalid choice. Please try again."
                    );
            }

        } while (choice != 9);

        sc.close();
    }
}