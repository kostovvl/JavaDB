import org.w3c.dom.ls.LSOutput;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String database = "minions_db";

    public static Connection connection;

    public static void main(String[] args) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "root");

        connection = DriverManager.getConnection(URL + database, props);
        Engine engine = new Engine(props);
        engine.run();

    }
}
