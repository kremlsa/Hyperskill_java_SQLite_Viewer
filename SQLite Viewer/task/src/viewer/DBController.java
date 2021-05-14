package viewer;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
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

    public MyTableModel executeQuery(String dbName, String query) {
        String url = "jdbc:sqlite:" + dbName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection con = dataSource.getConnection()) {
            if (con.isValid(5)) {
                System.out.println("Connection is valid.");
                try {
                    Statement statement= con.createStatement();
                    ResultSet rs = statement.executeQuery(query);
                    ResultSetMetaData md = rs.getMetaData();
                    int colCount = md.getColumnCount();
                    String[] columns = new String[colCount];
                    int size = 0;
                    while ( rs.next() ) {
                        size++;
                    }
                    rs = statement.executeQuery(query);
                    Object[][] data = new Object[size][colCount];

                    for (int i = 1; i <= colCount ; i++){
                        String col_name = md.getColumnName(i);
                        columns[i - 1] = col_name;
                        System.out.println(col_name + " " + rs.getString(i));
                    }
                    int j = 0;
                    while ( rs.next() ) {
                        for (int i = 1; i <= colCount ; i++){
                            data[j][i - 1] = rs.getString(i);
                        }
                        j++;
                    }
                    con.close();
                    return new MyTableModel(data, columns);
                } catch (Exception e) {
                    System.err.println("exception! ");
                    System.err.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
