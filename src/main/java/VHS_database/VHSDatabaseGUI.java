package VHS_database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Calendar;
import java.util.Vector;


public class VHSDatabaseGUI extends JFrame {
    
    private JTable VHSDataTable;
    private JPanel rootPanel;
    private JTextField titleTextField;
    private JTextField yearTextField;
    private JButton addNewVHSButton;
    private JButton quitButton;
    private JButton deleteVHSButton;
    private JSpinner ratingSpinner;
    
    
    private VHSDatabase db;
    
    private DefaultTableModel tableModel;
    private Vector columnNames;
    
    
    VHSDatabaseGUI(VHSDatabase db) {
    
        this.db = db;
        
        setContentPane(rootPanel);
        pack();
        setTitle("VHS Database Application");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        configureTable();
        
        //Set up the rating spinner. SpinnerNumberModel constructor arguments: spinner's initial value, min, max, step.
        ratingSpinner.setModel(new SpinnerNumberModel(1, VHSDatabase.VHS_MIN_RATING, VHSDatabase.VHS_MAX_RATING, 1));
        
        
        setVisible(true);
        
        //Event handlers for add, delete and quit buttons
        addNewVHSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addVHS();
            }
        });
        
        deleteVHSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedVHS();
            }
        });
    
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VHSDatabaseGUI.this.dispose();  // Closes this JFrame, which ends the program.
            }
        });
    
    }
    
    
    private void addVHS() {
        //Get VHS title, make sure it's not blank
        String titleData = titleTextField.getText();
        
        if (titleData == null || titleData.trim().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Please enter a title for the new VHS");
            return;
        }
        
        //Get VHS year. Check it's a number between 1900 and present year
        int yearData;
        
        try {
            yearData = Integer.parseInt(yearTextField.getText());
            if (yearData < 1900 || yearData > Calendar.getInstance().get(Calendar.YEAR)){
                //Calendar.getInstance() returns a Calendar object representing right now.
                //calenderObject.get(Calendar.MONTH) gets current month, calenderObject.get(Calendar.SECOND) gets current second
                //Can get and set other time/date fields- check Java documentation for others
                //http://docs.oracle.com/javase/7/docs/api/java/util/Calendar.html
                throw new NumberFormatException("Year needs to be between 1900 and present year");
            }
        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(rootPane,
                    "Year needs to be a number between 1900 and now");
            return;
        }
        
        //Using a spinner means we are guaranteed to get a number in the range we set, so no validation needed.
        int ratingData = (Integer)(ratingSpinner.getValue());
        
        db.addVHS(titleData, yearData, ratingData);
        
        updateTable();
        
    }
    
    
    private void deleteSelectedVHS() {
        
        int currentRow = VHSDataTable.getSelectedRow();
    
        if (currentRow == -1) {      // -1 means no row is selected. Display error message.
            JOptionPane.showMessageDialog(rootPane, "Please choose a VHS to delete");
        }
    
        else {
            db.deleteVHS(currentRow);
            updateTable();
        }
    }
    
    
    private void configureTable() {
    
        //Set up JTable
        VHSDataTable.setGridColor(Color.BLACK);
        
        //Enable sorting
        VHSDataTable.setAutoCreateRowSorter(true);
    
        columnNames = db.getColumnNames();
        Vector data = db.getAllVHS();
    
        // Custom methods for DefaultTableModel
        // Want to customize which cells are editable - the isCellEditable method
        // And, what happens when an editable cell is edited - the setValueAt method
        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return (col == 3);  // Rating column only.
            }
        
            @Override
            public void setValueAt(Object val, int row, int col) {
            
                // Get row and send new value to DB for update
                int id = (int) getValueAt(row, 0);
            
                try {
                    int newRating = Integer.parseInt(val.toString());
                    if (newRating < VHSDatabase.VHS_MIN_RATING || newRating > VHSDatabase.VHS_MAX_RATING) {
                        throw new NumberFormatException();
                    }
                    db.changeRating(id, newRating);
                    updateTable();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(VHSDatabaseGUI.this, "Enter a number between 1 and 5 for the rating");
                }
            }
        };
    
        VHSDataTable.setModel(tableModel);
    
    }
    
    
    private void updateTable() {
        
        Vector data = db.getAllVHS();
        tableModel.setDataVector(data, columnNames);
    
    }
    
    
}