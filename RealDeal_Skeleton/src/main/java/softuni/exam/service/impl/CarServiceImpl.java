package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.config.GlobalConstants;
import softuni.exam.models.dtos.SeedCarDto;
import softuni.exam.models.entities.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.FilesUtil;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final FilesUtil reader;
    private final Gson gson;
    private final ValidationUtil validator;
    private final ModelMapper mapper;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, FilesUtil reader,
                          Gson gson, ValidationUtil validator,
                          ModelMapper mapper) {
        this.carRepository = carRepository;
        this.reader = reader;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return this.reader.readFileContent(GlobalConstants.CARS_URL);
    }

    @Override
    public String importCars() throws IOException {
        StringBuilder result = new StringBuilder();

        SeedCarDto[] seedCarDtos = this.gson.fromJson(this.reader.readFileContent(GlobalConstants.CARS_URL),
                SeedCarDto[].class);

        for (SeedCarDto carDto : seedCarDtos) {
            if (this.validator.isValid(carDto)) {
                String make = carDto.getMake();
                String model = carDto.getModel();
                long kilometers = carDto.getKilometers();
                if (this.carRepository.findByMakeAndModelAndAndKilometers(make, model, kilometers) == null) {
                    Car car = this.mapper.map(carDto, Car.class);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                    LocalDate registeredOn = LocalDate.parse(carDto.getRegisteredOn(), formatter);
                    car.setRegisteredOn(registeredOn);
                    this.carRepository.saveAndFlush(car);
                    result.append(String.format("Successfully imported car - %s - %s%n",
                            car.getMake(), car.getModel()));
                } else {
                    result.append("Car already in DB!").append(System.lineSeparator());
                }
            } else {
                result.append("Invalid car").append(System.lineSeparator());
            }
        }

        return result.toString();
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        StringBuilder result = new StringBuilder();
        
        List<Car> cars = this.carRepository.findAllCarsForTask5();
        for (Car car : cars) {
            result.append(String.format("Car make - %s, model - %s%n" +
                    "     Kilometers - %d%n" +
                    "     Registered on - %s%n" +
                    "     Number of pictures - %d%n",
                    car.getMake(), car.getModel(), car.getKilometers(),
                    car.getRegisteredOn(), car.getPictures().size()));
        }

        return result.toString();
    }
}
