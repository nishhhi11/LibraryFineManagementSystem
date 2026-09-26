import java.io.*;
import java.util.ArrayList;

public class FileManager {

    static final String DATA_FOLDER = "data";
    static final String BOOK_FILE = DATA_FOLDER + "/books.txt";
    static final String STUDENT_FILE = DATA_FOLDER + "/students.txt";
    static final String RECORD_FILE = DATA_FOLDER + "/records.txt";

    public static void setupFiles() {

        File folder = new File(DATA_FOLDER);

        if (!folder.exists()) {
            folder.mkdir();
        }

        try {

            File books = new File(BOOK_FILE);
            File students = new File(STUDENT_FILE);
            File records = new File(RECORD_FILE);

            if (!books.exists()) {
                books.createNewFile();
            }

            if (!students.exists()) {
                students.createNewFile();
            }

            if (!records.exists()) {
                records.createNewFile();
            }

        } catch (IOException e) {

            System.out.println(
                    "Error creating data files."
            );
        }
    }

    // Add the complete MindSpace collection
    public static void addDefaultBooks() {

        File file =
                new File(BOOK_FILE);

        try {

            boolean migrateOldData = false;

            if (file.length() > 0) {

                BufferedReader reader =
                        new BufferedReader(
                                new FileReader(file)
                        );

                ArrayList<String> ids =
                        new ArrayList<>();

                String line;

                while ((line = reader.readLine()) != null) {

                    String[] data =
                            line.split("\\|");

                    if (data.length > 0) {
                        ids.add(data[0]);
                    }
                }

                reader.close();

                // Replace the old 5-book sample data
                if (ids.contains("B101")
                        || ids.contains("B102")
                        || ids.contains("B103")
                        || ids.contains("B104")
                        || ids.contains("B105")) {

                    migrateOldData = true;
                }

            }

            if (file.length() > 0
                    && !migrateOldData) {

                return;
            }

            try (BufferedWriter writer =
                         new BufferedWriter(
                                 new FileWriter(file)
                         )) {

                addBook(
                        writer,
                        "A101",
                        "Java: The Complete Reference",
                        "Herbert Schildt",
                        "Academic",
                        5
                );

                addBook(
                        writer,
                        "A102",
                        "Database Management Systems",
                        "Raghu Ramakrishnan, Johannes Gehrke",
                        "Academic",
                        4
                );

                addBook(
                        writer,
                        "A103",
                        "Computer Networks",
                        "Andrew S. Tanenbaum, David J. Wetherall",
                        "Academic",
                        4
                );

                addBook(
                        writer,
                        "A104",
                        "Operating System Concepts",
                        "Abraham Silberschatz, Peter B. Galvin, Greg Gagne",
                        "Academic",
                        4
                );

                addBook(
                        writer,
                        "A105",
                        "Fundamentals of Data Structures",
                        "Ellis Horowitz, Sartaj Sahni",
                        "Academic",
                        3
                );

                addBook(
                        writer,
                        "A106",
                        "Python Programming",
                        "Vamsi Kurama",
                        "Academic",
                        4
                );

                addBook(
                        writer,
                        "A107",
                        "Software Engineering",
                        "Roger S. Pressman",
                        "Academic",
                        3
                );

                addBook(
                        writer,
                        "A108",
                        "Database System Concepts",
                        "Abraham Silberschatz, Henry F. Korth, S. Sudarshan",
                        "Academic",
                        3
                );

                addBook(
                        writer,
                        "A109",
                        "Artificial Intelligence: A Modern Approach",
                        "Stuart Russell, Peter Norvig",
                        "Academic",
                        3
                );

                addBook(
                        writer,
                        "A110",
                        "Introduction to Algorithms",
                        "Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest, Clifford Stein",
                        "Academic",
                        3
                );

                addBook(writer, "R201", "Clean Code",
                        "Robert C. Martin", "Reference", 3);

                addBook(writer, "R202", "Design Patterns",
                        "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides",
                        "Reference", 2);

                addBook(writer, "R203", "Cryptography and Network Security",
                        "William Stallings", "Reference", 3);

                addBook(writer, "R204", "Fundamentals of Database Systems",
                        "Ramez Elmasri, Shamkant B. Navathe",
                        "Reference", 3);

                addBook(writer, "R205", "Computer Security",
                        "William Stallings, Lawrie Brown",
                        "Reference", 2);

                addBook(writer, "R206", "Data Structures and Algorithms in Java",
                        "Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser",
                        "Reference", 2);

                addBook(writer, "R207", "Computer Architecture",
                        "Morris Mano", "Reference", 3);

                addBook(writer, "R208", "Modern Operating Systems",
                        "Andrew S. Tanenbaum", "Reference", 2);

                addBook(writer, "R209", "Computer Organization",
                        "Carl Hamacher, Safwat Zaky, Steven Vranesic",
                        "Reference", 2);

                addBook(writer, "R210", "The Pragmatic Programmer",
                        "Andrew Hunt, David Thomas",
                        "Reference", 3);

                addBook(writer, "G301", "Atomic Habits",
                        "James Clear", "General", 3);

                addBook(writer, "G302",
                        "The 7 Habits of Highly Effective People",
                        "Stephen R. Covey", "General", 2);

                addBook(writer, "G303", "Ikigai",
                        "Hector Garcia, Francesc Miralles",
                        "General", 2);

                addBook(writer, "G304",
                        "How to Win Friends and Influence People",
                        "Dale Carnegie", "General", 3);

                addBook(writer, "G305", "Think and Grow Rich",
                        "Napoleon Hill", "General", 2);

                addBook(writer, "G306", "The Power of Now",
                        "Eckhart Tolle", "General", 2);

                addBook(writer, "G307", "Rich Dad Poor Dad",
                        "Robert T. Kiyosaki", "General", 3);

                addBook(writer, "G308", "Deep Work",
                        "Cal Newport", "General", 3);

                addBook(writer, "G309", "The Psychology of Money",
                        "Morgan Housel", "General", 3);

                addBook(writer, "G310", "The Alchemist",
                        "Paulo Coelho", "General", 3);

                addBook(writer, "F401",
                        "Harry Potter and the Philosophers Stone",
                        "J.K. Rowling", "Fiction", 4);

                addBook(writer, "F402", "The Hobbit",
                        "J.R.R. Tolkien", "Fiction", 3);

                addBook(writer, "F403", "1984",
                        "George Orwell", "Fiction", 3);

                addBook(writer, "F404", "The Great Gatsby",
                        "F. Scott Fitzgerald", "Fiction", 2);

                addBook(writer, "F405", "To Kill a Mockingbird",
                        "Harper Lee", "Fiction", 3);

                addBook(writer, "F406", "Pride and Prejudice",
                        "Jane Austen", "Fiction", 3);

                addBook(writer, "F407", "The Kite Runner",
                        "Khaled Hosseini", "Fiction", 3);

                addBook(writer, "F408", "The Book Thief",
                        "Markus Zusak", "Fiction", 2);

                addBook(writer, "F409", "The Fault in Our Stars",
                        "John Green", "Fiction", 3);

                addBook(writer, "F410", "The Hunger Games",
                        "Suzanne Collins", "Fiction", 3);

            }

        } catch (IOException e) {

            System.out.println(
                    "Error adding default books."
            );
        }
    }

