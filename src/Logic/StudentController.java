package Logic;

import DataAccess.DAOs.GradeDAO;
import DataAccess.DAOs.StudentDAO;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class StudentController {
    private List<Grade> gradeList;
    private List<Student> studentList;

    private final GradeDAO gradeDAO;

    private final StudentDAO studentDAO;

    public StudentController() {
        this.studentList = new LinkedList<>();
        this.gradeList = new LinkedList<>();
        this.gradeDAO = new GradeDAO();
        this.studentDAO = new StudentDAO();
    }

    public List<Student> getStudentList(){
        studentList = studentDAO.selectAllRowsToBusiness();
        return studentList;
    }

    public List<Grade> getGradeList() {
        gradeList = gradeDAO.selectAllRowsToBusiness();
        return this.gradeList;
    }

    private boolean containsGrade(String studentId, String courseName) {
        return getGradeList().stream().
                anyMatch(grade -> grade.isSame(studentId, courseName));
    }
    private Grade getGrade(String studentId, String courseName) {
        return getGradeList().stream().
                filter(grade -> grade.isSame(studentId, courseName)).toList().get(0);
    }
    public void addGrade(String studentId, String courseName, int grade) {
        if (containsGrade(studentId, courseName))
            throw new RuntimeException("this grade is already in the system " + printGrade(studentId, courseName));
        Grade toAdd = new Grade(studentId, courseName, grade);
        gradeDAO.insert(toAdd);
    }

    public Student getStudent(String id){
        return getStudentList().stream().
                filter(s -> s.getId().equals(id)).findFirst().orElse(new Student("-1","no such student","no such student"));
    }

    private String printGrade(String studentId, String courseName) {
        return getGrade(studentId, courseName).toString();
    }

    public double calculateAverageGrade(String studentId) {
        AtomicReference<Double> i = new AtomicReference<>(0.0);
        List<Grade> lst = getStudentsGrades(studentId);
        lst.forEach(g -> i.set(i.get() + g.getGrade()));
        if(lst.size() == 0) System.out.println("no grades available");
        return i.get()/lst.size();
    }

    public List<Grade> getStudentsGradesInCourse(String studentId, String courseName) {
        return getGradeList().stream().
                filter(g -> g.getStudentId().equals(studentId)
                        && g.getCourseName().equals(courseName)).toList();
    }
    public List<Grade> getStudentsGrades(String studentId) {
        return getGradeList().stream().filter(g -> g.getStudentId().equals(studentId)).toList();
    }
    public void addStudent(String id, String fn, String ln){
        Student toAdd = new Student(id, fn, ln);
        studentDAO.insert(toAdd);
    }

    public double calculateCourseAverageGrade(String courseName) {
        AtomicReference<Double> i = new AtomicReference<>(0.0);
        List<Grade> lst = getCourseGrades(courseName);
        lst.forEach(g -> i.set(i.get() + g.getGrade()));
        if(lst.size() == 0) System.out.println("no grades available for the course: "+courseName);
        return i.get()/lst.size();
    }

    private List<Grade> getCourseGrades(String courseName) {
        return getGradeList().stream().
                filter(g -> g.getCourseName().equals(courseName)).toList();
    }
}
