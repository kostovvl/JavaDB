package softuni.exam.util;

import java.io.IOException;

public interface FileUtils {
    String readFileContent(String filePath) throws IOException;

    void write(String content, String filePath) throws IOException;
}
