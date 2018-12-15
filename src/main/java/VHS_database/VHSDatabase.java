//VHS Database System - Jason

package VHS_database;

import java.math.BigInteger;
import java.sql.*;
import java.util.Calendar;
import java.util.Vector;



public class VHSDatabase {

//region [// VHSDatabase variables //]

    public static String APP_TITLE = "\uD83D\uDCFC VHS Database";
    //String variable for VHS Database title

    private static final String ID_COLUMN = "id";
    private static final String UPC_COLUMN = "upc";
    private static final String TITLE_COLUMN = "title";
    private static final String DIRECTOR_COLUMN = "director";
    private static final String GENRE_COLUMN = "genre";
    private static final String YEAR_COLUMN = "year_released";
    private static final String RATING_COLUMN = "rating";
    //String variables for VHS ID, UPC, Title, Director, Genre, Year & Rating columns.

    final static int VHS_MIN_YEAR = 1900;
    final static int VHS_MAX_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    //Integer variables for Min/Max film years

    final static int VHS_MIN_RATING = 1;
    final static int VHS_MAX_RATING = 5;
    //Integer variables for Min/Max film ratings

    final static int UPC_LENGTH = 12;
    //Integer variable for correct UPC length

    final static String[] genreList = new String[] {
            "Action","Adventure","Animation","Biography","Comedy","Crime","Documentary","Drama","Family",
            "Fantasy","History","Horror","Musical","Mystery","Romance","Sci-Fi","Sports","Superhero","Thriller",
            "War","Western"
    };
    //String list variable for film genres

//endregion


//region [// SQL statement string variables //]

    private static final String DB_CONNECTION_URL = "jdbc:sqlite:databases/VHS.sqlite";
    //String variable for VHS database connection

    private static final String CREATE_VHS_TABLE = "CREATE TABLE IF NOT EXISTS VHS_collection " +
            "(id INTEGER PRIMARY KEY, upc INTEGER, title TEXT, director TEXT, genre TEXT, year_released INTEGER, rating INTEGER)";
    //String variable to create VHS_collection

    private static final String GET_VHS = "SELECT * FROM VHS_collection";
    //String variable to get VHS from VHS_collection

    private static final String EDIT_VHS_UPC = "UPDATE VHS_collection SET upc = ? WHERE ID = ?";
    private static final String EDIT_VHS_TITLE = "UPDATE VHS_collection SET title = ? WHERE ID = ?";
    private static final String EDIT_VHS_DIRECTOR = "UPDATE VHS_collection SET director = ? WHERE ID = ?";
    private static final String EDIT_VHS_GENRE = "UPDATE VHS_collection SET genre = ? WHERE ID = ?";
    private static final String EDIT_VHS_YEAR = "UPDATE VHS_collection SET year_released = ? WHERE ID = ?";
    private static final String EDIT_VHS_RATING = "UPDATE VHS_collection SET rating = ? WHERE ID = ?";
    //String variables to edit film sets from VHS_collection

    private static final String DELETE_VHS = "DELETE FROM VHS_collection WHERE ID = ?";
    //String variable to delete VHS from VHS_collection

    private static final String ADD_VHS = "INSERT INTO VHS_collection " +
            "(upc, title, director, genre, year_released, rating) VALUES (?, ?, ?, ?, ?, ?) ";
    //String variable to add VHS info to VHS_collection

//endregion


//region [// Error message string variables //]

    public static String ERROR_NUMB = "//ERROR//: Field only accepts numeric data";
    public static String ERROR_NULL = "//ERROR//: Field cannot be null";
    public static String ERROR_SLCT = "//ERROR//: Field requires selection";
    public static String ERROR_DBCR = "//ERROR//: Unable to create VHS database";
    public static String ERROR_DBCN = "//ERROR//: Unable to connect to VHS Database";
    //String variables for error messages

    public static String YIELD_YEAR = " between "+VHS_MIN_YEAR+" - "+VHS_MAX_YEAR;
    public static String YIELD_RATING = " between "+VHS_MIN_RATING+" - "+VHS_MAX_RATING;
    public static String YIELD_UPC = " & must be "+UPC_LENGTH+" characters";
    //String variables for number yield error messages

//endregion



    VHSDatabase() {

        createTable();
        //Create VHS collection table

    }



