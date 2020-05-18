package softuni.exam.util;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface XmlParser {
    <T> void exportToXML(T object, String path) throws JAXBException;

    <T> T importFromXml(Class<T> klass, String path) throws JAXBException, FileNotFoundException;
}
