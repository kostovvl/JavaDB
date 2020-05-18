package productshop.services.impl;

import productshop.config.Constants;
import productshop.domain.dtos.ProductInRangeDto;
import productshop.domain.dtos.SeedProductDto;
import productshop.domain.dtos.SellerDto;
import productshop.domain.entities.Category;
import productshop.domain.entities.Product;
import productshop.domain.entities.User;
import productshop.repositories.CategoryRepository;
import productshop.repositories.ProductRepository;
import productshop.repositories.UserRepository;
import productshop.services.ProductService;
import productshop.utils.FileUtil;
import productshop.utils.ValidatorUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    //Знам, че не е най-правилното нещо да качвам други репозиторита в конкретния сървис, но така си
    //спестявам много booilerPlate код
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final Gson gson;
    private final FileUtil fileUtil;

    public ProductServiceImpl(ProductRepository productRepository,
                              UserRepository userRepository, CategoryRepository categoryRepository, ModelMapper modelMapper,
                              ValidatorUtil validator, Gson gson, FileUtil fileUtil) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.validatorUtil = validator;
        this.gson = gson;
        this.fileUtil = fileUtil;
    }

    @Override
    @Transactional
    public void seedProducts() throws IOException {
        if (this.productRepository.count() != 0) {
            return;
        }
        SeedProductDto[] seedProductDtos = this.gson.fromJson(this.fileUtil.readFileContent(Constants.PRODUCTS_URL),
                SeedProductDto[].class);

        for (int i = 0; i < seedProductDtos.length; i++) {
            SeedProductDto seedProductDto = seedProductDtos[i];
            if (this.validatorUtil.validate(seedProductDto)) {
                seedProduct(seedProductDto, i);
            } else {
                this.validatorUtil.getViolations(seedProductDto).stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        }
    }

    @Override
    public void getProductsInRange(String low, String high) throws IOException {
        BigDecimal lowPrice = new BigDecimal(low);
        BigDecimal highPrice = new BigDecimal(high);

        List<Product> products = this.productRepository.findProductInPriceRange(lowPrice, highPrice);
        ProductInRangeDto[] productInRangeDtos = products.stream().
                map(p -> {
                    ProductInRangeDto product = this.modelMapper.map(p, ProductInRangeDto.class);
                    SellerDto sellerDto = new SellerDto();
                    String name = p.getSeller().getFirstName() + " " + p.getSeller().getLastName();
                    System.out.println();
                    sellerDto.setName(name);
                    product.setSellerDto(sellerDto);
                    return product;
                })
                .toArray(ProductInRangeDto[]::new);
        String content = gson.toJson(productInRangeDtos);
        this.fileUtil.writeFile(content, Constants.PRODUCTS_IN_RANGE_URL);
    }


    private void seedProduct(SeedProductDto seedProductDto, int i) {
        Product product = this.modelMapper.map(seedProductDto, Product.class);
        if (i % 5 != 0) {
            product.setBuyer(getRandomUser(i));

        }
        product.setSeller(getRandomUser(i));
        product.setCategories(generateRandomCategories());
        System.out.println();
        this.productRepository.saveAndFlush(product);
    }

    private Set<Category> generateRandomCategories() {
        Random random = new Random();
        Set<Category> categories = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            int cateoryID = random.nextInt(11) + 1;
            categories.add(this.categoryRepository.getOne((long)cateoryID));
        }
        return categories;
    }

    private User getRandomUser(int i) {
        Random random = new Random();
        int userId = random.nextInt((int)this.userRepository.count()) + 1;
        return this.userRepository.getOne((long)userId);
    }
}
