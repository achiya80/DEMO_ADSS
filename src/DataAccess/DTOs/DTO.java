package DataAccess.DTOs;

import DataAccess.PrimaryKeys.PrimaryKey;

import java.lang.reflect.Field;


public abstract class DTO<T extends PrimaryKey> {

    private final T primaryKey;

    public DTO(T primaryKey) {
        this.primaryKey = primaryKey;
    }

    protected static <T> Field[] getFields(String[] fields, Class<T> tClass) {
        Field[] f = new Field[fields.length];
        for (int i = 0; i < f.length; i++) {
            try {
                f[i] = tClass.getDeclaredField(fields[i]);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
        return f;
    }


    public abstract Object[] getValues();

    public final String valuesToString() {
        StringBuilder fields = new StringBuilder();
        Field[] fieldsArray = this.getClass().getDeclaredFields();
        Object[] values = getValues();
        int i = 0;
        for (Field f : fieldsArray) {
            if (f.getType().isPrimitive() || f.getType().getName().equals("java.lang.String"))
                fields.append(f.getName()).append(" = '").append(values[i++].toString()).append("',");
        }
        return fields.substring(0, fields.length() - 1);
    }


    public T getPrimaryKey() {
        return this.primaryKey;
    }
}
