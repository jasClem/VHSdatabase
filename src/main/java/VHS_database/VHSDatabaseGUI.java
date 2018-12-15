//VHS Database GUI - Jason

package VHS_database;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    private JTextField yearTextField;
    //JTextField variable for film year text field

    private JButton addNewVHSButton;
    //JButton variable for adding new VHS

    private JButton deleteVHSButton;
    //JButton variable for deleting VHS

    private JSpinner ratingSpinner;
    //JSpinner variable for film rating selection

    private JSpinner genreSpinner;
    //JSpinner variable for film genre selection

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

        addNewVHSButton.addActionListener(new ActionListener() {
            //Add New VHS button
            @Override
            public void actionPerformed(ActionEvent e) {
                addVHS();
                //Event handler for adding new VHS button
            }
        });
        
        deleteVHSButton.addActionListener(new ActionListener() {
            //Delete VHS button
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedVHS();
                //Event handler for deleting selected VHS button
            }
        });
    
        quitButton.addActionListener(new ActionListener() {
            //Quit button
            @Override
            public void actionPerformed(ActionEvent e) {
                VHSDatabaseGUI.this.dispose();
                //Event handler for closing JFrame/ending application
            }
        });

        VHSDataTable.addMouseListener(new MouseAdapter(){
            //Mouse listener
            @Override
            public void mouseClicked(MouseEvent e){
                mouseClickVHS();
                //Event handler for mouse cell selections

            }
        });

        Action action = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                TableCellListener tcl = (TableCellListener)e.getSource();
                //Get info from TableCellListener

                /*
                For debug messages using table cell selection
                System.out.println("\n");
                System.out.println("   Row: " + tcl.getRow());
                System.out.println("Column: " + tcl.getColumn());
                System.out.println("   Old: " + tcl.getOldValue());
                System.out.println("   New: " + tcl.getNewValue());
                */

                if (tcl.getColumn() == 4){

                    //Not finished
                    //For adding genre spinner to correct selected cell
                }

                if (tcl.getColumn() == 6){

                    //Not finished
                    //For adding rating spinner to correct selected cell
                }
            }
        };

        TableCellListener tcl = new TableCellListener(VHSDataTable, action);
        //TableCellListener variable

        pack();
        setVisible(true);
        //Pack & enable GUI visibility
    }



    private void mouseClickVHS() {

        int currentRow = VHSDataTable.getSelectedRow();
        //Integer variable from current row selection

        String selectedUPC = (String) VHSDataTable.getValueAt(currentRow, 1);
        String selectedTitle = (String) VHSDataTable.getValueAt(currentRow, 2);
        String selectedDirector = (String) VHSDataTable.getValueAt(currentRow, 3);
        String selectedGenre = (String) VHSDataTable.getValueAt(currentRow, 4);
        int selectedYear = (int) VHSDataTable.getValueAt(currentRow, 5);
        int selectedRating = (int) VHSDataTable.getValueAt(currentRow, 6);
        //Get selected VHS info

        if (currentRow > -1)
        {
            upcTextField.setText(selectedUPC);
            titleTextField.setText(selectedTitle);
            directorTextField.setText(selectedDirector);
            genreSpinner.setValue(String.valueOf(selectedGenre));
            yearTextField.setText(String.valueOf(selectedYear));
            ratingSpinner.setValue(selectedRating);
            //Insert selected info into Text & Spinner fields

        } else {

            upcTextField.setText("");
            titleTextField.setText("");
            directorTextField.setText("");
            genreSpinner.setValue("Action");
            yearTextField.setText(String.valueOf(""));
            ratingSpinner.setValue(1);
            //Clear text/spinner fields
        }
    }



    private void addVHS() {

        String upcData;
        //String variable for VHS UPC

        try {
            upcData = upcTextField.getText();

            if (upcData.length() != VHSDatabase.UPC_LENGTH) {
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


        JOptionPane.showMessageDialog(rootPane,
                titleData+" has been added to the database");

        upcTextField.setText("");
        titleTextField.setText("");
        directorTextField.setText("");
        genreSpinner.setValue("Action");
        yearTextField.setText(String.valueOf(""));
        ratingSpinner.setValue(1);
        //Clear text/spinner fields

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

            String vhsTitle = (String) VHSDataTable.getValueAt(
                    currentRow, 2);

            db.deleteVHS(currentID);
            //Delete selected VHS

            JOptionPane.showMessageDialog(rootPane,
                    vhsTitle+" has been deleted from the database");

            upcTextField.setText("");
            titleTextField.setText("");
            directorTextField.setText("");
            genreSpinner.setValue("Action");
            yearTextField.setText(String.valueOf(""));
            ratingSpinner.setValue(1);
            //Clear text/spinner fields

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

                String selectedUPC = (String) VHSDataTable.getValueAt(row, 1);
                String selectedTitle = (String) VHSDataTable.getValueAt(row, 2);
                String selectedDirector = (String) VHSDataTable.getValueAt(row, 3);
                String selectedGenre = (String) VHSDataTable.getValueAt(row, 4);
                int selectedYear = (int) VHSDataTable.getValueAt(row, 5);
                int selectedRating = (int) VHSDataTable.getValueAt(row, 6);
                //Get selected VHS info

                if (col == 1)
                {
                    String newUPC = val.toString();
                    //Get UPC number

                    if (newUPC.length() != VHSDatabase.UPC_LENGTH) {
                        JOptionPane.showMessageDialog(VHSDatabaseGUI.this,
                                VHSDatabase.ERROR_NUMB
                                        +VHSDatabase.YIELD_UPC);
                        throw new NumberFormatException();
                        //Catch errors/display error message
                    }
                    db.changeUPC(id, newUPC);
                    //Change UPC number to new value

                    if (!newUPC.equals(selectedUPC)){
                        JOptionPane.showMessageDialog(rootPane,
                                selectedTitle+"'s UPC has been changed from ["
                                        +selectedUPC+"] to ["+newUPC+"]");
                        //If selection is changed, display message
                    }
                }

                if (col == 2)
                {
                    String newTitle = val.toString();
                    //Get VHS title

                    if (newTitle == null) {
                        JOptionPane.showMessageDialog(VHSDatabaseGUI.this,
                                VHSDatabase.ERROR_NULL);
                        throw new NullPointerException();
                        //Catch errors/display error message
                    }

                    db.changeTitle(id, newTitle);
                    //Change title to new value

                    if (!newTitle.equals(selectedTitle)){
                        JOptionPane.showMessageDialog(rootPane,
                                newTitle+"'s title has been changed from ["
                                        +selectedTitle+"] to ["+newTitle+"]");
                        //If selection is changed, display message
                    }
                }

                if (col == 3)
                {
                    String newDirector = val.toString();
                    //Get VHS director

                    if (newDirector == null) {
                        JOptionPane.showMessageDialog(VHSDatabaseGUI.this,
                                VHSDatabase.ERROR_NULL);
                        throw new NullPointerException();
                        //Catch errors/display error message
                    }

                    db.changeDirector(id, newDirector);
                    //Change director to new value

                    if (!newDirector.equals(selectedDirector)){
                        JOptionPane.showMessageDialog(rootPane,
                                selectedTitle+"'s director has been changed from ["
                                        +selectedDirector+"] to ["+newDirector+"]");
                        //If selection is changed, display message
                    }

                }

                if (col == 4)
                {


                    String newGenre = (String) genreSpinner.getValue();
                    //Get VHS genre

                    if (newGenre == null) {
                        JOptionPane.showMessageDialog(VHSDatabaseGUI.this,
                                VHSDatabase.ERROR_NULL);
                        throw new NullPointerException();
                        //Catch errors/display error message
                    }

                    db.changeGenre(id, newGenre);
                    //Change genre to new value

                    if (!newGenre.equals(selectedGenre)){
                        JOptionPane.showMessageDialog(rootPane,
                                selectedTitle+"'s genre has been changed from ["
                                        +selectedGenre+"] to ["+newGenre+"]");
                        //If selection is changed, display message
                    }


                }

                if (col == 5)
                {
                    int newYear = Integer.parseInt(val.toString());
                    //Get VHS year

                    if (newYear < VHSDatabase.VHS_MIN_YEAR || newYear > VHSDatabase.VHS_MAX_YEAR) {
                        JOptionPane.showMessageDialog(VHSDatabaseGUI.this,
                                VHSDatabase.ERROR_NUMB
                                        +VHSDatabase.YIELD_YEAR);
                        throw new NumberFormatException();
                        //Catch errors/display error message
                    }

                    db.changeYear(id, newYear);
                    //Change year to new value

                    if (newYear != selectedYear) {
                        JOptionPane.showMessageDialog(rootPane,
                                selectedTitle+"'s year has been changed from ["
                                        +selectedYear+"] to ["+newYear+"]");
                        //If selection is changed, display message
                    }
                }

                if (col == 6)
                {
                    int newRating = (int) ratingSpinner.getValue();
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

                    if (newRating != selectedRating) {
                        JOptionPane.showMessageDialog(rootPane,
                                selectedTitle+"'s rating has been changed from ["
                                        +selectedRating+"] to ["+newRating+"]");
                        //If selection is changed, display message
                    }
                }

                updateTable();
                //Update table data

                upcTextField.setText("");
                titleTextField.setText("");
                directorTextField.setText("");
                genreSpinner.setValue("Action");
                yearTextField.setText(String.valueOf(""));
                ratingSpinner.setValue(1);
                //Clear text/spinner fields
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