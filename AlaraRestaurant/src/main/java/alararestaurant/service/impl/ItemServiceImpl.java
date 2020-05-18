package alararestaurant.service.impl;

import alararestaurant.config.GlobalConstants;
import alararestaurant.domain.dtos.SeedItemsDto;
import alararestaurant.domain.entities.Category;
import alararestaurant.domain.entities.Item;
import alararestaurant.repository.CategoryRepository;
import alararestaurant.repository.ItemRepository;
import alararestaurant.service.ItemService;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class ItemServiceImpl implements ItemService {


    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final FileUtil reader;
    private final StringBuilder result;
    private final Gson gson;
    private final ModelMapper mapper;
    private final ValidationUtil validator;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, CategoryRepository categoryRepository,
                           FileUtil reader, StringBuilder result, Gson gson, ModelMapper mapper, ValidationUtil validator) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.reader = reader;
        this.result = result;
        this.gson = gson;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public Boolean itemsAreImported() {
       return this.itemRepository.count() > 0;
    }

    @Override
    public String readItemsJsonFile() throws IOException {
        return this.reader.readFile(GlobalConstants.ITEMS_URL);
    }

    @Override
    public String importItems(String items) throws IOException {
        SeedItemsDto[] seedItemsDtos = this.gson.fromJson(readItemsJsonFile(), SeedItemsDto[].class);

        for (SeedItemsDto itemDto : seedItemsDtos) {
            if (this.validator.isValid(itemDto)) {
                if (this.itemRepository.findByName(itemDto.getName()) == null) {
                    if (this.categoryRepository.findByName(itemDto.getCategory()) == null) {
                        seedCategoryInDb(itemDto.getCategory());
                    }
                    Category category = this.categoryRepository.findByName(itemDto.getCategory());
                    Item item = this.mapper.map(itemDto, Item.class);
                    item.setCategory(category);
                    this.itemRepository.saveAndFlush(item);
                    this.result.append(String.format("Record %s successfully imported.%n", itemDto.getName()));
                } else {
                    this.result.append("Already in DB.").append(System.lineSeparator());
                }
            } else {
                this.result.append("Invalid data format.").append(System.lineSeparator());
            }
        }


        return this.result.toString();
    }

    private void seedCategoryInDb(String category) {
        Category category1 = new Category();
        category1.setName(category);
        this.categoryRepository.saveAndFlush(category1);
    }
}
