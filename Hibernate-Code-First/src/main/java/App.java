import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class App {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("code_first");
        EntityManager manager = emf.createEntityManager();

        Engine engine = new Engine(manager);
        engine.run();
    }
}
