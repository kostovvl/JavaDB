package xmlprocessing.productshop.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xmlprocessing.productshop.config.Constants;
import xmlprocessing.productshop.domain.dtos.seeduserdto.SeedUserDto;
import xmlprocessing.productshop.domain.dtos.seeduserdto.SeedUsersDto;
import xmlprocessing.productshop.domain.dtos.userandproductsdto.SoldProductDtoo;
import xmlprocessing.productshop.domain.dtos.userandproductsdto.SoldProductsDtoo;
import xmlprocessing.productshop.domain.dtos.userandproductsdto.UserDtoo;
import xmlprocessing.productshop.domain.dtos.userandproductsdto.UsersDtoo;
import xmlprocessing.productshop.domain.dtos.userswithsoldproductdto.SoldProductDto;
import xmlprocessing.productshop.domain.dtos.userswithsoldproductdto.UserWithSoldProductsDto;
import xmlprocessing.productshop.domain.dtos.userswithsoldproductdto.UsersWithSoldProductsDto;
import xmlprocessing.productshop.domain.entities.Product;
import xmlprocessing.productshop.domain.entities.User;
import xmlprocessing.productshop.repositories.UserRepository;
import xmlprocessing.productshop.services.UserService;
import xmlprocessing.productshop.utils.ValidatorUtil;
import xmlprocessing.productshop.utils.XMLParser;

import javax.validation.ConstraintViolation;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final XMLParser xmlParser;
    private final ValidatorUtil validatorUtil;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Autowired
    public UserServiceImpl(XMLParser xmlParser, ValidatorUtil validatorUtil,
                           UserRepository userRepository, ModelMapper mapper) {
        this.xmlParser = xmlParser;
        this.validatorUtil = validatorUtil;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public void seedUsers() throws JAXBException, FileNotFoundException {
        if (this.userRepository.count() != 0) {
            return;
        }
        SeedUsersDto seedUsersDto =
                this.xmlParser.importFromXml(SeedUsersDto.class, Constants.USERS_URL);
        for (SeedUserDto dto : seedUsersDto.getUsers()) {
            if (this.validatorUtil.isValid(dto)) {
                this.userRepository.saveAndFlush(this.mapper.map(dto, User.class));
            } else {
                this.validatorUtil.getViolations(dto).stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        }

    }

    @Override
    public void writeUsersWithSuccessfullySoldProducts() throws JAXBException {
        List<User> users = this.userRepository.findAllWithAtLeasOneSoldProduct();
        List<UserWithSoldProductsDto> usersDto = new ArrayList<>();
        System.out.println();
        boolean hasSoldProducts = false;
        for (User user : users) {
            for (Product product : user.getProductsForSale()) {
                if (product.getBuyer() == null) {
                    hasSoldProducts = true;
                    break;
                }
            }
            if (hasSoldProducts) {
                hasSoldProducts = false;
                continue;
            }

            UserWithSoldProductsDto userDto = this.mapper.map(user, UserWithSoldProductsDto.class);
            List<SoldProductDto> productDtos = new ArrayList<>();
            for (Product product : user.getProductsForSale()) {
                SoldProductDto productDto = this.mapper.map(product, SoldProductDto.class);
                try{productDto.setBuyerFirstName(product.getBuyer().getFirstName());
                } catch (NullPointerException e) {
                    productDto.setBuyerFirstName("");
                }
                productDto.setBuyerLastName(product.getBuyer().getLastName());
                productDtos.add(productDto);
            }
            userDto.setSoldProducts(productDtos);
            usersDto.add(userDto);

        }
        UsersWithSoldProductsDto usersWithSoldProductsDto = new UsersWithSoldProductsDto();
        usersWithSoldProductsDto.setUsers(usersDto);
        this.xmlParser.exportToXML(usersWithSoldProductsDto, Constants.USERS_SOLD_PRODUCTS);
    }

    @Override
    public void writeUsersWithSoldProducts() throws JAXBException {
        List<User> users = this.userRepository.findAllWithAtLeasOneSoldProduct();
        UsersDtoo usersDtoo = new UsersDtoo();
        List<UserDtoo> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDtoo userDto = this.mapper.map(user, UserDtoo.class);
            SoldProductsDtoo soldProductsDtoo = new SoldProductsDtoo();
            List<SoldProductDtoo> soldProductDtos = new ArrayList<>();
            for (Product product : user.getProductsForSale()) {
                SoldProductDtoo s = this.mapper.map(product, SoldProductDtoo.class);
                soldProductDtos.add(s);
            }
            soldProductsDtoo.setSoldProducts(soldProductDtos);
            soldProductsDtoo.setCount(soldProductsDtoo.getSoldProducts().size());
            userDto.setSoldProducts(soldProductsDtoo);
            userDtos.add(userDto);
        }
        usersDtoo.setUsers(userDtos);
        usersDtoo.setCount(usersDtoo.getUsers().size());
        this.xmlParser.exportToXML(usersDtoo, Constants.USERS_AND_PRODUCTS);
    }
}
