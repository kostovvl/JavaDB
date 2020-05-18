package xmlprocessing.cardealer.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xmlprocessing.cardealer.utils.ValidatorUtil;
import xmlprocessing.cardealer.utils.ValidatorUtilImpl;
import xmlprocessing.cardealer.utils.XmlParserUtil;
import xmlprocessing.cardealer.utils.XmlParserUtilImpl;

import java.util.Random;

@Configuration
public class AppBeanConfiguration {

    @Bean
    public ValidatorUtil validatorUtil() {
        return new ValidatorUtilImpl();
    }

    @Bean
    public XmlParserUtil xmlParserUtil() {
        return new XmlParserUtilImpl();
    }

    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }

    @Bean
    public Random random() {
        return new Random();
    }
}
