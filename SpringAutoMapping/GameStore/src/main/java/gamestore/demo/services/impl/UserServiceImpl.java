package gamestore.demo.services.impl;

import gamestore.demo.domain.dtos.LoginUserDto;
import gamestore.demo.domain.dtos.RegisterUserDto;
import gamestore.demo.domain.dtos.UserDto;
import gamestore.demo.domain.entities.Status;
import gamestore.demo.domain.entities.User;
import gamestore.demo.repositories.UserRepository;
import gamestore.demo.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private UserDto userDto;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public void registerUser(RegisterUserDto registerUserDto) {

       if (checkIfUserExists(registerUserDto)) {
           System.out.println("User with this e-Mail already exists!");
           return;
       }
        User user = this.modelMapper.map(registerUserDto, User.class);

        if (this.userRepository.count() == 0) {
            user.setStatus(Status.ADMIN);
        } else {
            user.setStatus(Status.USER);
        }
        this.userRepository.saveAndFlush(user);
        System.out.printf("%s was registered%n", user.getFullName());
    }

    private boolean checkIfUserExists(RegisterUserDto registerUserDto) {
      return this.userRepository.findByEmail(registerUserDto.getEmail()) != null;
    }


    @Override
    public void loginUser(LoginUserDto loginUserDto) {

        User user = this.userRepository.findByEmailAndPassword(loginUserDto.getEmail(), loginUserDto.getPassword());
        if(user == null) {
            System.out.println("Incorrect username / password");
            return;
        }


        System.out.println();
        if (userDto != null) {
            if (loginUserDto.getEmail().equals(userDto.getEmail())) {
                System.out.printf("User %s already logged in", userDto.getFullName());
            } else {
                System.out.println("There's already a logged user in the system!");
            }
            System.out.println("Logout and try again!");
        } else {
            this.userDto = this.modelMapper.map(user, UserDto.class);
            System.out.printf("Successfully logged in %s%n", this.userDto.getFullName());
        }
    }

    @Override
    public void logout() {
        if (this.userDto == null) {
            System.out.println("Cannot log out. No user was logged in.");
        } else {
            String fullName = this.userDto.getFullName();
            this.userDto = null;
            System.out.printf("User %s successfully logged out%n", fullName);
        }
    }

    @Override
    public UserDto getLoggedUser() {
        return this.userDto;
    }


}
