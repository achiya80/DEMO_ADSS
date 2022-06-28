package DataAccess.DATA_BASE;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    private static String path;

    //+ ((currentRelativePath.toAbsolutePath().toString().contains("\\dev")) ? "" : "\\dev")
    public static Connection connect() {
        Path currentRelativePath = Paths.get("");
        String dbPathPrefix = currentRelativePath.toAbsolutePath().toString();
        String dbPath = dbPathPrefix + "\\src\\DataAccess\\DATA_BASE\\StudentGradesDB.sqlite";
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
