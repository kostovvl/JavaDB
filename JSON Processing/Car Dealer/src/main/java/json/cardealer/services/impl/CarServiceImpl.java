package json.cardealer.services.impl;

import com.google.gson.Gson;
import json.cardealer.config.Constants;
import json.cardealer.domain.dtos.createdtos.CreateCarDto;
import json.cardealer.domain.dtos.writedtos.WriteCarDto;
import json.cardealer.domain.dtos.writedtos.WriteCarWithPartsDto;
import json.cardealer.domain.dtos.writedtos.WriteLocalSupplierDto;
import json.cardealer.domain.dtos.writedtos.WritePartDto;
import json.cardealer.domain.entites.Car;
import json.cardealer.domain.entites.Part;
import json.cardealer.repositories.CarRepository;
import json.cardealer.repositories.PartRepository;
import json.cardealer.services.CarService;
import json.cardealer.utils.FilesUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    private final FilesUtil filesUtil;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final Random random;
    private final CarRepository carRepository;
    // Поставям partRepository  с ясната идея, че не е правилно, но го правя, да са си спеся
    // boilerplate code
    private final PartRepository partRepository;

    @Autowired
    public CarServiceImpl(FilesUtil filesUtil, Gson gson,
                          ModelMapper modelMapper, Random random, CarRepository carRepository,
                          PartRepository partRepository) {
        this.filesUtil = filesUtil;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.random = random;
        this.carRepository = carRepository;
        this.partRepository = partRepository;
    }

    @Transactional
    @Override
    public void seedCars() throws IOException {
        if (this.carRepository.count() != 0) {
            return;
        }
        String carsInput = this.filesUtil.readFileContent(Constants.CARS_FILE_URL);
        CreateCarDto[] createCarDtos =
                this.gson.fromJson(carsInput, CreateCarDto[].class);
        for (CreateCarDto createCarDto : createCarDtos) {
            Car car = this.modelMapper.map(createCarDto, Car.class);
            car.setParts(createRandomParts());
            this.carRepository.saveAndFlush(car);
            System.out.println();
        }
    }

    private Set<Part> createRandomParts() {
        int numberOfParts = this.random.nextInt(20 + 1 - 10) + 10;
        Set<Part> parts = new HashSet<>();

        for (int i = 0; i < numberOfParts; i++) {
            long partId = this.random.nextInt((int)this.partRepository.count()) + 1;
            parts.add(this.partRepository.findById(partId));
        }
        System.out.println();
        return parts;
    }

    @Transactional
    @Override
    public void writeAllToyotas() throws IOException {
        String carName = "Toyota";
        List<WriteCarDto> writeCarDtos = this.carRepository.findAllByMake(carName)
                .stream().map(o -> {
                    return this.modelMapper.map(o, WriteCarDto.class);
                }).collect(Collectors.toList());
        this.filesUtil.write(this.gson.toJson(writeCarDtos), Constants.TOYOTA_CARS_FILE_URL);
    }

    @Transactional
    @Override
    public void writeCarsWithTheirListOfParts() throws IOException {
        List<WriteCarWithPartsDto> writeCarWithPartsDtos =
                this.carRepository.findAll().stream()
                .map(o -> {
                    Car car = o;
                    System.out.println();
                    WriteCarWithPartsDto r = this.modelMapper.map(o, WriteCarWithPartsDto.class);
                return r;
                }).collect(Collectors.toList());
        this.filesUtil.write(this.gson.toJson(writeCarWithPartsDtos), Constants.CARS_AND_PARTS_FILE_URL);
    }


}