    private void createTable() {
        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             Statement statement = connection.createStatement()) {
            //Try connecting to VHS database

            statement.executeUpdate(CREATE_VHS_TABLE);
            //Try to create VHS collection table

        } catch (SQLException ERROR) {
            System.out.println(ERROR_DBCR);
            throw new RuntimeException(ERROR);
            //Catch errors/display error message
        }
    }



    Vector getColumnNames() {

        Vector colNames = new Vector();
        //Vector variable for VHS collection column names

        colNames.add("ID");
        colNames.add("UPC");
        colNames.add("Title");
        colNames.add("Director");
        colNames.add("Genre");
        colNames.add("Year Released");
        colNames.add("Rating");
        //Add column names for VHS ID, UPC, Title, Director, Genre, Year Released & Ratings

        return colNames;
        //Return column names
    }



    Vector<Vector> getAllVHS() {

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             Statement statement = connection.createStatement()) {
            //Connect to VHS database

            ResultSet rs = statement.executeQuery(GET_VHS);
            //ResultSet variable for SQL statement to get VHS tapes

            Vector<Vector> vhsList = new Vector<>();
            //Vector list variable for required vectors

            String upc, name, director, genre;
            //String variables for VHS UPC, name, director & genre

            int id, year, rating;
            //Integer variables for VHS id, year & rating

            while (rs.next()) {

                id = rs.getInt(ID_COLUMN);
                upc = rs.getString(UPC_COLUMN);
                name = rs.getString(TITLE_COLUMN);
                director = rs.getString(DIRECTOR_COLUMN);
                genre = rs.getString(GENRE_COLUMN);
                year = rs.getInt(YEAR_COLUMN);
                rating = rs.getInt(RATING_COLUMN);
                //Get each VHS

                Vector vhs = new Vector();
                //Vector variable for each VHS


                vhs.add(id); vhs.add(upc); vhs.add(name); vhs.add(director); vhs.add(genre); vhs.add(year); vhs.add(rating);
                //Add info to vector for each VHS

                vhsList.add(vhs);
                //Add each VHS vector to VHS vector list
            }

            return vhsList;
            //Return VHS vector list

        } catch (SQLException ERROR) {
            System.out.println(ERROR_DBCN);
            throw new RuntimeException(ERROR);
            //Catch errors/display error message
        }

    }



    public void addVHS(String upc, String title, String director, String genre, int year, int rating) {
        //Add VHS

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_VHS)) {

            preparedStatement.setString(1, upc);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, director);
            preparedStatement.setString(4, genre);
            preparedStatement.setInt(5, year);
            preparedStatement.setInt(6, rating);

            preparedStatement.executeUpdate();

        } catch (SQLException ERROR) {
            System.out.println(ERROR_DBCN+" when adding VHS");
            throw new RuntimeException(ERROR);
        }
    }



    public void deleteVHS(int id) {
        //Delete VHS

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_VHS)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException ERROR) {
            System.out.println(ERROR_DBCN+" while deleting VHS");
            throw new RuntimeException(ERROR);
        }
    }



    public void changeUPC(int id, String upc) {
        //Change VHS UPC number

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(EDIT_VHS_UPC)) {

            preparedStatement.setString(1, upc);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();

        } catch (SQLException ERROR) {
            System.out.println(ERROR_DBCN+" while changing VHS UPC number");
            throw new RuntimeException(ERROR);
        }
    }



    public void changeTitle(int id, String title) {
        //Change VHS title

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(EDIT_VHS_TITLE)) {

            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();

        } catch (SQLException ERROR) {
            System.out.println(ERROR_DBCN+" while changing VHS title");
            throw new RuntimeException(ERROR);
        }
    }



    public void changeDirector(int id, String director) {
        //Change VHS director

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(EDIT_VHS_DIRECTOR)) {

            preparedStatement.setString(1, director);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();

        } catch (SQLException ERROR) {
            System.out.println(ERROR_DBCN+" while changing VHS director");
            throw new RuntimeException(ERROR);
        }
    }



    public void changeGenre(int id, String genre) {
        //Change VHS genre

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(EDIT_VHS_GENRE)) {

            preparedStatement.setString(1, genre);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();

        } catch (SQLException ERROR) {
            System.out.println(ERROR_DBCN+" while changing VHS genre");
            throw new RuntimeException(ERROR);
        }
    }



    public void changeRating(int id, int rating) {
        //Change VHS rating

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(EDIT_VHS_RATING)) {

            preparedStatement.setInt(1, rating);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();

        } catch (SQLException ERROR) {
            System.out.println(ERROR_DBCN+" while changing VHS rating");
            throw new RuntimeException(ERROR);
        }
    }



    public void changeYear(int id, int year) {
        //Change VHS year

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(EDIT_VHS_YEAR)) {

            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();

        } catch (SQLException ERROR) {
            System.out.println(ERROR_DBCN+" while changing VHS year");
            throw new RuntimeException(ERROR);
        }
    }
}