package softuni.exam.util;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface XmlParser {
    <T> T importFromXml(Class<T> klass, String path) throws JAXBException, FileNotFoundException;
}
