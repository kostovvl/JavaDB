package json.cardealer.domain.dtos.writedtos;

import com.google.gson.annotations.Expose;
import json.cardealer.domain.entites.Part;

import java.util.Set;

public class WriteCarWithPartsDto {

    @Expose
    private String make;

    @Expose
    private String model;

    @Expose
    private long travelledDistance;

    @Expose
    private Set<WritePartDto> parts;

    public WriteCarWithPartsDto() {
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public long getTravelledDistance() {
        return travelledDistance;
    }

    public void setTravelledDistance(long travelledDistance) {
        this.travelledDistance = travelledDistance;
    }

    public Set<WritePartDto> getParts() {
        return parts;
    }

    public void setParts(Set<WritePartDto> parts) {
        this.parts = parts;
    }
}
