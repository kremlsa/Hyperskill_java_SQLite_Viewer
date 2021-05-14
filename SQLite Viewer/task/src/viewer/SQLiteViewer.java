package viewer;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SQLiteViewer extends JFrame {
    JTextField nameTextField;
    JButton openFileButton;
    JComboBox comboBox;
    JTextArea textArea;
    JButton executeButton;
    DBController dbController;
    JTable table;
    TableModel tableModel;
    JScrollPane sp;
    String dbName;

    public SQLiteViewer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 900);
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(5);
        setLayout(flowLayout);
        setResizable(false);
        //inititalize compomnents
        initComponents();

        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("SQLite Viewer");
    }

    private void initComponents() {
        //add text field
        nameTextField = new JTextField("", 50);
        nameTextField.setName("FileNameTextField");
        add(nameTextField);

        //add button
        openFileButton = new JButton("Open");
        openFileButton.setName("OpenFileButton");
        add(openFileButton);
        //add listener for button
        openFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFileButtonClick();
            }
        } );

        //add comboBox
        comboBox = new JComboBox();
        comboBox.setPreferredSize(new Dimension(550,25));
        comboBox.setName("TablesComboBox");
        add(comboBox);
        //add listener for combobox
        comboBox.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                generateQuery();
            }
        });

        //add text area
        textArea = new JTextArea("", 20, 50);
        textArea.setName("QueryTextArea");
        add(textArea);

        //add button
        executeButton = new JButton("Execute");
        executeButton.setName("ExecuteQueryButton");
        //add listener for button
        executeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executeButtonClick();
            }
        } );
        add(executeButton);

        //add table
        String[] columns = {"test1" , "test2"};
        Object[][] data = {{"1" , "2"} , {"3" , "4"}};
        tableModel = new MyTableModel(data, columns);
        table = new JTable(tableModel);
        table.setName("Table");
        tableModel.addTableModelListener(new CustomListener());
        sp = new JScrollPane(table);
        this.add(sp);

    }

    private void executeButtonClick() {
        tableModel = dbController.executeQuery(dbName, textArea.getText());
        table.setModel(tableModel);
        table.repaint();
    }

    private void generateQuery() {
        textArea.setText(String.format("SELECT * FROM %s;", comboBox.getSelectedItem()));
    }

    private void openFileButtonClick() {
        dbName = nameTextField.getText();
        dbController = new DBController();
        comboBox.removeAllItems();
        dbController.setUpDBConnection(dbName)
                .forEach(comboBox::addItem);
    }

}

class CustomListener implements TableModelListener {
    @Override
    public void tableChanged(TableModelEvent e) {
        System.out.println("Table Updated!");
    }
}
