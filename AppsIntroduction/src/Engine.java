import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class Engine {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String database = "minions_db";
    private  Connection connection;
    private  PreparedStatement statement;
    private Properties props;
    private Scanner scanner;

    public Engine (Properties props)  {
        this.props = props;
        this.scanner = new Scanner(System.in);
    }

    public void run() throws SQLException {
        this.connection = DriverManager.getConnection(URL + database, props);
        DatabaseManipulator manipulator = new DatabaseManipulator(connection);
        System.out.println("Hello dear colleague. I hope this program finds you well.");
        System.out.println("Choose the number of the task you want to test(if you want to exit press 0): ");
        String task = scanner.nextLine();

        while (!task.equals("0")) {

            switch (task) {
                case "1" : {
                    manipulator.testConnection();
                    break;
                }
                case "2" : {
                    manipulator.getVilliansNames();
                    break;
                }
                case "3": {
                  manipulator.getMinionsNames(scanner);
                    break;
                }
                case "4": {
                   manipulator.addMinion(scanner);
                    break;
                }
                case "5": {
                  manipulator.changeTownNameCasing(scanner);
                    break;
                }
                case "7": {
                   manipulator.printAllMinionNames();
                    break;
                }
                case "8" : {
                   manipulator.increaseMinionsAge(scanner);
                    break;
                }
                case "9": {
                    manipulator.increaseAge(scanner);
                    break;
                }
                default: {
                    System.out.println("Wrong selection.");
                    break;
                }
            }
            System.out.println("Choose next task(if you want to exit press 0): ");
            task = scanner.nextLine();
        }

    }

}
