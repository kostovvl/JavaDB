package productshop.services.impl;

import productshop.config.Constants;
import productshop.domain.dtos.SeedUserDto;
import productshop.domain.dtos.userSoldProductsDto.SoldItemsUsersDto;
import productshop.domain.dtos.usersAndProductsDto.Buyer2Dto;
import productshop.domain.dtos.usersAndProductsDto.Product2Dto;
import productshop.domain.dtos.usersAndProductsDto.UsersAndProductsDto;
import productshop.domain.entities.User;
import productshop.repositories.UserRepository;
import productshop.services.UserService;
import productshop.utils.FileUtil;
import productshop.utils.ValidatorUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validator;
    private final Gson gson;
    private final FileUtil fileUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper, ValidatorUtil validator, Gson gson, FileUtil fileUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validator = validator;
        this.gson = gson;
        this.fileUtil = fileUtil;
    }


    @Override
    public void seedUsers() throws IOException {
        if (this.userRepository.count() != 0) {
            return;
        }
        SeedUserDto[] seedUserDtos = this.gson.fromJson(fileUtil.readFileContent(Constants.USERS_URL), SeedUserDto[].class);
        Arrays.stream(seedUserDtos).forEach(seedUserDto -> {
            if (this.validator.validate(seedUserDto)) {
            this.userRepository.saveAndFlush(this.modelMapper.map(seedUserDto, User.class));
            } else {
                this.validator.getViolations(seedUserDto)
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        });
    }

    @Override
    public void getUsersWithSoldItems() throws IOException {
        List<SoldItemsUsersDto> soldItemsUsersDtos = this.userRepository.findUserWithSoldItem()
                .stream().map(u -> {
                SoldItemsUsersDto s = this.modelMapper.map(u, SoldItemsUsersDto.class);
                    System.out.println();
                return s;
                }).collect(Collectors.toList());
        System.out.println();
        this.fileUtil.writeFile(this.gson.toJson(soldItemsUsersDtos), Constants.USER_SOLD_PRODUCTS_URL);

    }

    @Override
    public void getUserWithSoldItemsOrdered() throws IOException {
        List<User> users = this.userRepository.findUserWithSoldItemOrdered();
        UsersAndProductsDto usersAndProductsDto = new UsersAndProductsDto();

        usersAndProductsDto.setCount(users.size());
        Set<Buyer2Dto> buyer2Dtos = new HashSet<>();

        for (User user : users) {
            Buyer2Dto buyer2Dto = this.modelMapper.map(user, Buyer2Dto.class);
          Set<Product2Dto> product2Dtos =  user.getSelling().stream()
                    .map(p -> modelMapper.map(p, Product2Dto.class)).collect(Collectors.toSet());
          buyer2Dto.setSoldProducts(product2Dtos);
          buyer2Dtos.add(buyer2Dto);
        }
        usersAndProductsDto.setUsers(buyer2Dtos);

        this.fileUtil.writeFile(this.gson.toJson(usersAndProductsDto), Constants.USERS_AND_PRODUCTS_URL);
    }


}
