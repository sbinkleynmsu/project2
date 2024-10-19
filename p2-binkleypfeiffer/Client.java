import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Client {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/abcmediabinkleypfeiffer";
    private static final String USER = "root";
    private static final String PASSWORD = "Lcj703922!";

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int arg0 = Integer.parseInt(args[0]);

        try {
            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the MySQL/MariaDB server successfully.");

            // Create a statement object for executing SQL queries
            statement = connection.createStatement();

            if(arg0 == 5){
                String selectQuery = "With TempTable(AdId, THours) AS (SELECT empId, SUM(hours) FROM AdmWorkHours GROUP BY empId) SELECT " +
                                     "Administrator.empId, Administrator.name, Temptable.THours FROM Administrator, TempTable WHERE Administrator.empId = TempTable.AdId ORDER BY TempTable.THours ASC";
                resultSet = statement.executeQuery(selectQuery);

                while (resultSet.next()) {
                    int id = resultSet.getInt("empId");
                    String name = resultSet.getString("name");
                    double hours = resultSet.getDouble("Thours");

                    System.out.println("ID: " + id + ", Name: " + name +
                                       ", Total Hours: " + hours);
                }
            }

            if(arg0 == 6){
                if(args.length != 2){
                    throw new IOException("INCORRECT USAGE ARGUMENTS SHOULD BE: \"6 <model number> \" ");
                }else{
                    String modelNo = args[1].replace("'", "");
                    PreparedStatement prepState = connection.prepareStatement("SELECT TechnicalSupport.name FROM TechnicalSupport, Specializes WHERE Specializes.modelNo = ? " +
                                         "AND Specializes.empId = TechnicalSupport.empId");
                    prepState.setString(1, modelNo);
                    resultSet = prepState.executeQuery();

                    while (resultSet.next()) {
                        String name = resultSet.getString("name");

                        System.out.println("Name: " + name);
                    }
                }
            }

            if(arg0 == 7){
                String selectQuery = "WITH TempTable(SId, avgComRate) AS (SELECT empId, AVG(commissionRate) FROM Purchases GROUP BY empId) SELECT Salesman.name," + 
                                    " TempTable.avgComRate FROM Salesman, Temptable WHERE TempTable.SId = Salesman.empId ORDER BY avgComRate DESC";
                resultSet = statement.executeQuery(selectQuery);

                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    double comRate = resultSet.getDouble("avgComRate");

                    System.out.println("Name: " + name + ", Average Commission Rate: " + comRate);
                }
            }

            if(arg0 == 8){
                int emptySet = 0;
                String selectQuery = "WITH Admins AS (SELECT COUNT(*) as AdminCt FROM Administrator), SMen " + 
                                     "AS (SELECT COUNT(*) AS SalesCt FROM Salesman), TechSupp AS (SELECT COUNT(*) AS TechCt FROM TechnicalSupport) " + 
                                     "SELECT * FROM Admins, SMen, TechSupp";
                resultSet = statement.executeQuery(selectQuery);

                if(resultSet.next()){
                    int adminCt = resultSet.getInt("AdminCt");
                    int salesCt = resultSet.getInt("SalesCt");
                    int techCt = resultSet.getInt("TechCt");
                
                    System.out.printf("%-15s     %6s\n", "Role", "ct");
                    System.out.println("----------------------------");
                    System.out.printf("%-15s     |%6d\n", "Administrators", adminCt);
                    System.out.printf("%-15s     |%6d\n", "Salesmen", salesCt);
                    System.out.printf("%-15s     |%6d\n", "Technicians", techCt);
                    emptySet = 1;
                }

                if(emptySet == 0){
                    System.out.println("No Results Found");
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        finally {
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
