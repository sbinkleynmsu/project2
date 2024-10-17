import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Client {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3301/abcmediabinkleypfeiffer";
    private static final String USER = "root";
    private static final String PASSWORD = "218023.Copiper$1";

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the MySQL/MariaDB server successfully.");

            // Create a statement object for executing SQL queries
            statement = connection.createStatement();

            // Example DDL query: Create table if not exists
            String ddlQuery = "CREATE TABLE IF NOT EXISTS employees (" +
                              "id INT AUTO_INCREMENT PRIMARY KEY, " +
                              "name VARCHAR(255) NOT NULL, " +
                              "position VARCHAR(100), " +
                              "salary DECIMAL(10, 2), " +
                              "hire_date DATE)";
            statement.executeUpdate(ddlQuery);
            System.out.println("Table 'employees' created successfully.");

            // Example DML query: Insert data into the table
            String dmlQuery = "INSERT INTO employees (name, position, salary, hire_date) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(dmlQuery);
            
            // Inserting multiple rows using batch processing
            preparedStatement.setString(1, "John Doe");
            preparedStatement.setString(2, "Software Developer");
            preparedStatement.setDouble(3, 70000);
            preparedStatement.setDate(4, java.sql.Date.valueOf("2024-01-15"));
            preparedStatement.addBatch();

            preparedStatement.setString(1, "Jane Smith");
            preparedStatement.setString(2, "Project Manager");
            preparedStatement.setDouble(3, 85000);
            preparedStatement.setDate(4, java.sql.Date.valueOf("2023-10-10"));
            preparedStatement.addBatch();

            preparedStatement.setString(1, "Sam Wilson");
            preparedStatement.setString(2, "Designer");
            preparedStatement.setDouble(3, 50000);
            preparedStatement.setDate(4, java.sql.Date.valueOf("2022-05-30"));
            preparedStatement.addBatch();

            int[] affectedRows = preparedStatement.executeBatch();
            System.out.println("Inserted " + affectedRows.length + " rows successfully.");

            // Example DML query: Select data from the table
            String selectQuery = "SELECT * FROM employees";
            resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String position = resultSet.getString("position");
                double salary = resultSet.getDouble("salary");
                java.sql.Date hireDate = resultSet.getDate("hire_date");

                System.out.println("ID: " + id + ", Name: " + name + ", Position: " + position +
                                   ", Salary: " + salary + ", Hire Date: " + hireDate);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
