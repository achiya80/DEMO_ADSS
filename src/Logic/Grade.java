package Logic;

import java.util.Objects;

public class Grade {
    private double grade;
    private final String courseName;
    private final String studentId;

    public Grade(String studentId, String courseName, double grade) {
        this.grade = grade;
        this.courseName = courseName;
        this.studentId = studentId;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseName() {
        return courseName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grade grade1)) return false;
        return Double.compare(grade1.getGrade(), getGrade()) == 0 && Objects.equals(courseName, grade1.courseName) && Objects.equals(getStudentId(), grade1.getStudentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGrade(), courseName, getStudentId());
    }

    @Override
    public String toString() {
        return "Grade{" +
                "grade=" + grade +
                ", courseName='" + courseName + '\'' +
                ", studentId='" + studentId + '\'' +
                '}';
    }

    public boolean isSame(String studentId, String courseName) {
        return Objects.equals(this.studentId, studentId)
                && Objects.equals(this.courseName, courseName);
    }
}
