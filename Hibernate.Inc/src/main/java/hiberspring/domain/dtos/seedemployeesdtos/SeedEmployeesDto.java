package hiberspring.domain.dtos.seedemployeesdtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "employees")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedEmployeesDto {

    @XmlElement(name = "employee")
    private List<SeedEmployeeDto> employees;

    public SeedEmployeesDto() {
    }

    public List<SeedEmployeeDto> getEmployees() {
        return employees;
    }

    public void setEmployees(List<SeedEmployeeDto> employees) {
        this.employees = employees;
    }
}
