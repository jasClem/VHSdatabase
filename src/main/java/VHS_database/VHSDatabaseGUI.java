//VHS Database GUI - Jason

package VHS_database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Calendar;
import java.util.Vector;


public class VHSDatabaseGUI extends JFrame {

//region [VHSDatabaseGUI variables]

    private JTable VHSDataTable;
    //JTable variable for VHS table

    private JPanel rootPanel;
    //JPanel variable for rootPanel

    private JTextField upcTextField;
    //JTextField variable for VHS UPC text field

    private JTextField titleTextField;
    //JTextField variable for VHS title text field

    private JTextField directorTextField;
    //JTextField variable for film director text field

    private JTextField genreTextField;
    //JTextField variable for film genre text field

    private JTextField yearTextField;
    //JTextField variable for film year text field

    private JButton addNewVHSButton;
    //JButton variable for adding new VHS

    private JButton deleteVHSButton;
    //JButton variable for deleting VHS

    private JSpinner ratingSpinner;
    //JSpinner variable for film rating selection

    private JButton quitButton;
    //JButton variable for quitting application

    private VHSDatabase db;
    //VHSDatabase variable for selecting database
    
    private DefaultTableModel tableModel;
    //DefaultTableModel variable for selecting table model

    private Vector columnNames;
    //Vector variable for column names

    public static boolean loaded = false;

//endregion

    VHSDatabaseGUI(VHSDatabase db) {
    
        this.db = db;
        //Set VHS database
        
        setContentPane(rootPanel);
        //Set rootPanel

        setTitle(VHSDatabase.APP_TITLE);
        //Set app title

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Set app close operation
        
        configureTable();
        //Configure table

        //System.out.println("\n\n"+VHSDatabase.APP_TITLE+" table loaded successfully");

        ratingSpinner.setModel(new SpinnerNumberModel(1,
                VHSDatabase.VHS_MIN_RATING, VHSDatabase.VHS_MAX_RATING, 1));
        //Set up the rating spinner. SpinnerNumberModel constructor arguments: spinner's initial value, min, max, step.
        
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
                VHSDatabaseGUI.this.dispose();
                //Close JFrame/end application
            }
        });

        pack();
        setVisible(true);
        //Display GUI

        //System.out.println("\n\n"+VHSDatabase.APP_TITLE+" loaded successfully");
        loaded = true;
        //Set loaded boolean to true
    
    }
    
    
    private void addVHS() {
        //Add VHS (GUI)

        int upcData;
        //Integer variable for VHS UPC

        try {
            upcData = Integer.parseInt(upcTextField.getText());
            //Get VHS UPC

        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(rootPane,
                    VHSDatabase.ERROR_NUMB);
            return;
            //Check for valid number/display error message
        }

        String titleData = titleTextField.getText();
        //String variable to get VHS title

        if (titleData == null || titleData.trim().equals("")) {
            JOptionPane.showMessageDialog(rootPane,
                    VHSDatabase.ERROR_NULL);
            return;
            //Check for empty VHS title/display error message
        }

        String directorData = directorTextField.getText();
        //String variable to get film director

        if (directorData == null) {
            JOptionPane.showMessageDialog(rootPane,
                    VHSDatabase.ERROR_NULL);
            return;
            //Check for null film director/display error message
        }

        String genreData = genreTextField.getText();
        //String variable to get film director

        if (genreData == null) {
            JOptionPane.showMessageDialog(rootPane,
                    VHSDatabase.ERROR_NULL);
            return;
            //Check for null film genre/display error message
        }

        int yearData;
        //Integer variable to get VHS film year
        
        try {
            yearData = Integer.parseInt(yearTextField.getText());
            if (yearData < 1900 || yearData > Calendar.getInstance().get(Calendar.YEAR)){
                //Check film year (Between 1900 and present)

                throw new NumberFormatException(
                        VHSDatabase.ERROR_NUMB
                                +VHSDatabase.YIELD_YEAR);
            }
        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(rootPane,
                    VHSDatabase.ERROR_NUMB
                            +VHSDatabase.YIELD_YEAR);
            return;
        }
        
        //Using a spinner means we are guaranteed to get a number in the range we set, so no validation needed.
        int ratingData = (Integer)(ratingSpinner.getValue());
        
        db.addVHS(upcData, titleData, directorData, genreData, yearData, ratingData);
        
        updateTable();
        
    }
    
    
    private void deleteSelectedVHS() {
        //Delete VHS (GUI)

        int currentRow = VHSDataTable.getSelectedRow();
        //Integer variable from current row selection
    
        if (currentRow == -1) {
            //Check for no row selected/
            JOptionPane.showMessageDialog(rootPane,
                    VHSDatabase.ERROR_SLCT);
        }
    
        else {

            int currentID = (int) VHSDataTable.getValueAt(currentRow, 0);

            db.deleteVHS(currentID);
            updateTable();
        }

    }
    
    
    private void configureTable() {
        //Configure VHS table (GUI)
    
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
                return (col == 6);  // Rating column only.
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
                    JOptionPane.showMessageDialog(VHSDatabaseGUI.this,
                            VHSDatabase.ERROR_NUMB
                                    +VHSDatabase.YIELD_RATING);
                }
            }
        };
    
        VHSDataTable.setModel(tableModel);
    
    }
    
    
    private void updateTable() {
        //Update VHS table (GUI)
        
        Vector data = db.getAllVHS();
        tableModel.setDataVector(data, columnNames);
    
    }


}