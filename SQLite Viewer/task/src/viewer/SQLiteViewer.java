package viewer;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Class for creating application interface
 *
 * @author kremlsa@yandex.ru
 * @version 1.0
 */
public class SQLiteViewer extends JFrame {
    private JTextField nameTextField;
    private JButton openFileButton;
    private JComboBox comboBox;
    private JTextArea textArea;
    private JButton executeButton;
    private DBController dbController;
    private JTable table;
    private TableModel tableModel;
    private JScrollPane sp;
    private String dbName;


    public SQLiteViewer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 900);
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(5);
        setLayout(flowLayout);
        setResizable(false);
        initComponents();//inititalize compomnents
        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("SQLite Viewer");
    }

    /**
     * Method for initializing interface components
     *
     */
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
        textArea.setEnabled(false);
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
        executeButton.setEnabled(false);
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

    /**
     * Method for executing click on button - execute
     *
     */
    private void executeButtonClick() {
        tableModel = dbController.executeQuery(textArea.getText());
        table.setModel(tableModel);
        table.repaint();
    }

    /**
     * Method for generating query for text area/
     *
     */
    private void generateQuery() {
        textArea.setText(String.format("SELECT * FROM %s;", comboBox.getSelectedItem()));
    }

    /**
     * Method for executing click on button - open file
     *
     */
    private void openFileButtonClick() {
        dbName = nameTextField.getText();
        File file = new File (dbName);
        if(!file.exists())
        {
            textArea.setEnabled(false);
            executeButton.setEnabled(false);
            errorBox("File doesn't exist!");
        } else {
            textArea.setEnabled(true);
            executeButton.setEnabled(true);
            dbController = new DBController();
            comboBox.removeAllItems();
            dbController.setUpConnection(dbName)
                        .getTableNames()
                        .forEach(comboBox::addItem);
        }
    }

    /**
     * Method for displaying error popup window
     *
     * @param errorMessage Error message. String
     */
    public static void errorBox(String errorMessage)
    {
        JOptionPane.showMessageDialog(new Frame(), errorMessage);
    }

}

/**
 * Inner Class for table model
 *
 * @author kremlsa@yandex.ru
 * @version 1.0
 */
class CustomListener implements TableModelListener {
    @Override
    public void tableChanged(TableModelEvent e) {
        System.out.println("Table Updated!");
    }
}
