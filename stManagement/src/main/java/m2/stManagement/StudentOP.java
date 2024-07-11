package m2.stManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentOP {
    private List<Student> students = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private Connection connection;

    public StudentOP() {
        try {
            String url = "jdbc:mysql://localhost:3306/studentmanagement";
            String username = "root"; 
            String password = "Meet@2005"; 
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM students";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int rollno = resultSet.getInt("rollno");
                String enrollmentno = resultSet.getString("enrollmentno");
                String studentname = resultSet.getString("studentname");
                String division = resultSet.getString("division");
                String emailid = resultSet.getString("emailid");

                Student student = new Student(rollno, enrollmentno, studentname, division, emailid);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    public void displayMenu() {
        System.out.println("1. Add Student");
        System.out.println("2. View Students");
        System.out.println("3. Update Student");
        System.out.println("4. Delete Student");
        System.out.println("5. Search Student");
        System.out.println("6. Exit");
    }

    public void addStudent() {
        System.out.print("Enter Roll No: ");
        int rollno = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter Enrollment No: ");
        String enrollmentno = scanner.nextLine();
        System.out.print("Enter Student Name: ");
        String studentname = scanner.nextLine();
        System.out.print("Enter Division: ");
        String division = scanner.nextLine();
        System.out.print("Enter Email ID: ");
        String emailid = scanner.nextLine();

        try (Statement statement = connection.createStatement()) {
            String query = String.format("INSERT INTO students (rollno, enrollmentno, studentname, division, emailid) VALUES (%d, '%s', '%s', '%s', '%s')", rollno, enrollmentno, studentname, division, emailid);
            statement.executeUpdate(query);
            System.out.println("Student added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewStudents() {
        List<Student> students = getAllStudents();
        System.out.printf("%-10s %-20s %-30s %-20s %-30s\n", "Roll No", "Enrollment No", "Student Name", "Division", "Email ID");
        System.out.println("-----------------------------------------------------------------------------------------------");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void updateStudent() {
        System.out.print("Enter Enrollment No of the student to update: ");
        String enrollmentno = scanner.nextLine();

        System.out.print("Enter new Roll No: ");
        int rollno = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter new Student Name: ");
        String studentname = scanner.nextLine();
        System.out.print("Enter new Division: ");
        String division = scanner.nextLine();
        System.out.print("Enter new Email ID: ");
        String emailid = scanner.nextLine();

        try (PreparedStatement statement = connection.prepareStatement("UPDATE students SET rollno = ?, studentname = ?, division = ?, emailid = ? WHERE enrollmentno = ?")) {
            statement.setInt(1, rollno);
            statement.setString(2, studentname);
            statement.setString(3, division);
            statement.setString(4, emailid);
            statement.setString(5, enrollmentno);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Student updated successfully.");
            } else {
                System.out.println("Student with Enrollment No " + enrollmentno + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent() {
        System.out.print("Enter Enrollment No of the student to delete: ");
        String enrollmentno = scanner.nextLine();

        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM students WHERE enrollmentno = ?")) {
            statement.setString(1, enrollmentno);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Student deleted successfully.");
            } else {
                System.out.println("Student with Enrollment No " + enrollmentno + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchStudent() {
        System.out.print("Enter Enrollment No of the student to search: ");
        String enrollmentno = scanner.nextLine();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM students WHERE enrollmentno = ?")) {
            statement.setString(1, enrollmentno);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int rollno = resultSet.getInt("rollno");
                String studentname = resultSet.getString("studentname");
                String division = resultSet.getString("division");
                String emailid = resultSet.getString("emailid");
                System.out.printf("%-10s %-20s %-30s %-20s %-30s\n", "Roll No", "Enrollment No", "Student Name", "Division", "Email ID");
                System.out.println("-----------------------------------------------------------------------------------------------");
                System.out.printf("%-10d %-20s %-30s %-20s %-30s\n", rollno, enrollmentno, studentname, division, emailid);
            } else {
                System.out.println("Student with Enrollment No " + enrollmentno + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        while (true) {
            displayMenu();
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewStudents();
                    break;
                case 3:
                    updateStudent();
                    break;
                case 4:
                    deleteStudent();
                    break;
                case 5:
                    searchStudent();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
