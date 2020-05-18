
import entities.User;
import orm.Conncector;
import orm.EntityManager;

import java.sql.SQLException;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException {
        Conncector conncector = new Conncector("root", "root");
        conncector.createConnection("test_db");

        EntityManager<User> entityManager = new EntityManager<>(conncector.getConnection());
        User user = new User("Pesho", "1234", 26, new Date());
        entityManager.persist(user);
    }
}
