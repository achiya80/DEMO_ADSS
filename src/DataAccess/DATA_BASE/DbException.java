package DataAccess.DATA_BASE;

public class DbException extends RuntimeException {
    public DbException(String s) {
        super(s);
    }

    public DbException() {
        super();
    }
}
