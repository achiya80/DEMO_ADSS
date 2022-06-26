package DataAccess.PrimaryKeys;

import java.lang.reflect.Field;
import java.util.Arrays;

public class PK implements PrimaryKey {

    private final Field[] fields;
    private final Object[] objects;


    public PK(Field[] fields, Object... o) {
        this.fields = fields;
        this.objects = o;
    }

    @Override
    public Object[] getValue() {
        return objects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PK)) return false;
        PK that = (PK) o;
        return Arrays.equals(objects, that.objects);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(objects);
    }

    public String primaryKeyToString() {
        Object[] values = getValue();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            stringBuilder.append(fields[i].getName()).
                    append(" = ").append(fields[i].getType().getName().equals("java.lang.String") ?
                            "'" + values[i] + "'" : values[i]).append(" AND ");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 4);
    }
}