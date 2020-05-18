package json.cardealer.domain.dtos.createdtos;

import com.google.gson.annotations.Expose;

public class CreateSupplierDto {

    @Expose
    private String name;
    @Expose
    private Boolean isImporter;

    public CreateSupplierDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getImporter() {
        return isImporter;
    }

    public void setImporter(Boolean importer) {
        isImporter = importer;
    }
}
