package DataAccess.DAOs;
import DataAccess.DTOs.StudentDTO;
import DataAccess.IdentityMap.IM;
import DataAccess.PrimaryKeys.PK;
import Logic.Student;
import java.util.List;


public class StudentDAO extends DAO<PK, StudentDTO, Student>{
    public StudentDAO(){
        super(StudentDTO.class, IM.getInstance().getIdentityMap(Student.class));
    }

    @Override
    protected Student convertDtoToBusiness(StudentDTO dto) {
        return new Student(dto.getId(), dto.getFirstName(), dto.getLastName());
    }

    @Override
    protected StudentDTO convertBusinessToDto(Student business) {
        return new StudentDTO(business.getId(), business.getFirstName(), business.getLastName());
    }

    @Override
    protected StudentDTO createDTO(List<Object> listFields) { 
        return new StudentDTO((String) listFields.get(0), (String) listFields.get(1), (String) listFields.get(2));
    }

}