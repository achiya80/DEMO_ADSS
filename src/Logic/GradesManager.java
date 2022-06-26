package Logic;

import DataAccess.DAOs.GradeDAO;
import DataAccess.DAOs.StudentDAO;

import java.util.List;

public class GradesManager {

    private final StudentController studentController;

    public GradesManager(StudentController studentController){
        this.studentController = studentController;
    }
    public void addGrade(String studentId, String courseName, int grade){
        studentController.addGrade( studentId,  courseName,  grade);
    }


    public double calculateCourseAverageGrade(String courseName){
        return studentController.calculateCourseAverageGrade(courseName);
    }

    public double calculateAverageGrade(String studentId){
        return studentController.calculateAverageGrade(studentId);
    }

    public List<Grade> getStudentGrades(String id){
        return studentController.getStudentsGrades(id);
    }

    public Student getStudent(String id){
        return studentController.getStudent(id);
    }
    public List<Student> getStudents(){
        return studentController.getStudentList();
    }

    public void updateGrade(String id, String courseName, int newGrade){
        getStudentGrades(id).stream().filter(g -> g.getCourseName().equals(courseName)).findFirst().orElse(null).setGrade(newGrade);
    }

    public void addStudent(String id, String fn, String ln){
        studentController.addStudent(id, fn, ln);
    }
}
