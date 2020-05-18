package gamestore.demo.config;

import gamestore.demo.utils.ValidatorUtil;
import gamestore.demo.utils.ValidatorUtilImpl;
import org.hibernate.validator.internal.engine.ValidatorImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validator;
import java.util.Scanner;

@Configuration
public class AppBeanConfiguration {

    @Bean
    public Scanner getScanner() {
        return new Scanner(System.in);
    }

    @Bean
    public ModelMapper getMapper() {
        return new ModelMapper();
    }

    @Bean
    public ValidatorUtil getValidator() {
        return new ValidatorUtilImpl();
    }

}
