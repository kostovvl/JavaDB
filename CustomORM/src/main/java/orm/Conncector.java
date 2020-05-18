package orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conncector {
    private Connection connection;
    private Properties props;

    public Conncector(String user, String password) {
        this.props = new Properties();
        this.props.setProperty("user", user);
        this.props.setProperty("password", password);
    }

    public void createConnection(String dbName) throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, this.props);
    }

    public Connection getConnection() {
        return this.connection;
    }
}
