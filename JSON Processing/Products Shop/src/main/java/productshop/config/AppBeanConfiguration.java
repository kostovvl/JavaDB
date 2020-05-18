package productshop.config;

import productshop.utils.FileUtil;
import productshop.utils.FileUtilImpl;
import productshop.utils.ValidatorUtil;
import productshop.utils.ValidatorUtilImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Scanner;

@Configuration
public class AppBeanConfiguration {

    @Bean
    Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean
    Gson gson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    }

    @Bean
    ModelMapper mapper() {
        return new ModelMapper();
    }

    @Bean
    ValidatorUtil validator() {
        return new ValidatorUtilImpl();
    }

    @Bean
    FileUtil fileUtil() {
        return new FileUtilImpl();
    }
}
