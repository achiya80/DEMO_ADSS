package DataAccess.DAOs;
import DataAccess.DTOs.GradeDTO;
import DataAccess.IdentityMap.IM;
import DataAccess.PrimaryKeys.PK;
import Logic.Grade;
import java.util.List;


public class GradeDAO extends DAO<PK, GradeDTO, Grade>{
    public GradeDAO(){
        super(GradeDTO.class, IM.getInstance().getIdentityMap(Grade.class));
    }

    @Override
    protected Grade convertDtoToBusiness(GradeDTO dto) {
        return new Grade(dto.getStudentId(), dto.getCourseName(), dto.getGrade());
    }

    @Override
    protected GradeDTO convertBusinessToDto(Grade business) {
        return new GradeDTO(business.getStudentId(), business.getCourseName(), business.getGrade());
    }

    @Override
    protected GradeDTO createDTO(List<Object> listFields) { 
        return new GradeDTO((String) listFields.get(0), (String) listFields.get(1), (double) listFields.get(2));
    }

}