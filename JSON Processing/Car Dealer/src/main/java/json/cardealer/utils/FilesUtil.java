package json.cardealer.utils;

import java.io.IOException;

public interface FilesUtil {
    String readFileContent(String filePath) throws IOException;

    void write(String content, String filePath) throws IOException;
}
