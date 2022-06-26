package DataAccess.DTOs;
import DataAccess.PrimaryKeys.PK;
import java.lang.reflect.Field;
    public class StudentDTO extends DTO<PK>{
        private String id;
private String firstName;
private String lastName;
public StudentDTO(String id, String firstName, String lastName){
		super(new PK(getFields(), id));
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
}
  public static Field[] getFields(){ return getFields(new String[]{"id"}, StudentDTO.class);}
 public static PK getPK(String id){ return new PK(getFields(), id);}

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
	@Override
	public Object[] getValues() {return new Object[]{id, firstName, lastName};}
}


