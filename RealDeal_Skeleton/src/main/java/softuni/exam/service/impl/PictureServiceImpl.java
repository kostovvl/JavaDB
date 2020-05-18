package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.config.GlobalConstants;
import softuni.exam.models.entities.Picture;
import softuni.exam.models.dtos.SeedPictureDto;
import softuni.exam.models.entities.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.PictureService;
import softuni.exam.util.FilesUtil;
import softuni.exam.util.ValidationUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Set;

@Service
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    private final CarRepository carRepository;
    private final FilesUtil reader;
    private final Gson gson;
    private final ValidationUtil validator;
    private final ModelMapper mapper;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, CarRepository carRepository,
                              FilesUtil reader, Gson gson, ValidationUtil validator, ModelMapper mapper) {
        this.pictureRepository = pictureRepository;
        this.carRepository = carRepository;
        this.reader = reader;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return this.reader.readFileContent(GlobalConstants.PICTURES_URL);
    }

    @Transactional
    @Override
    public String importPictures() throws IOException {
        StringBuilder result = new StringBuilder();

        SeedPictureDto[] seedPictureDtos = this.gson.fromJson(this.reader.readFileContent(GlobalConstants.PICTURES_URL),
                SeedPictureDto[].class);

        for (SeedPictureDto pictureDto : seedPictureDtos) {
            if (this.validator.isValid(pictureDto)) {
                if (this.pictureRepository.findByName(pictureDto.getName()) == null) {
                    Picture picture = this.mapper.map(pictureDto, Picture.class);
                    Car car = this.carRepository.findById(pictureDto.getCar()).orElse(null);
                    if (car != null) {
                       picture.setCar(car);
                       this.pictureRepository.saveAndFlush(picture);
                        this.carRepository.saveAndFlush(car);
                       result.append(String.format("Successfully import picture - %s%n", picture.getName()));
                    } else {
                        result.append("Invalid picture").append(System.lineSeparator());
                    }
                } else {
                    result.append("Picture already in DB!").append(System.lineSeparator());
                }
            } else {
                result.append("Invalid picture").append(System.lineSeparator());
            }
        }

        return result.toString();
    }
}
