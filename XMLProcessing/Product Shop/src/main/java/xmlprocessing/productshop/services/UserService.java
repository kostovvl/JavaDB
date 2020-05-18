package xmlprocessing.productshop.services;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface UserService {

    void seedUsers() throws JAXBException, FileNotFoundException;

    void writeUsersWithSuccessfullySoldProducts() throws JAXBException;

    void writeUsersWithSoldProducts() throws JAXBException;
}
