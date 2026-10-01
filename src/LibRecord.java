import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LibRecord {
    private LocalDate issueDate;


    private Student student;
    private Book book;
    private int allowedDays;
    private int actualDays;
    private int delayedDays;
    private double fine;
    private boolean returned;
    private String fineStatus;

    // Create issue record
    public LibRecord(Student student, Book book, int allowedDays, LocalDate issueDate) {
        this.student = student;
        this.book = book;
        this.allowedDays = allowedDays;
        this.issueDate = issueDate;
        this.actualDays = 0;
        this.delayedDays = 0;
        this.fine = 0;
        this.returned = false;
        this.fineStatus = "NONE";
    }

    public LibRecord(Student student, Book book, int allowedDays) {
        this(student, book, allowedDays, LocalDate.now());
    }

    // Return book
    public void returnBook(LocalDate returnDate) {
        this.actualDays = (int) ChronoUnit.DAYS.between(issueDate, returnDate);
        if (this.actualDays < 0) this.actualDays = 0;
        
        delayedDays = actualDays - allowedDays;
        if (delayedDays < 0) delayedDays = 0;

        fine = calculateFineAmount(delayedDays);
        returned = true;
        fineStatus = fine > 0 ? "UNPAID" : "NONE";
    }
    
        public void setReturnData(int actualDays, int delayedDays, double fine, String fineStatus) {
        this.actualDays = actualDays;
        this.delayedDays = delayedDays;
        this.fine = fine;
        this.returned = true;
        this.fineStatus = fineStatus;
    }

    // Calculate fine
    public static double calculateFineAmount(int delayedDays) {
        if (delayedDays <= 0) return 0.0;
        if (delayedDays <= 7) return delayedDays * 5.0;
        if (delayedDays <= 14) return (7 * 5.0) + ((delayedDays - 7) * 10.0);
        return (7 * 5.0) + (7 * 10.0) + ((delayedDays - 14) * 20.0);
    }
    
    public LocalDate getIssueDate() { return issueDate; }
    public Student getStudent() {
        return student;
    }

    public Book getBook() {
        return book;
    }

    public int getAllowedDays() {
        return allowedDays;
    }

    public int getActualDays() {
        return actualDays;
    }

    public int getDelayedDays() {
        return delayedDays;
    }

    public double getFine() {
        return fine;
    }

    public boolean isReturned() {
        return returned;
    }
    
    public String getFineStatus() { return fineStatus; }
    public void setFineStatus(String status) { this.fineStatus = status; }

    // Display receipt
    public void displayReceipt() {

        System.out.println("\n==========================================");
        System.out.println("              FINE RECEIPT");
        System.out.println("==========================================");

        System.out.println("Student ID   : " + student.getId());
        System.out.println("Student Name : " + student.getName());
        System.out.println("Course       : " + student.getCourse());

        System.out.println("------------------------------------------");

        System.out.println("Book ID      : " + book.getId());
        System.out.println("Book Name    : " + book.getTitle());
        System.out.println("Author       : " + book.getAuthor());
        System.out.println("Category     : " + book.getCategory());

        System.out.println("------------------------------------------");

        System.out.println("Allowed Days : " + allowedDays);
        System.out.println("Actual Days  : " + actualDays);
        System.out.println("Delayed Days : " + delayedDays);
        System.out.println("Fine Amount  : ₹" + fine);

        System.out.println("------------------------------------------");
        System.out.println("Status       : " + (returned ? "RETURNED" : "ISSUED"));
        System.out.println("==========================================");
    }
}