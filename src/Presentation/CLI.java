package Presentation;

import Logic.Grade;
import Logic.GradesManager;
import Logic.Student;
import Logic.StudentController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLI {

    private static boolean Exit = false;
    private static GradesManager gradesManager;

    private static final Map<String, Action> actionsMap = new HashMap<String, Action>(){
        {
            put("0", CLI::exit);
            put("1", CLI::addStudent);
            put("2", CLI::addGrade);
            put("3", CLI::updateGrade);
            put("4", CLI::getStudentAvg);
            put("5", CLI::getAllStudents);
            put("6", CLI::getStudentInfo);
            put("7", CLI::getCourseAVG);
        }
    };

    public static void main(String[] args){
        StudentController studentController = new StudentController();
        gradesManager = new GradesManager(studentController);
        String userInput = "";
        printMenu();
        while (!Exit){
            userInput = Parser.getStrInput();
            Parser.handleAction(actionsMap.get(userInput));
            printMenu();
        }
        System.out.println("System is shutting down...");
        System.exit(0);
    }

    private static void exit(){
        Exit = true;
    }

    private static void printMenu(){
        System.out.println("0: exit");
        System.out.println("1: add student");
        System.out.println("2: add grade");
        System.out.println("3: update grade");
        System.out.println("4: get student avg");
        System.out.println("5: get all students");
        System.out.println("6: get student info");
        System.out.println("7: get course avg");
    }


    private static void addStudent(){
        String id = Parser.getStrInput("insert id:");
        String firstName = Parser.getStrInput("insert first name:");
        String lastName = Parser.getStrInput("insert last name:");
        gradesManager.addStudent(id, firstName, lastName);
        System.out.println("added student");
    }

    private static void addGrade(){
        String id = Parser.getStrInput("insert id:");
        String courseName = Parser.getStrInput("insert course name:");
        int grade = Parser.getIntInput("insert grade:");
        gradesManager.addGrade(id, courseName, grade);
        System.out.println("added grade");
    }

    private static void updateGrade(){
        String id = Parser.getStrInput("insert id:");
        String courseName = Parser.getStrInput("insert course name:");
        int grade = Parser.getIntInput("insert new grade:");
        gradesManager.updateGrade(id, courseName, grade);
        System.out.println("update grade");
    }

    private static void getStudentAvg(){
        String id = Parser.getStrInput("insert id:");
        double avg = gradesManager.calculateAverageGrade(id);
        Student s = gradesManager.getStudent(id);
        String name = s.getFirstName() + " " + s.getLastName();
        System.out.printf("student %s avg is: %f%n", name, avg);
    }

    private static void getAllStudents(){
        List<Student> lst = gradesManager.getStudents();
        Parser.printList(lst, s -> String.format("(id: %s, name: %s %s)", s.getId(),s.getFirstName(), s.getLastName()));
    }

    private static void getStudentInfo(){
        String id = Parser.getStrInput("insert id:");
        Student s = gradesManager.getStudent(id);
        List<Grade> lst = gradesManager.getStudentGrades(id);
        double avg = gradesManager.calculateAverageGrade(id);
        System.out.println(s.toString() + "\ngrades = {\n"+ Parser.printList(lst) + "\n}\n AVG = " + avg + "\n");
    }

    private static void getCourseAVG(){
        String courseName = Parser.getStrInput("insert course name:");
        double avg = gradesManager.calculateCourseAverageGrade(courseName);
        System.out.printf("course %s avg is: %f%n", courseName, avg);
    }




}
