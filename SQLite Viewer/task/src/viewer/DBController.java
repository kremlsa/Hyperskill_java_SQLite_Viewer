package viewer;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for actions with DB
 *
 * @author kremlsa@yandex.ru
 * @version 1.0
 */
public class DBController implements AutoCloseable {

    private final String queryPublicTable =
            "SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';";
    private Connection con;

    /**
     * Method for setup DB connection
     *
     * @param dbName name of DB file. String
     * @return this class
     */
    public DBController setUpConnection(String dbName) {
        String url = "jdbc:sqlite:" + dbName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try {
            con = dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return this;
    }

    /**
     * Method for getting names of tables
     *
     * @return list of table names. List<String>
     */
    public List<String> getTableNames() {
        try (Statement statement = con.createStatement()) {
            ResultSet tables = statement.executeQuery(queryPublicTable);
            List<String> results = new ArrayList<>();
            while(tables.next()) {
                results.add(tables.getString("name"));
            }
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method for getting table contents as table
     *
     * @param query query. String
     * @return results as table. MyTableModel
     */
    public MyTableModel executeQuery(String query) {
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
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        this.con.close();
    }
}
