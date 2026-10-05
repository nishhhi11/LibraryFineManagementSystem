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

    public static void saveRecords(ArrayList<LibRecord> records) {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(RECORD_FILE))) {
            for (LibRecord record : records) {
                writer.write(
                        record.getStudent().getId() + "|" +
                                record.getBook().getId() + "|" +
                                record.getAllowedDays() + "|" +
                                record.getActualDays() + "|" +
                                record.getDelayedDays() + "|" +
                                record.getFine() + "|" +
                                record.isReturned() + "|" +
                                (record.getIssueDate() != null ? record.getIssueDate().toString() : java.time.LocalDate.now().toString()) + "|" +
                                record.getFineStatus()
                );
                writer.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error saving records.");
        }
    }

    public static ArrayList<LibRecord> loadRecords(ArrayList<Student> students, ArrayList<Book> books) {
        ArrayList<LibRecord> records = new ArrayList<>();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(RECORD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length >= 7) {
                    Student student = findStudent(students, data[0]);
                    Book book = findBook(books, data[1]);
                    if (student != null && book != null) {
                        int allowedDays = Integer.parseInt(data[2]);
                        int actualDays = Integer.parseInt(data[3]);
                        boolean returned = Boolean.parseBoolean(data[6]);
                        java.time.LocalDate issueDate = java.time.LocalDate.now();
                        if (data.length >= 8) {
                            issueDate = java.time.LocalDate.parse(data[7]);
                        }
                        LibRecord record = new LibRecord(student, book, allowedDays, issueDate);
                        if (returned) {
                            String fStatus = data.length >= 9 ? data[8] : (Double.parseDouble(data[5]) > 0 ? "UNPAID" : "NONE");
                            record.setReturnData(actualDays, Integer.parseInt(data[4]), Double.parseDouble(data[5]), fStatus);
                        }
                        records.add(record);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading records.");
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
