public class Record {

    private Student student;
    private Book book;
    private int allowedDays;
    private int actualDays;
    private int delayedDays;
    private double fine;
    private boolean returned;

    // Create issue record
    public Record(Student student, Book book, int allowedDays) {

        this.student = student;
        this.book = book;
        this.allowedDays = allowedDays;
        this.actualDays = 0;
        this.delayedDays = 0;
        this.fine = 0;
        this.returned = false;
    }

    // Return book
    public void returnBook(int actualDays) {

        this.actualDays = actualDays;

        delayedDays = actualDays - allowedDays;

        if (delayedDays < 0) {
            delayedDays = 0;
        }

        calculateFine();

        returned = true;
    }

    // Calculate fine
    private void calculateFine() {

        double finePerDay;

        switch (book.getCategory()) {

            case "General":
                finePerDay = 2;
                break;

            case "Academic":
                finePerDay = 3;
                break;

            case "Reference":
                finePerDay = 5;
                break;

            case "Fiction":
                finePerDay = 2;
                break;

            default:
                finePerDay = 0;
        }

        if (delayedDays == 0) {
            fine = 0;
        } else if (delayedDays <= 5) {
            fine = delayedDays * finePerDay;
        } else {
            fine = delayedDays * finePerDay + 10;
        }
    }

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