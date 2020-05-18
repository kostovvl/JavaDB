package softuni.exam.domain.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "teams")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedTeamsDto {

    @XmlElement(name = "team")
    private List<SeedTeamDto> teams;

    public SeedTeamsDto() {
    }

    public List<SeedTeamDto> getTeams() {
        return teams;
    }

    public void setTeams(List<SeedTeamDto> teams) {
        this.teams = teams;
    }
}
