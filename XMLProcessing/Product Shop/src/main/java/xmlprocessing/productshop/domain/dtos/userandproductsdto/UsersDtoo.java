package xmlprocessing.productshop.domain.dtos.userandproductsdto;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UsersDtoo {

    @XmlAttribute(name = "count")
    private int count;
    @XmlElement(name = "user")
    List<UserDtoo> users;

    public UsersDtoo() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<UserDtoo> getUsers() {
        return users;
    }

    public void setUsers(List<UserDtoo> users) {
        this.users = users;
    }
}
