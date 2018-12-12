package VHS_database;

import java.sql.*;
import java.util.Vector;

public class VHSDatabase {

    private static final String DB_CONNECTION_URL = "jdbc:sqlite:databases/VHS.sqlite";

    private static final String ID_COLUMN = "id";             //Primary key column. Each VHS will have a unique ID.
    private static final String TITLE_COLUMN = "title";
    private static final String YEAR_COLUMN = "year_released";
    private static final String RATING_COLUMN = "rating";

    final static int VHS_MIN_RATING = 1;
    final static int VHS_MAX_RATING = 5;

    // SQL statements
    private static final String CREATE_VHS_TABLE = "CREATE TABLE IF NOT EXISTS VHS_reviews (id INTEGER PRIMARY KEY , title TEXT, year_released INTEGER, rating INTEGER)";

    private static final String GET_ALL_VHS = "SELECT * FROM VHS_reviews";
    private static final String EDIT_VHS_RATING = "UPDATE VHS_reviews SET rating = ? WHERE ID = ?";
    private static final String DELETE_VHS = "DELETE FROM VHS_reviews WHERE ID = ?";
    private static final String ADD_VHS = "INSERT INTO VHS_reviews (title, year_released, rating) VALUES (?, ?, ?) ";


    VHSDatabase() {

        createTable();
    }


    private void createTable() {

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(CREATE_VHS_TABLE);
            System.out.println("Created VHS reviews table");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    Vector getColumnNames() {

        Vector colNames = new Vector();
        colNames.add("id");
        colNames.add("Title");
        colNames.add("Year Released");
        colNames.add("Rating");

        return colNames;
    }


    Vector<Vector> getAllVHS() {

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery(GET_ALL_VHS);

            Vector<Vector> vectors = new Vector<>();

            String name;
            int id, year, rating;

            while (rs.next()) {

                id = rs.getInt(ID_COLUMN);
                name = rs.getString(TITLE_COLUMN);
                year = rs.getInt(YEAR_COLUMN);
                rating = rs.getInt(RATING_COLUMN);

                Vector v = new Vector();
                v.add(id); v.add(name); v.add(year); v.add(rating);

                vectors.add(v);
            }

            return vectors;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void deleteVHS(int VHSID) {

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_VHS)) {

            preparedStatement.setInt(1, VHSID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void addVHS(String title, int year, int rating) {

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_VHS)) {

            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, year);
            preparedStatement.setInt(3, rating);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void changeRating(int id, int rating) {

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(EDIT_VHS_RATING)) {

            preparedStatement.setInt(1, rating);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}