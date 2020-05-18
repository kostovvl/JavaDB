package productshop.services.impl;

import productshop.config.Constants;
import productshop.domain.dtos.PrintCategoryDto;
import productshop.domain.dtos.SeedCategoryDto;
import productshop.domain.entities.Category;
import productshop.domain.entities.Product;
import productshop.repositories.CategoryRepository;
import productshop.services.CategoryService;
import productshop.utils.FileUtil;
import productshop.utils.ValidatorUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final Gson gson;
    private final FileUtil fileUtil;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               ModelMapper modelMapper, ValidatorUtil validator, Gson gson, FileUtil fileUtil) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.validatorUtil = validator;
        this.gson = gson;
        this.fileUtil = fileUtil;
    }


    @Override
    public void seedCategories() throws IOException {
        if (this.categoryRepository.count() != 0) {
            return;
        }

        SeedCategoryDto[] seedCategoryDtos = this.gson.fromJson(this.fileUtil.readFileContent(Constants.CATEGORIES_URL),
                SeedCategoryDto[].class);

        Arrays.stream(seedCategoryDtos).forEach(seedCategoryDto -> {
            if (this.validatorUtil.validate(seedCategoryDto)) {
                this.categoryRepository.saveAndFlush(this.modelMapper.map(seedCategoryDto, Category.class));
            } else {
                this.validatorUtil.getViolations(seedCategoryDto)
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        });
    }

    @Override
    public void findAllCategories() throws IOException {
        List<Category> categories = this.categoryRepository.findAllCategories();
        List<PrintCategoryDto> printCategoryDtos = new ArrayList<>();

        for (Category category : categories) {
            PrintCategoryDto printCategoryDto = new PrintCategoryDto();
            printCategoryDto.setName(category.getName());
            printCategoryDto.setCount(category.getProducts().size());
            BigDecimal totalRevenue = new BigDecimal("0");
            for (Product product : category.getProducts()) {
              totalRevenue = totalRevenue.add(product.getPrice());
            }
            BigDecimal avgRevenue =
                    totalRevenue.divide(new BigDecimal(String.valueOf(category.getProducts().size())), 1);
            printCategoryDto.setAveragePrice(avgRevenue);
            printCategoryDto.setTotalRevenue(totalRevenue);

            printCategoryDtos.add(printCategoryDto);
        }

        this.fileUtil.writeFile(gson.toJson(printCategoryDtos), Constants.CATEGORIES__URL);

    }
}
