package project.utils;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileUtilImpl implements FileUtil {


    @Override
    public String[] readFile(String fileLocation) throws IOException {

        File file = new File(fileLocation);

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String row;

        List<String> result = new ArrayList<>();
        while ((row = reader.readLine()) != null) {
            if (!"".equals(row)) {
            result.add(row);
            }
        }
        return result.toArray(String[]::new);
    }
}
