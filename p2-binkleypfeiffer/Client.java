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

        int queryNumber;
        try {
            queryNumber = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid query number format. Please provide a valid number.");
            return;
        }

        String param = args.length > 1 ? args[1] : null;

        switch (queryNumber) {
            case 1 -> findSitesByStreet(param);
            case 2 -> findDisplaysByScheduler(param);
            case 3 -> listSalesmen();
            case 4 -> findClientsByPhone(param);
            default -> System.out.println("Invalid query number. Please provide a number between 1 and 4.");
        }
    }

    // Method to execute a query and handle results
    private static void executeQuery(String query, QueryHandler handler, String... params) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                preparedStatement.setString(i + 1, params[i]);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                System.out.println("No results found.");
                return;
            }
            handler.handle(resultSet);

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    //Question 1: Find sites by street
    public static void findSitesByStreet(String streetName) {
        if (streetName == null || streetName.isEmpty()) {
            System.out.println("Please provide a valid street name.");
            return;
        }

        String query = "SELECT * FROM Site WHERE address LIKE ?";
        executeQuery(query, resultSet -> {
            while (resultSet.next()) {
                System.out.printf("Site Code: %d, Type: %s, Address: %s, Phone: %s%n",
                        resultSet.getInt("siteCode"),
                        resultSet.getString("type"),
                        resultSet.getString("address"),
                        resultSet.getString("phone"));
            }
        }, "%" + streetName + "%");
    }

    //Question 2: Find digital displays by scheduler system
    public static void findDisplaysByScheduler(String schedulerSystem) {
        if (schedulerSystem == null || schedulerSystem.isEmpty()) {
            System.out.println("Please provide a valid scheduler system.");
            return;
        }

        String query = "SELECT DigitalDisplay.serialNo, DigitalDisplay.modelNo, Model.screenSize " +
                       "FROM DigitalDisplay " +
                       "JOIN Model ON DigitalDisplay.modelNo = Model.modelNo " +
                       "WHERE DigitalDisplay.schedulerSystem = ?";
        executeQuery(query, resultSet -> {
            while (resultSet.next()) {
                System.out.printf("Serial No: %s, Model No: %s, Screen Size: %.2f%n",
                        resultSet.getString("serialNo"),
                        resultSet.getString("modelNo"),
                        resultSet.getDouble("screenSize"));
            }
        }, schedulerSystem);
    }

    //Question 3: List distinct salesmen and their count
    public static void listSalesmen() {
        String query = "SELECT name, COUNT(*) as cnt FROM Salesman GROUP BY name ORDER BY name ASC";
        executeQuery(query, resultSet -> {
            while (resultSet.next()) {
                System.out.printf("Name: %s, Count: %d%n",
                        resultSet.getString("name"),
                        resultSet.getInt("cnt"));
            }
        });
    }

    //Question 4: Find clients by phone number
    public static void findClientsByPhone(String phoneNo) {
        if (phoneNo == null || phoneNo.isEmpty()) {
            System.out.println("Please provide a valid phone number.");
            return;
        }

        String query = "SELECT * FROM Client WHERE phone = ?";
        executeQuery(query, resultSet -> {
            while (resultSet.next()) {
                System.out.printf("Client ID: %d, Name: %s, Phone: %s, Address: %s%n",
                        resultSet.getInt("clientId"),
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getString("address"));
            }
        }, phoneNo);
    }

    // Functional interface for handling ResultSet
    @FunctionalInterface
    interface QueryHandler {
        void handle(ResultSet resultSet) throws SQLException;
    }
}
