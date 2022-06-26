package Logic;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class StudentController {
    private final List<Grade> gradeList;
    private final List<Student> studentList;

    public StudentController() {
        this.studentList = new LinkedList<>();
        this.gradeList = new LinkedList<>();
    }

    public List<Student> getStudentList(){
        return studentList;
    }

    public List<Grade> getGradeList() {
        return this.gradeList;
    }

    private boolean containsGrade(String studentId, String courseName) {
        return getGradeList().stream().anyMatch(grade -> grade.isSame(studentId, courseName));
    }
    private Grade getGrade(String studentId, String courseName) {
        return getGradeList().stream().filter(grade -> grade.isSame(studentId, courseName)).toList().get(0);
    }
    public void addGrade(String studentId, String courseName, int grade) {
        if (containsGrade(studentId, courseName))
            throw new RuntimeException("this grade is already in the system " + printGrade(studentId, courseName));
        getGradeList().add(new Grade(studentId, courseName, grade));
    }

    public Student getStudent(String id){
        return getStudentList().stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    private String printGrade(String studentId, String courseName) {
        return getGrade(studentId, courseName).toString();
    }

    public double calculateAverageGrade(String studentId) {
        AtomicReference<Double> i = new AtomicReference<>(0.0);
        List<Grade> lst = getStudentsGrades(studentId);
        lst.forEach(g -> i.set(i.get() + g.getGrade()));
        if(lst.size() == 0) throw new RuntimeException("no grades available");
        return i.get()/lst.size();
    }

    public List<Grade> getStudentsGrades(String studentId) {
        return getGradeList().stream().filter(g -> g.getStudentId().equals(studentId)).toList();
    }

    public void addStudent(String id, String fn, String ln){
        Student toAdd = new Student(id, fn, ln);
        getStudentList().add(toAdd);
    }

    public double calculateCourseAverageGrade(String courseName) {
        AtomicReference<Double> i = new AtomicReference<>(0.0);
        List<Grade> lst = getCourseGrades(courseName);
        lst.forEach(g -> i.set(i.get() + g.getGrade()));
        if(lst.size() == 0)throw new RuntimeException("no grades available for the course: "+courseName);
        return i.get()/lst.size();
    }

    private List<Grade> getCourseGrades(String courseName) {
        return getGradeList().stream().filter(g -> g.getCourseName().equals(courseName)).toList();
    }
}
