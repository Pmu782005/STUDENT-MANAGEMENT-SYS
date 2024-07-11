package m2.stManagement;

public class Student {
    private int rollno;
    private String enrollmentno;
    private String studentname;
    private String division;
    private String emailid;

    public Student(int rollno, String enrollmentno, String studentname, String division, String emailid) {
        this.rollno = rollno;
        this.enrollmentno = enrollmentno;
        this.studentname = studentname;
        this.division = division;
        this.emailid = emailid;
    }

    public int getRollno() {
        return rollno;
    }

    public String getEnrollmentno() {
        return enrollmentno;
    }

    public String getStudentname() {
        return studentname;
    }

    public String getDivision() {
        return division;
    }

    public String getEmailid() {
        return emailid;
    }

    @Override
    public String toString() {
        return String.format("%-10d %-20s %-30s %-20s %-30s", rollno, enrollmentno, studentname, division, emailid);
    }
}
