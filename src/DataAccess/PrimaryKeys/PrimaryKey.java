package DataAccess.PrimaryKeys;


public interface PrimaryKey {

    Object[] getValue();

    boolean equals(Object other);

    int hashCode();

    String primaryKeyToString();
}
