package json.cardealer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import json.cardealer.utils.FilesUtil;
import json.cardealer.utils.FilesUtilImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.Scanner;

@Configuration
public class AppBeanConfiguration {

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public FilesUtil filesUtil() {
        return new FilesUtilImpl();
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @Bean
    public Random random() {
        return new Random();
    }
}
