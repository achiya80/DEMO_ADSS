package DataAccess.DTOs;
import DataAccess.PrimaryKeys.PK;
import java.lang.reflect.Field;
    public class GradeDTO extends DTO<PK>{
        private String studentId;
private String courseName;
private double grade;
public GradeDTO(String studentId, String courseName, double grade){
		super(new PK(getFields(), studentId, courseName));
		this.studentId = studentId;
		this.courseName = courseName;
		this.grade = grade;
}
  public static Field[] getFields(){ return getFields(new String[]{"studentId", "courseName"}, GradeDTO.class);}
 public static PK getPK(String studentId, String courseName){ return new PK(getFields(), studentId,  courseName);}

    public String getStudentId(){
        return studentId;
    }
    public void setStudentId(String studentId){
        this.studentId = studentId;
    }
    public String getCourseName(){
        return courseName;
    }
    public void setCourseName(String courseName){
        this.courseName = courseName;
    }
    public double getGrade(){
        return grade;
    }
    public void setGrade(double grade){
        this.grade = grade;
    }
	@Override
	public Object[] getValues() {return new Object[]{studentId, courseName, grade};}
}


