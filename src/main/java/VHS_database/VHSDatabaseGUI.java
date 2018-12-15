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

    //private JSpinner genreTextField;
    //JTextField variable for film genre text field

    private JTextField yearTextField;
    //JTextField variable for film year text field

    private JButton addNewVHSButton;
    //JButton variable for adding new VHS

    private JButton deleteVHSButton;
    //JButton variable for deleting VHS

    private JSpinner ratingSpinner;
    //JSpinner variable for film rating selection

    private JSpinner genreSpinner;

    private JButton quitButton;
    //JButton variable for quitting application

    private VHSDatabase db;
    //VHSDatabase variable for selecting database
    
    private DefaultTableModel tableModel;
    //DefaultTableModel variable for selecting table model

    private Vector columnNames;
    //Vector variable for column names

    private SpinnerListModel genreModel = new SpinnerListModel(VHSDatabase.genreList);
    //SpinnerListModel variable for film genres

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

        ratingSpinner.setModel(new SpinnerNumberModel(1,
                VHSDatabase.VHS_MIN_RATING, VHSDatabase.VHS_MAX_RATING, 1));
        //Set up the rating spinner. SpinnerNumberModel constructor arguments: spinner's initial value, min, max, step.

        genreSpinner.setModel(genreModel);
        //Set up the genre spinner
        
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

        int upcData;
        //Integer variable for VHS UPC

        try {
            upcData = Integer.parseInt(upcTextField.getText());

            String upcLength = upcTextField.getText();
            //String variable for UPC length

            if (upcLength.length() != VHSDatabase.UPC_LENGTH) {
                //Check UPC length (12 numbers)

                throw new NumberFormatException(
                        VHSDatabase.ERROR_NUMB
                                + VHSDatabase.YIELD_UPC);
            }

        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(rootPane,
                    VHSDatabase.ERROR_NUMB+
                            VHSDatabase.YIELD_UPC);
            //Check for valid number/display error message

            return;
            //Return selected data for UPC

        }

        String titleData = titleTextField.getText();
        //String variable to get VHS title

        if (titleData == null || titleData.trim().equals("")) {
            JOptionPane.showMessageDialog(rootPane,
                    VHSDatabase.ERROR_NULL);
            //Check for empty VHS title/display error message

            return;
            //Return selected data for Title

        }

        String directorData = directorTextField.getText();
        //String variable to get film director

        if (directorData == null) {
            JOptionPane.showMessageDialog(rootPane,
                    VHSDatabase.ERROR_NULL);
            //Check for null film director/display error message

            return;
            //Return selected data for Director
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
            //Catch errors/display error messages

            return;
            //Return selected data for year
        }

        String genreData = (String)(genreSpinner.getValue());
        //String variable to get film genres

        int ratingData = (Integer)(ratingSpinner.getValue());
        //Integer variable to get rating values
        
        db.addVHS(upcData, titleData, directorData, genreData, yearData, ratingData);
        //Add data to database
        
        updateTable();
        //Update VHS table
        
    }


    
    private void deleteSelectedVHS() {

        int currentRow = VHSDataTable.getSelectedRow();
        //Integer variable from current row selection
    
        if (currentRow == -1) {
            JOptionPane.showMessageDialog(rootPane,
                    VHSDatabase.ERROR_SLCT);
            //Check for no row selected error/display error message
        }
    
        else {
            int currentID = (int) VHSDataTable.getValueAt(
                    currentRow, 0);
            //Get ID integer for selected VHS

            db.deleteVHS(currentID);
            //Delete selected VHS

            updateTable();
            //Update VHS table
        }

    }


    
    private void configureTable() {

        VHSDataTable.setGridColor(Color.BLACK);
        //Set grid color to black

        VHSDataTable.setAutoCreateRowSorter(true);
        //Enable sorting
    
        columnNames = db.getColumnNames();
        //Get column names

        Vector data = db.getAllVHS();
        //Get vector data from all VHS
    

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return (col <= 6 && col >= 1);
                //Return valid columns only
            }

            @Override
            public void setValueAt(Object val, int row, int col) throws NullPointerException,
                    NumberFormatException {

                int id = (int) getValueAt(row, 0);
                //Get VHS ID from selected VHS row

                if (col == 1)
                {
                    int newUPC = Integer.parseInt(val.toString());
                    //Get UPC number

                    if (newUPC != VHSDatabase.UPC_LENGTH) {
                        JOptionPane.showMessageDialog(VHSDatabaseGUI.this,
                                VHSDatabase.ERROR_NUMB
                                        +VHSDatabase.YIELD_UPC);
                        throw new NumberFormatException();
                        //Catch errors/display error message
                    }
                    db.changeUPC(id, newUPC);
                    //Change UPC number to new value
                }

                if (col == 2)
                {
                    String newTitle = (val.toString());
                    //Get VHS title

                    if (newTitle == null| newTitle == "") {
                        JOptionPane.showMessageDialog(VHSDatabaseGUI.this,
                                VHSDatabase.ERROR_NULL);
                        throw new NullPointerException();
                        //Catch errors/display error message
                    }

                    db.changeTitle(id, newTitle);
                    //Change title to new value
                }

                if (col == 3)
                {
                    String newDirector = (val.toString());
                    //Get VHS director

                    if (newDirector == null|newDirector == "") {
                        JOptionPane.showMessageDialog(VHSDatabaseGUI.this,
                                VHSDatabase.ERROR_NULL);
                        throw new NullPointerException();
                        //Catch errors/display error message
                    }

                    db.changeDirector(id, newDirector);
                    //Change director to new value
                }

                if (col == 4)
                {
                    String newGenre = (val.toString());
                    //Get VHS genre

                    if (newGenre == null|newGenre == null) {
                        JOptionPane.showMessageDialog(VHSDatabaseGUI.this,
                                VHSDatabase.ERROR_NULL);
                        throw new NullPointerException();
                        //Catch errors/display error message
                    }

                    db.changeGenre(id, newGenre);
                    //Change genre to new value
                }

                if (col == 5)
                {
                    int newYear = Integer.parseInt(val.toString());
                    //Get VHS year

                    if (newYear < VHSDatabase.VHS_MIN_YEAR || newYear > VHSDatabase.VHS_MAX_YEAR) {
                        JOptionPane.showMessageDialog(VHSDatabaseGUI.this,
                                VHSDatabase.ERROR_NUMB
                                        +VHSDatabase.YIELD_RATING);
                        throw new NumberFormatException();
                        //Catch errors/display error message
                    }

                    db.changeYear(id, newYear);
                    //Change year to new value
                }

                if (col == 6)
                {
                    int newRating = Integer.parseInt(val.toString());
                    //Get VHS rating

                    if (newRating < VHSDatabase.VHS_MIN_RATING || newRating > VHSDatabase.VHS_MAX_RATING) {
                        JOptionPane.showMessageDialog(VHSDatabaseGUI.this,
                                VHSDatabase.ERROR_NUMB
                                        +VHSDatabase.YIELD_RATING);
                        throw new NumberFormatException();
                        //Catch errors/display error message
                    }

                    db.changeRating(id, newRating);
                    //Change rating to new value
                }

                updateTable();
                //Update table data
            }
        };

        VHSDataTable.setModel(tableModel);
        //Set VHS data table model with new info
    }


    
    private void updateTable() {
        
        Vector data = db.getAllVHS();
        //Get vector data from all VHS

        tableModel.setDataVector(data, columnNames);
        //Set VHS data
    }
}