    static void addBook(
            BufferedWriter writer,
            String id,
            String title,
            String author,
            String category,
            int copies)
            throws IOException {

        writer.write(
                id + "|" +
                        title + "|" +
                        author + "|" +
                        category + "|" +
                        copies + "|" +
                        copies
        );

        writer.newLine();
    }

    public static ArrayList<Book> loadBooks() {

        ArrayList<Book> books =
                new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(BOOK_FILE)
                     )) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data =
                        line.split("\\|");

                if (data.length >= 6) {

                    Book book =
                            new Book(
                                    data[0],
                                    data[1],
                                    data[2],
                                    data[3],
                                    Integer.parseInt(data[4])
                            );

                    int available =
                            Integer.parseInt(data[5]);

                    while (book.getAvailableCopies()
                            > available) {

                        book.issueCopy();
                    }

                    books.add(book);
                }
            }

        } catch (IOException
                 | NumberFormatException e) {

            System.out.println(
                    "Error loading books."
            );
        }

        return books;
    }

    public static void saveBooks(
            ArrayList<Book> books) {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(BOOK_FILE)
                     )) {

            for (Book book : books) {

                writer.write(
                        book.getId() + "|" +
                                book.getTitle() + "|" +
                                book.getAuthor() + "|" +
                                book.getCategory() + "|" +
                                book.getTotalCopies() + "|" +
                                book.getAvailableCopies()
                );

                writer.newLine();
            }

        } catch (IOException e) {

            System.out.println(
                    "Error saving books."
            );
        }
    }

    public static ArrayList<Student> loadStudents() {

        ArrayList<Student> students =
                new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(STUDENT_FILE)
                     )) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data =
                        line.split("\\|");

                if (data.length >= 4) {

                    students.add(
                            new Student(
                                    data[0],
                                    data[1],
                                    data[2],
                                    data[3]
                            )
                    );
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Error loading students."
            );
        }

        return students;
    }

    public static void saveStudents(
            ArrayList<Student> students) {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(STUDENT_FILE)
                     )) {

            for (Student student : students) {

                writer.write(
                        student.getId() + "|" +
                                student.getName() + "|" +
                                student.getCourse() + "|" +
                                student.getContact()
                );

                writer.newLine();
            }

        } catch (IOException e) {

            System.out.println(
                    "Error saving students."
            );
        }
    }

    public static void saveRecords(
            ArrayList<LibRecord> records) {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(RECORD_FILE)
                     )) {

            for (LibRecord record : records) {

                writer.write(
                        record.getStudent().getId() + "|" +
                                record.getBook().getId() + "|" +
                                record.getAllowedDays() + "|" +
                                record.getActualDays() + "|" +
                                record.getDelayedDays() + "|" +
                                record.getFine() + "|" +
                                record.isReturned()
                );

                writer.newLine();
            }

        } catch (IOException e) {

            System.out.println(
                    "Error saving records."
            );
        }
    }

    public static ArrayList<LibRecord> loadRecords(
            ArrayList<Student> students,
            ArrayList<Book> books) {

        ArrayList<LibRecord> records =
                new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(RECORD_FILE)
                     )) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data =
                        line.split("\\|");

                if (data.length >= 7) {

                    Student student =
                            findStudent(
                                    students,
                                    data[0]
                            );

                    Book book =
                            findBook(
                                    books,
                                    data[1]
                            );

                    if (student != null
                            && book != null) {

                        int allowedDays =
                                Integer.parseInt(data[2]);

                        int actualDays =
                                Integer.parseInt(data[3]);

                        boolean returned =
                                Boolean.parseBoolean(data[6]);

                        LibRecord record =
                                new LibRecord(
                                        student,
                                        book,
                                        allowedDays
                                );

                        if (returned) {

                            record.returnBook(
                                    actualDays
                            );

                        }

                        records.add(record);
                    }
                }
            }

        } catch (IOException
                 | NumberFormatException e) {

            System.out.println(
                    "Error loading records."
            );
        }

        return records;
    }

    private static Student findStudent(
            ArrayList<Student> students,
            String id) {

        for (Student student : students) {

            if (student.getId().equals(id)) {
                return student;
            }
        }

        return null;
    }

    private static Book findBook(
            ArrayList<Book> books,
            String id) {

        for (Book book : books) {

            if (book.getId().equals(id)) {
                return book;
            }
        }

        return null;
    }
}
