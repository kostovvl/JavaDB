package xmlprocessing.cardealer.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import xmlprocessing.cardealer.config.Constants;
import xmlprocessing.cardealer.domain.dtos.carsfromtoyotadto.CarFromToyotaDto;
import xmlprocessing.cardealer.domain.dtos.carsfromtoyotadto.CarsFromToyotaDto;
import xmlprocessing.cardealer.domain.dtos.carswithpartsdto.CarDto;
import xmlprocessing.cardealer.domain.dtos.carswithpartsdto.CarsDto;
import xmlprocessing.cardealer.domain.dtos.carswithpartsdto.PartDto;
import xmlprocessing.cardealer.domain.dtos.carswithpartsdto.PartsDto;
import xmlprocessing.cardealer.domain.dtos.seedcardto.SeedCarDto;
import xmlprocessing.cardealer.domain.dtos.seedcardto.SeedCarsDto;
import xmlprocessing.cardealer.domain.entities.Car;
import xmlprocessing.cardealer.domain.entities.Part;
import xmlprocessing.cardealer.repositories.CarRepository;
import xmlprocessing.cardealer.repositories.PartRepository;
import xmlprocessing.cardealer.services.CarService;
import xmlprocessing.cardealer.utils.ValidatorUtil;
import xmlprocessing.cardealer.utils.XmlParserUtil;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    private final ValidatorUtil validator;
    private final ModelMapper mapper;
    private final PartRepository partRepository;
    private final CarRepository carRepository;
    private final Random random;
    private final XmlParserUtil parser;

    public CarServiceImpl(ValidatorUtil validator, ModelMapper mapper,
                          PartRepository partRepository, CarRepository carRepository,
                          Random random, XmlParserUtil parser) {
        this.validator = validator;
        this.mapper = mapper;
        this.partRepository = partRepository;
        this.carRepository = carRepository;
        this.random = random;
        this.parser = parser;
    }

    @Transactional
    @Override
    public void seedCars() throws JAXBException, FileNotFoundException {
        if (this.carRepository.count() != 0) {
            return;
        }
        SeedCarsDto seedCarsDto = this.parser.importFromXml(SeedCarsDto.class, Constants.CARS_URL);
        for (SeedCarDto carDto : seedCarsDto.getCarDtos()) {
            if (validator.isValid(carDto)) {
                Car car = this.mapper.map(carDto, Car.class);
                car.setParts(createRandomParts());
                this.carRepository.saveAndFlush(car);
            } else {
                this.validator.getViolations(carDto).stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        }

    }

    private Set<Part> createRandomParts() {
        int numberOfParts = this.random.nextInt(20 + 1 - 10) + 10;
        Set<Part> parts = new HashSet<>();

        for (int i = 0; i < numberOfParts; i++) {
            long partId = this.random.nextInt((int)this.partRepository.count()) + 1;
            parts.add(this.partRepository.findById(partId).orElse(null));
        }
        System.out.println();
        return parts;
    }

    @Override
    public void writeCarsFromMakeToyota() throws JAXBException {
        CarsFromToyotaDto carsFromToyotaDto = new CarsFromToyotaDto();
        carsFromToyotaDto.setCars(this.carRepository.findAllCarsMadeByToyota().stream()
                .map(c -> this.mapper.map(c, CarFromToyotaDto.class)).collect(Collectors.toList())
        );

        this.parser.exportToXML(carsFromToyotaDto, Constants.TOYOTA_CARS_URL);
    }

    @Transactional
    @Override
    public void writeCarsWithTheirParts() throws JAXBException {
        List<Car> cars = this.carRepository.findAll();
        List<CarDto> carDtos = new ArrayList<>();
        for (Car car : cars) {
            CarDto carDto = this.mapper.map(car, CarDto.class);
            List<PartDto> partDtos = new ArrayList<>();
            for (Part part : car.getParts()) {
                PartDto partDto = this.mapper.map(part, PartDto.class);
                partDtos.add(partDto);
            }
            PartsDto partsDto = new PartsDto();
            partsDto.setParts(partDtos);
            carDto.setParts(partsDto);
            carDtos.add(carDto);
        }
        CarsDto carsDto = new CarsDto();
        carsDto.setCars(carDtos);
        this.parser.exportToXML(carsDto, Constants.CARS_AND_PARTS_URL);
    }
}
