import java.sql.*;
import java.util.*;

public class DatabaseManipulator {
    private PreparedStatement statement;
    private Connection connection;
    public DatabaseManipulator (Connection connection) {
        this.connection = connection;
    }

    public void testConnection() throws SQLException {
        this.statement = connection.prepareStatement("select * from minions;");
        ResultSet resultSet = statement.executeQuery();

        System.out.println("Task 1:");
        if (resultSet.next()) {
            System.out.println("Connection successful!");
        } else {
            System.out.println("Connection unsuccessful!");
        }
        System.out.println("End of task 1\n **************************************");
    }

    public void getVilliansNames() throws SQLException {
        this.statement = connection.prepareStatement("select v.name as name, count(m.id) as minions_count from villains as v\n" +
                "join minions_villains as mv\n" +
                "on v.id = mv.villain_id\n" +
                "join minions as m\n" +
                "on m.id = mv.minion_id\n" +
                "group by v.name\n" +
                "having minions_count > 15\n" +
                "order by minions_count desc;");
        ResultSet resultSet = statement.executeQuery();

        System.out.println("Task 2: ");
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            int minionsCount = resultSet.getInt("minions_count");

            System.out.printf("%-10.10s|%d%n", name, minionsCount);
        }
        System.out.println("End of task 2\n **************************************");
    }

    public void getMinionsNames(Scanner scanner) throws SQLException {
        statement = connection.prepareStatement("select v.name as name, m.name as minions_name, m.age as minion_age from villains as v\n" +
                "                join minions_villains as mv\n" +
                "                on v.id = mv.villain_id\n" +
                "                join minions as m\n" +
                "                on m.id = mv.minion_id\n" +
                "                where v.id = ?;");
        System.out.println("Task 3:");
        System.out.println("Choose villain Id: ");
        int villainId = Integer.parseInt(scanner.nextLine());
        statement.setInt(1, villainId);

        printVillainAndMinions(statement, villainId);
        System.out.println("End of task 3\n **************************************");
    }

    public void addMinion(Scanner scanner) throws SQLException {
        System.out.println("Task 4: ");
        System.out.println("Minion: ");
        String[] minionArr = scanner.nextLine().split(" ");
        System.out.println("Villain: ");
        String villain = scanner.nextLine();
        String minionName = minionArr[0];
        int minionAge = Integer.parseInt(minionArr[1]);
        String minionCity = minionArr[2];

        if (!checkIfEntityExists(minionCity, "towns")) {
            createTown(minionCity);
            System.out.printf("Town %s was added to the database.%n", minionCity);
        }

        if (!checkIfEntityExists(villain, "villains")) {
            createVillain(villain);
            System.out.printf("Villain %s was added to the database.%n", villain);
        }


        addMinionToVillain(villain, minionName, minionAge, minionCity);
        System.out.printf("Successfully added %s to be minion of %s%n", minionName, villain);
        System.out.println("End of task 4\n **************************************");
    }

    public void changeTownNameCasing(Scanner scanner) throws SQLException {
        System.out.println("Task 5:");
        System.out.println("Country: ");
        String country = scanner.nextLine();
        int numberOfAffectedTowns = extractCountOfTowns(country);
        if (numberOfAffectedTowns != 0) {
            System.out.printf("%d town names were affected.%n", numberOfAffectedTowns);
            changeTownNames(country);
            printTownNames(country);
        } else {
            System.out.println("No town names were affected.");
        }
        System.out.println("End of task 5\n **************************************");
    }

    public void printAllMinionNames() throws SQLException {
       this.statement = connection.prepareStatement("select name from minions");
        ResultSet resultSet = statement.executeQuery();
        ArrayDeque<String> rawResult = new ArrayDeque<>();
        System.out.println("Task 7:");
        while (resultSet.next()) {
            rawResult.offerLast(resultSet.getString("name"));
        }
        List<String> polishedResult = new ArrayList<>();
        int counter = 1;

        while (!rawResult.isEmpty()) {
            if (counter % 2 != 0) {
                polishedResult.add(rawResult.removeFirst());
            } else {
                polishedResult.add(rawResult.removeLast());
            }
            counter++;
        }
        polishedResult.forEach(System.out::println);
        System.out.println("End of task 7\n **************************************");
    }

    public void increaseMinionsAge(Scanner scanner) throws SQLException {
        System.out.println("Task 8: \n " +
                "!!NB please note that here you change only the printed result the entities in the DB are not affected!!");
        System.out.println("Enter minions Id's: ");
        int[] arr = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        Set<Integer>ids = new HashSet<>();
        for (int i : arr) {
            ids.add(i);
        }
        printAllMinionsNameAndAge(ids);
        System.out.println("End of task 8\n **************************************");
    }


    public void increaseAge(Scanner scanner) throws SQLException {
        System.out.println("Task 9: ");
        System.out.println("Enter minion id: ");
        int minionInd = Integer.parseInt(scanner.nextLine());
        increaseTheAge(minionInd);
        printTheSpecifiedMinion(minionInd);
        System.out.println("End of task 9\n **************************************");
    }






    //Private methods start from here:...
    private static void printVillainAndMinions(PreparedStatement statement, int villainInd) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            String villainName = resultSet.getString("name");
            System.out.printf("Villain: %s%n", villainName);
            int number = 1;
            resultSet.previous();
            while (resultSet.next()) {
                String minionName = resultSet.getString("minions_name");
                int minionAge = resultSet.getInt("minion_age");
                System.out.printf("%d. %s %d%n", number, minionName, minionAge);
                number++;
            }
        } else {
            System.out.printf("No villain with ID %d exists in the database.%n", villainInd);
        }
    }

    private boolean checkIfEntityExists(String entity, String table) throws SQLException {
        this.statement = connection.prepareStatement("select id from " + table + "\n" +
                "where name = ?;");
        statement.setString(1, entity);
        ResultSet result = statement.executeQuery();
        return result.next();
    }

    private void createVillain(String villain) throws SQLException {
        statement = connection.prepareStatement("insert into villains (`name`, `evilness_factor`) values (?, 'evil');");
        statement.setString(1, villain);
        statement.execute();
    }

    private void createTown(String minionCity) throws SQLException {
        statement = connection.prepareStatement("insert into towns(`name`) values (?);");
        statement.setString(1, minionCity);
        statement.execute();
    }

    private  void addMinionToVillain(String villain, String minionName, int minionAge, String minionCity) throws SQLException {
        int townId = getId("towns", minionCity);
        statement = connection.prepareStatement("insert into minions (`name`, `age`, `town_id`) values (?, ?, ?);");
        statement.setString(1, minionName);
        statement.setInt(2, minionAge);
        statement.setInt(3, townId);
        statement.execute();
        int minionId = getId("minions", minionName);
        int villainId = getId("villains", villain);
        statement = connection.prepareStatement("insert into minions_villains (`minion_id`, `villain_id`) values (?, ?);");
        statement.setInt(1, minionId);
        statement.setInt(2, villainId);
        statement.execute();
    }

    private int getId(String table, String entity) throws SQLException {
        statement = connection.prepareStatement("select id from " + table + " where name = ?");
        statement.setString(1, entity);
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getInt("id");
    }

    private int extractCountOfTowns(String country) throws SQLException {
        this.statement = connection.prepareStatement("\n" +
                "select count(id) as id from towns\n" +
                "where country = ?;");
        statement.setString(1, country);
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getInt("id");

    }

    private void printTownNames(String country) throws SQLException {
        this.statement = connection.prepareStatement("select name from towns where country = ?");
        statement.setString(1, country);
        ResultSet resultSet = statement.executeQuery();
        List<String> townNames = new ArrayList<>();
        while (resultSet.next()) {
            townNames.add(resultSet.getString("name"));
        }
        System.out.println("[" + String.join(", ", townNames) + "]");
    }


    private void changeTownNames(String country) throws SQLException {
        this.statement = connection.prepareStatement("update towns\n" +
                "set name = upper(name)\n" +
                "where country = ?;");
        statement.setString(1, country);
        statement.execute();
    }

    private  void printAllMinionsNameAndAge(Set<Integer> ids) throws SQLException {
        this.statement = connection.prepareStatement("select id, name, age from minions");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            if (ids.contains(resultSet.getInt("id"))) {
                System.out.printf("%s %d%n", resultSet.getString("name").toLowerCase(),
                        resultSet.getInt("age") + 1);
            } else {
                System.out.printf("%s %d%n", resultSet.getString("name"), resultSet.getInt("age"));
            }
        }
    }

    private void increaseTheAge(int minionInd) throws SQLException {
        statement = connection.prepareStatement("call usp_get_older(?)");
        statement.setInt(1, minionInd);
        statement.execute();
    }

    private void printTheSpecifiedMinion(int minionInd) throws SQLException {
        this.statement = connection.prepareStatement("select name, age from minions where id = ?");
        statement.setInt(1, minionInd);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        System.out.printf("%s, %d%n", resultSet.getString("name"), resultSet.getInt("age"));
    }

}
