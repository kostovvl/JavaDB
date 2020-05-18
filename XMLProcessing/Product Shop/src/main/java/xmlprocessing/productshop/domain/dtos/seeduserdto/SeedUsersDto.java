package xmlprocessing.productshop.domain.dtos.seeduserdto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedUsersDto {

    @XmlElement(name = "user")
    private List<SeedUserDto> users;

    public SeedUsersDto() {
    }

    public List<SeedUserDto> getUsers() {
        return users;
    }

    public void setUsers(List<SeedUserDto> users) {
        this.users = users;
    }
}
