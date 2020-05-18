package project.utils;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileUtil {
    String[] readFile(String fileLocation) throws IOException;
}
