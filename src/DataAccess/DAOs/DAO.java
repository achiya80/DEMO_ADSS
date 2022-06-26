package DataAccess.DAOs;

import DataAccess.DATA_BASE.DataBaseConnection;
import DataAccess.DATA_BASE.DbException;
import DataAccess.DTOs.DTO;
import DataAccess.PrimaryKeys.PrimaryKey;

import java.lang.reflect.Field;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public abstract class DAO<T1 extends PrimaryKey, T2 extends DTO<T1>, T3> {

    //eyal was here - spooooki
    protected final HashMap<T1, T3> identityMap;//singleton
    private final Class<T2> dtoClass;

    private final SimpleDateFormat simpleDateFormat;
    private final SimpleDateFormat simpleDateFormatNonHour;

    public DAO(Class<T2> dtoClass, HashMap<T1, T3> identityMap) {
        this.identityMap = identityMap;
        this.dtoClass = dtoClass;
        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.simpleDateFormatNonHour = new SimpleDateFormat("yyyy-MM-dd");

    }


    protected abstract T3 convertDtoToBusiness(T2 dto);

    protected abstract T2 convertBusinessToDto(T3 business);

    protected abstract T2 createDTO(List<Object> listFields);

    private T3 getRowFromDB(String condition) {
        List<T3> t3 = getRowsFromDB(condition);
        return t3.size() == 0 ? null : t3.get(0);
    }

    public List<T3> getRowsFromDB(String conditions) {

        List<T2> DTOList = selectAllUnderCondition(conditions);
        return getT3s(DTOList);
    }

    public List<T3> getRowsFromDB() {

        List<T2> DTOList = selectAll();
        return getT3s(DTOList);
    }

    private List<T3> getT3s(List<T2> DTOList) {
        List<T3> list = new LinkedList<>();
        for (T2 t2 : DTOList) {
            if (!identityMap.containsKey(t2.getPrimaryKey())) {
                T3 temp = convertDtoToBusiness(t2);
                identityMap.put(t2.getPrimaryKey(), temp);
                list.add(temp);
            } else {
                list.add(identityMap.get(t2.getPrimaryKey()));
            }
        }
        return list;
    }

    public List<List<Object>> selectCols(List<Field> fields) {

        List<List<Object>> rowList = new ArrayList<>();
        String daoName = getClassName();
        String fieldsNames = classFieldsInParenthesis(fields);
        int length = fieldsNames.length() - 1;
        fieldsNames = fieldsNames.substring(1, length);
        String selectQuery = "SELECT " + fieldsNames + " FROM " + daoName;
        try (Connection conn = DataBaseConnection.connect(); PreparedStatement preparedStatement = conn.prepareStatement(selectQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                List<Object> row = new ArrayList<>();
                for (Field field : fields) {
                    row.add(resultSet.getObject(field.getName()));
                }
                rowList.add(row);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rowList;

    }

    public T3 getRow(T1 primaryKey) {
        return identityMap.containsKey(primaryKey) ?
                identityMap.get(primaryKey) :
                getRowFromDB(primaryKey.primaryKeyToString());
    }

    //return the list of dtos that apply the given condition
    public List<T2> selectAllUnderCondition(String condition) {

        String dtoName = getClassName();
        String selectQuery = "SELECT* " + " FROM " + dtoName + " WHERE " + condition;
        try {
            return getDTOsFromDB(selectQuery);
        } catch (DbException e) {
            throw e;
        }
    }

    //return the list of dtos that apply the given condition
    public List<T3> selectAllUnderConditionToBusiness(String condition) {
        LinkedList<T3> lst = new LinkedList<>();
        List<T2> lst1 = selectAllUnderCondition(condition);
        for (T2 t2 : lst1) {
            lst.add(convertDtoToBusiness(t2));
        }
        return lst;
    }

    protected List<Object> freeQueryOneCol(String rowName, String query) {
        List<Object> result = new LinkedList<>();
        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getObject(rowName));
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        return result;
    }

    private List<T2> getDTOsFromDB(String selectQuery) throws DbException {
        List<T2> dtos = new LinkedList<>();
        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(selectQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                List<Object> daoFields = new ArrayList<>();
                for (Field field : getPrimitiveFields()) {
                    daoFields.add(resultSet.getObject(field.getName()));
                }
                dtos.add(createDTO(daoFields));
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        return dtos;
    }

    public List<T2> selectAll() throws DbException {
        String daoName = getClassName();
        String selectQuery = "SELECT* " + " FROM " + daoName;
        try {
            return getDTOsFromDB(selectQuery);
        } catch (DbException e) {
            throw e;
        }
    }

    public List<T3> selectAllRowsToBusiness() throws DbException {
        String daoName = getClassName();
        String selectQuery = "SELECT* " + " FROM " + daoName;
        try {
            return getDTOsFromDB(selectQuery).stream().map(this::convertDtoToBusiness).collect(Collectors.toList());
        } catch (DbException e) {
            throw e;
        }

    }

    protected List<Field> getPrimitiveFields() {
        List<Field> fieldList = new LinkedList<>();
        for (Field field : dtoClass.getDeclaredFields()) {
            if (field.getType().isPrimitive() || field.getType().getName().equals("java.lang.String") || field.getType().getName().equals("java.lang.Long")) {
                fieldList.add(field);
            }
        }
        return fieldList;
    }

    public void insert(T3 businessObject) {
        T2 t2 = convertBusinessToDto(businessObject);
        Object[] values = t2.getValues();
        String dtoName = getClassName();
        int length = values.length;
        String insertQuery = "INSERT INTO " + dtoName + classFieldsInParenthesis(getPrimitiveFields())
                + " VALUES" + insertHelper(length);
        try (Connection conn = DataBaseConnection.connect(); PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
            for (int i = 1; i <= length; i++)
                preparedStatement.setObject(i, values[i - 1]);
            preparedStatement.executeUpdate();

            identityMap.put(t2.getPrimaryKey(), businessObject);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String insertHelper(int length) {
        StringBuilder string = new StringBuilder("(");
        for (int i = 0; i < length; i++) {
            string.append("?,");
        }
        return string.substring(0, string.length() - 1) + ")";
    }

    private String classFieldsInParenthesis(List<Field> fieldsArray) {
        StringBuilder fields = new StringBuilder("(");
        for (Field f : fieldsArray) {
            if (f.getDeclaringClass().equals(dtoClass))
                fields.append(f.getName()).append(",");
        }
        return fields.substring(0, fields.length() - 1) + ")";
    }

    public void deleteRow(T3 businessObject) {
        T2 t2 = convertBusinessToDto(businessObject);
        String daoName = getClassName();
        String deleteQuery = "DELETE FROM " + daoName + " WHERE " + t2.getPrimaryKey().primaryKeyToString();
        try (Connection conn = DataBaseConnection.connect(); PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery)) {
            preparedStatement.executeUpdate();
            identityMap.remove(t2.getPrimaryKey());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAllRecords() {
        String daoName = getClassName();
        String deleteQuery = "DELETE FROM " + daoName;
        try (Connection conn = DataBaseConnection.connect(); PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery)) {
            preparedStatement.executeUpdate();
            identityMap.clear();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteRows(String condition) {
        List<T2> businessObjects = getDTOsFromDB("SELECT * " + " FROM " + getClassName() + " WHERE " + condition);
        String daoName = getClassName();
        String deleteQuery = "DELETE FROM " + daoName + " WHERE " + condition;
        try (Connection conn = DataBaseConnection.connect(); PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery)) {
            preparedStatement.executeUpdate();
            businessObjects.forEach(t2 -> identityMap.remove(t2.getPrimaryKey()));
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    private String getClassName() {
        String daoName = dtoClass.getName();
        daoName = daoName.substring(daoName.lastIndexOf(".") + 1);
        return daoName.substring(0, daoName.length() - 3);
    }

    public void update(T3 businessObject) {
        T2 t2 = convertBusinessToDto(businessObject);

        String daoName = getClassName();
        String updateQuery = "UPDATE " + daoName + " SET " + t2.valuesToString()
                + " WHERE " + t2.getPrimaryKey().primaryKeyToString();

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(updateQuery)) {

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String convertDBdateToBusiness(String date) {
        String[] split = date.split("-");
        return split[2] + "/" + split[1] + "/" + split[0];
    }

    public String convertBusinessDateToDB(String date) {
        String[] split = date.split("/");
        return split[2] + "-" + split[1] + "-" + split[0];
    }
/*    public Object[] getValues(T2 dto){
        return dto.getValues();
    }*/

/*    public static void main(String[]args){
        UmlGenerator.main(args);

    }*/

    public SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }

    public SimpleDateFormat getSimpleDateFormatNonHour() {
        return simpleDateFormatNonHour;
    }

    public T3 getFirstRow() {
        String dtoName = getClassName();
        String selectQuery = "SELECT * FROM " + dtoName + " LIMIT 1";
        try {
            return convertDtoToBusiness(getDTOsFromDB(selectQuery).get(0));
        } catch (DbException e) {
            throw e;
        }
    }

}
