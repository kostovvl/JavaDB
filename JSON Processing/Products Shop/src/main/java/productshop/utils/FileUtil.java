package productshop.utils;

import java.io.IOException;

public interface FileUtil {
    String readFileContent(String filePath) throws IOException;

    void writeFile(String content, String filePath) throws IOException;
}
