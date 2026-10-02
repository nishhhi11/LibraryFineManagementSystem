public class Book {
    private String id;
    private String title;
    private String author;
    private String category;
    private int totalCopies;
    private int availableCopies;

    // Store book details
    public Book(String id, String title, String author, String category, int totalCopies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public int getIssuedCopies() { return totalCopies - availableCopies; }

    // Check availability
    public boolean isAvailable() {
        return availableCopies > 0;
    }

    // Issue one copy
    public void issueCopy() {
        if (availableCopies > 0) {
            availableCopies--;
        }
    }

    // Return one copy
    public void returnCopy() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }

    // Display book
    public void display() {
        String status = availableCopies > 0 ? "AVAILABLE" : "UNAVAILABLE";
        System.out.println("------------------------------------------");
        System.out.println("Book ID          : " + id);
        System.out.println("Title            : " + title);
        System.out.println("Author           : " + author);
        System.out.println("Category         : " + category);
        System.out.println("Total Copies     : " + totalCopies);
        System.out.println("Available Copies : " + availableCopies);
        System.out.println("Issued Copies    : " + getIssuedCopies());
        System.out.println("Status           : " + status);
    }
}