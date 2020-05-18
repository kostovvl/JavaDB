package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.config.Constants;
import softuni.exam.domain.dtos.SeedPictureDto;
import softuni.exam.domain.dtos.SeedPicturesDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.PictureService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    private final FileUtil reader;
    private final StringBuilder result;
    private final XmlParser parser;
    private final ValidatorUtil validator;
    private final ModelMapper mapper;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, FileUtil reader,
                              StringBuilder result, XmlParser parser, ValidatorUtil validator, ModelMapper mapper) {
        this.pictureRepository = pictureRepository;
        this.reader = reader;
        this.result = result;
        this.parser = parser;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public String importPictures() throws JAXBException, FileNotFoundException {
        SeedPicturesDto seedPicturesDto = this.parser.importFromXml(SeedPicturesDto.class, Constants.PICTURES_URL);

        for (SeedPictureDto pictureDto : seedPicturesDto.getPictures()) {
            if (this.validator.isValid(pictureDto)) {

                if (this.pictureRepository.findByUrl(pictureDto.getUrl()) == null) {

                    Picture picture = this.mapper.map(pictureDto, Picture.class);
                    this.pictureRepository.saveAndFlush(picture);
                    this.result.append(String.format("Successfully imported picture - %s%n", picture.getUrl()));

                } else {
                    this.result.append("Invalid picture").append(System.lineSeparator());
                }

            } else {
                this.result.append("Invalid picture").append(System.lineSeparator());
            }
        }


        return this.result.toString();
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {
        return this.reader.readFile(Constants.PICTURES_URL);
    }

}
