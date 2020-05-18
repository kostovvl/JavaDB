package xmlprocessing.productshop.domain.dtos.seeduserdto;

import org.hibernate.validator.constraints.Length;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedUserDto {

    @XmlAttribute(name = "first-name")
    private String firstName;

    @XmlAttribute(name = "last-name")
    @Length(min = 3, message = "User's last name must be at lease 3 characters long!")
    private String lastName;

    @XmlAttribute(name = "age")
    private int age;

    public SeedUserDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
