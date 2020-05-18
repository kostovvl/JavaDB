package productshop.utils;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class FileUtilImpl implements FileUtil {

    @Override
    public String readFileContent(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath))
                .stream()
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public void writeFile(String content, String filePath) throws IOException {
        Files.write(Paths.get(filePath), Collections.singleton(content), StandardCharsets.UTF_8);
    }
}
