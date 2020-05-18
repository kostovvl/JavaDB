package xmlprocessing.productshop.services;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface CategoryService {

    void seedCategories() throws JAXBException, FileNotFoundException;

    void writeCategoriesByProductCount() throws JAXBException;
}
