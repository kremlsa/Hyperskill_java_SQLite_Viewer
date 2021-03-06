package viewer;

import javax.swing.table.AbstractTableModel;

/**
 * Class for custom table model
 *
 * @author kremlsa@yandex.ru
 * @version 1.0
 */
public class MyTableModel extends AbstractTableModel {
    String[] columns;
    Object[][] data;

    public MyTableModel (Object[][] data, String[] columns) {
        this.columns = columns;
        this.data = data;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

}
