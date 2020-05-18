package gamestore.demo.services;

import gamestore.demo.domain.dtos.LoginUserDto;
import gamestore.demo.domain.dtos.RegisterUserDto;
import gamestore.demo.domain.dtos.UserDto;

public interface UserService {

    void registerUser(RegisterUserDto registerUserDto);

    void loginUser(LoginUserDto loginUserDto);

    void logout();

    UserDto getLoggedUser();


}
