package gamestore.demo.domain.dtos;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterUserDto {
    private String email;
    private String password;
    private String fullName;

    public RegisterUserDto() {
    }

    public RegisterUserDto(String emial, String password, String fullName) {
        this.email = emial;
        this.password = password;
        this.fullName = fullName;
    }

    @Pattern(regexp = ".+[@].+[\\.].+", message = "Incorrect email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String emial) {
        this.email = emial;
    }

    @Pattern(regexp = "[A-Z]{1,}.+[0-9]{1,}.+", message = "Password is not Valid")
    @Size(min = 6, message = "Password length must be at leas 6 characters")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
