public class Student {
    private String id;
    private String name;
    private String course;
    private String contact;

    // Store student details
    public Student(String id, String name, String course, String contact) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.contact = contact;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCourse() { return course; }
    public String getContact() { return contact; }

    // Display student
    public void display() {
        System.out.println("Student ID : " + id);
        System.out.println("Name       : " + name);
        System.out.println("Course     : " + course);
        System.out.println("Contact    : " + contact);
    }
}