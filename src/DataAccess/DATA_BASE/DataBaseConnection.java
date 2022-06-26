package DataAccess.DATA_BASE;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    private static String path;

    public static Connection connect() {
        Path currentRelativePath = Paths.get("");
        String dbPathHelp = currentRelativePath.toAbsolutePath().toString() + ((currentRelativePath.toAbsolutePath().toString().contains("\\dev")) ? "" : "\\dev");
        String dbPath = dbPathHelp+"\\src\\Backend\\DataAccess\\DATA_BASE\\dbSuperLi.db";
        String url = "jdbc:sqlite:";
        url += path == null ? dbPath : path;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void setConnection(String path) {
        DataBaseConnection.path = path;
    }


}
