# MINDSPACE
## Library Fine Management System

MindSpace is a Java-based Library Fine Management System designed to manage books, students, book issue and return operations, overdue days, and library fines.

The project includes both a **console-based implementation using Scanner** and a **Java Swing graphical interface**. Data is stored locally using text files, so the application does not require a database.

---

##  Project Overview

The system provides a simple library workflow where users can:

- Manage student information
- Manage book records
- Search and filter books
- Issue books
- Return books
- Track book availability
- Calculate delayed/overdue days
- Calculate category-based fines
- View fine records
- Generate fine receipts
- Store data using local text files

The project demonstrates important Java programming concepts including OOP, collections, file handling, date handling, conditional statements, switch-case, loops, and event handling.

---

##  Objectives

- Maintain student and book information
- Display and search the library collection
- Issue books to students
- Return issued books
- Calculate actual and delayed days
- Calculate fines based on delayed days and book category
- Maintain multiple library transaction records
- Store and load data using local files
- Provide a Java Swing graphical interface
- Demonstrate fundamental Java programming concepts

---

##  Technologies Used

| Technology | Purpose |
|---|---|
| Java | Core application development |
| Java Swing | Graphical user interface |
| Scanner | Console input |
| ArrayList | Storing students, books and records |
| LocalDate | Date management |
| ChronoUnit | Delayed-day calculation |
| File I/O | Local data persistence |
| Git & GitHub | Version control |

---

##  Java Concepts Used

The project demonstrates:

- Classes and Objects
- Encapsulation
- Constructors
- Methods
- Method Overloading
- `if-else`
- `switch-case`
- Loops
- Arithmetic Operators
- Comparison Operators
- Logical Operators
- ArrayList
- LocalDate
- ChronoUnit
- File Handling
- Java Swing
- ActionListener
- Event Handling
- Basic GUI Layout
- Custom Drawing

---

##  System Architecture

The project is divided into multiple components:

```text
                    MINDSPACE
                        │
             ┌──────────┴──────────┐
             │                     │
        Console App            Swing GUI
        Main.java              GUI.java
             │                     │
             └──────────┬──────────┘
                        │
                  Core Classes
             ┌──────────┼──────────┐
             │          │          │
          Book.java  Student.java  LibRecord.java
             │          │          │
             └──────────┼──────────┘
                        │
                  FileManager.java
                        │
             ┌──────────┼──────────┐
             │          │          │
         books.txt  students.txt records.txt
