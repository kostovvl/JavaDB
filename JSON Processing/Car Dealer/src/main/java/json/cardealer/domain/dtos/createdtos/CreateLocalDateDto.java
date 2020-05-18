package json.cardealer.domain.dtos.createdtos;

import com.google.gson.annotations.Expose;

import java.time.LocalDate;

public class CreateLocalDateDto {

    @Expose
    LocalDate localDate;

    public CreateLocalDateDto() {
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
