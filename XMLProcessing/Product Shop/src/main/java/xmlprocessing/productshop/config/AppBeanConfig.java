package xmlprocessing.productshop.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xmlprocessing.productshop.utils.ValidatorUtil;
import xmlprocessing.productshop.utils.ValidatorUtilImpl;
import xmlprocessing.productshop.utils.XMLParser;
import xmlprocessing.productshop.utils.XMLParserImpl;

import javax.xml.validation.Validator;
import java.util.Random;

@Configuration
public class AppBeanConfig {

    @Bean
    public ValidatorUtil validatorUtil() {
        return new ValidatorUtilImpl();
    }

    @Bean
    public XMLParser xmlParser() {
        return new XMLParserImpl();
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
