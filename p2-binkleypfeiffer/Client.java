import java.sql.*;

public class Client {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3301/abcmediabinkleypfeiffer";
    private static final String USER = "root";
    private static final String PASSWORD = "218023.Copiper$1";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a query number and necessary parameters.");
            return;
        }

        int queryNumber = Integer.parseInt(args[0]);
        String param = args.length > 1 ? args[1] : null;

        switch (queryNumber) {
            case 1:
                findSitesByStreet(param);
                break;
            case 2:
                findDisplaysByScheduler(param);
                break;
            case 3:
                listSalesmen();
                break;
            case 4:
                findClientsByPhone(param);
                break;
            default:
                System.out.println("Invalid query number.");
        }
    }

    // Method for Question 1: Find sites by street
    public static void findSitesByStreet(String streetName) {
        String query = "SELECT * FROM Site WHERE address LIKE ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, "%" + streetName + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Site Code: " + resultSet.getInt("siteCode") + 
                                   ", Type: " + resultSet.getString("type") + 
                                   ", Address: " + resultSet.getString("address") +
                                   ", Phone: " + resultSet.getString("phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method for Question 2: Find digital displays by scheduler system
    public static void findDisplaysByScheduler(String schedulerSystem) {
        String query = "SELECT DigitalDisplay.serialNo, DigitalDisplay.modelNo, Model.screenSize " +
                       "FROM DigitalDisplay " +
                       "JOIN Model ON DigitalDisplay.modelNo = Model.modelNo " +
                       "WHERE DigitalDisplay.schedulerSystem = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, schedulerSystem);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Serial No: " + resultSet.getString("serialNo") + 
                                   ", Model No: " + resultSet.getString("modelNo") + 
                                   ", Screen Size: " + resultSet.getDouble("screenSize"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method for Question 3: List distinct salesmen and their count
    public static void listSalesmen() {
        String query = "SELECT name, COUNT(*) as cnt FROM Salesman GROUP BY name ORDER BY name ASC";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int count = resultSet.getInt("cnt");
                System.out.println("Name: " + name + ", Count: " + count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method for Question 4: Find clients by phone number
    public static void findClientsByPhone(String phoneNo) {
        String query = "SELECT * FROM Client WHERE phone = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, phoneNo);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Client ID: " + resultSet.getInt("clientId") + 
                                   ", Name: " + resultSet.getString("name") + 
                                   ", Phone: " + resultSet.getString("phone") +
                                   ", Address: " + resultSet.getString("address"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}