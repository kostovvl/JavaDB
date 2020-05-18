package hiberspring.util.impl;

import hiberspring.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class FileUtilImpl implements FileUtil {
    @Override
    public String readFile(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath)).stream()
                .filter(r -> !r.isEmpty())
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
