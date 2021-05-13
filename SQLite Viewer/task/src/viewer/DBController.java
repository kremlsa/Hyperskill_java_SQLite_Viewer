package viewer;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBController {

    private final String queryPublicTable =
            "SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';";

    public List<String> setUpDBConnection(String dbName) {
        String url = "jdbc:sqlite:" + dbName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection con = dataSource.getConnection()) {
            if (con.isValid(5)) {
                System.out.println("Connection is valid.");
                try (Statement statement = con.createStatement()) {
                    // Statement execution
                    ResultSet tables = statement.executeQuery(queryPublicTable);
                    List<String> results = new ArrayList<>();
                    while(tables.next()) {
                        results.add(tables.getString("name"));
                    }
                    return results;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
