package viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLiteViewer extends JFrame {
    JTextField nameTextField;
    JButton openFileButton;
    JComboBox comboBox;
    JTextArea textArea;
    JButton executeButton;
    DBController dbController;

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

        //add text area
        textArea = new JTextArea("", 20, 50);
        textArea.setName("QueryTextArea");
        add(textArea);

        //add button
        executeButton = new JButton("Execute");
        executeButton.setName("ExecuteQueryButton");
        add(executeButton);

    }

    private void openFileButtonClick() {
        String dbname = nameTextField.getText();
        dbController = new DBController();
        ResultSet tables =dbController.setUpDBConnection(dbname);
            try {
                System.out.println(tables.getString("name") + "*********");
                comboBox.addItem(tables.getString("name"));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

    }

}
