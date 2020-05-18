package xmlprocessing.productshop.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import xmlprocessing.productshop.config.Constants;
import xmlprocessing.productshop.domain.dtos.productsinrangedto.ProductInRangeDto;
import xmlprocessing.productshop.domain.dtos.productsinrangedto.ProductsInRangeDto;
import xmlprocessing.productshop.domain.dtos.seedproductdto.SeedProductDto;
import xmlprocessing.productshop.domain.dtos.seedproductdto.SeedProductsDto;
import xmlprocessing.productshop.domain.entities.Category;
import xmlprocessing.productshop.domain.entities.Product;
import xmlprocessing.productshop.domain.entities.User;
import xmlprocessing.productshop.repositories.CategoryRepository;
import xmlprocessing.productshop.repositories.ProductRepository;
import xmlprocessing.productshop.repositories.UserRepository;
import xmlprocessing.productshop.services.ProductService;
import xmlprocessing.productshop.utils.ValidatorUtil;
import xmlprocessing.productshop.utils.XMLParser;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final XMLParser parser;
    private final ValidatorUtil validatorUtil;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final Random random;

    public ProductServiceImpl(XMLParser parser, ValidatorUtil validatorUtil,
                              ProductRepository productRepository,
                              CategoryRepository categoryRepository, UserRepository userRepository,
                              ModelMapper mapper, Random random) {
        this.parser = parser;
        this.validatorUtil = validatorUtil;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.random = random;
    }

    @Transactional
    @Override
    public void seedProducts() throws JAXBException, FileNotFoundException {
        if (this.productRepository.count() != 0) {
            return;
        }
        SeedProductsDto seedProductsDto =
                this.parser.importFromXml(SeedProductsDto.class, Constants.PRODUCTS_URL);
        for (int i = 0; i < seedProductsDto.getProducts().size(); i++) {
            SeedProductDto dto = seedProductsDto.getProducts().get(i);
            if (this.validatorUtil.isValid(dto)) {
                seedProduct(dto, i);
            } else {
                this.validatorUtil.getViolations(dto).stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        }
    }

    private void seedProduct(SeedProductDto dto, int i) {
        Product product = this.mapper.map(dto, Product.class);
        product.setCategories(generateRandomCategories());
        product.setSeller(getRandomUser(i));
        if (i % 5 != 0) {
            product.setBuyer(getRandomUser(i));
        }
        this.productRepository.saveAndFlush(product);
    }

    private Set<Category> generateRandomCategories() {
        Random random = new Random();
        Set<Category> categories = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            int cateoryID = random.nextInt(11) + 1;
            categories.add(this.categoryRepository.findById((long)cateoryID).orElse(null));
        }
        return categories;
    }

    private User getRandomUser(int i) {
        Random random = new Random();
        int userId = random.nextInt((int)this.userRepository.count()) + 1;
        return this.userRepository.getOne((long)userId);
    }

    @Override
    public void writeProductsInRange() throws JAXBException {
        BigDecimal low = new BigDecimal("500");
        BigDecimal high = new BigDecimal("1000");
        List<Product> products = this.productRepository.findProductsInRange(low, high);
        List<ProductInRangeDto> productsInRange = products.stream()
                .map(p -> {
                    ProductInRangeDto result = this.mapper.map(p, ProductInRangeDto.class);
                    result.setSeller(p.getSeller().getFirstName() + p.getSeller().getLastName());
                    return result;
                }).collect(Collectors.toList());
        ProductsInRangeDto productsInRangeDto = new ProductsInRangeDto();
        productsInRangeDto.setProducts(productsInRange);

        this.parser.exportToXML(productsInRangeDto, Constants.PRODUCTS_IN_RANGE);
    }


}
