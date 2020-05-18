package xmlprocessing.productshop.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xmlprocessing.productshop.config.Constants;
import xmlprocessing.productshop.domain.dtos.categoriesdto.CategoriesDto;
import xmlprocessing.productshop.domain.dtos.categoriesdto.CategoryDto;
import xmlprocessing.productshop.domain.dtos.seedcategorydto.SeedCategoriesDto;
import xmlprocessing.productshop.domain.dtos.seedcategorydto.SeedCategoryDto;
import xmlprocessing.productshop.domain.entities.Category;
import xmlprocessing.productshop.domain.entities.Product;
import xmlprocessing.productshop.repositories.CategoryRepository;
import xmlprocessing.productshop.services.CategoryService;
import xmlprocessing.productshop.utils.ValidatorUtil;
import xmlprocessing.productshop.utils.XMLParser;

import javax.validation.ConstraintViolation;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final XMLParser xmlParser;
    private final ValidatorUtil validatorUtil;
    private final ModelMapper mapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               XMLParser xmlParser, ValidatorUtil validatorUtil, ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.xmlParser = xmlParser;
        this.validatorUtil = validatorUtil;
        this.mapper = mapper;
    }

    @Override
    public void seedCategories() throws JAXBException, FileNotFoundException {
        if (categoryRepository.count() != 0) {
            return;
        }
        SeedCategoriesDto seedCategoriesDto =
                this.xmlParser.importFromXml(SeedCategoriesDto.class, Constants.CATEGORIES_URL);
        for (SeedCategoryDto dto : seedCategoriesDto.getSeedCategoryDtos()) {
            if (validatorUtil.isValid(dto)) {
                this.categoryRepository.saveAndFlush(this.mapper.map(dto, Category.class));
            } else {
                validatorUtil.getViolations(dto).stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        }

    }

    @Override
    public void writeCategoriesByProductCount() throws JAXBException {
        List<CategoryDto> categoriesDto = this.categoryRepository.findAll().stream()
                .map(c -> {
                    CategoryDto categoryDto = new CategoryDto();
                   categoryDto.setName(c.getName());
                    categoryDto.setProductsCount(c.getProducts().size());
                    BigDecimal totalRevenue = new BigDecimal("0");
                    for (Product product : c.getProducts()) {
                        totalRevenue = totalRevenue.add(product.getPrice());
                    }
                    BigDecimal avgRevenue =
                            totalRevenue.divide(new BigDecimal(String.valueOf(c.getProducts().size())), 1);
                 categoryDto.setAveragePrice(avgRevenue);
                categoryDto.setTotalRevenue(totalRevenue);
                return categoryDto;
                }).collect(Collectors.toList());
        CategoriesDto categoriesDto1 = new CategoriesDto();
        categoriesDto1.setCategories(categoriesDto);
        this.xmlParser.exportToXML(categoriesDto1, Constants.CATEGORIES_BY_PRODUCTS);
    }
}
