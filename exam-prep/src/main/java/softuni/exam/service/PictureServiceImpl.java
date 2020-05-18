package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.config.Constants;
import softuni.exam.domain.dtos.seedpicturedtos.SeedPicturesDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.util.FileUtils;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;


import javax.xml.bind.JAXBException;
import java.io.IOException;


@Service
public class PictureServiceImpl implements PictureService {

    private final PictureRepository repository;
    private final ModelMapper mapper;
    private final XmlParser parser;
    private final ValidatorUtil validator;
    private final FileUtils fileUtils;

    @Autowired
    public PictureServiceImpl(PictureRepository repository,
                              ModelMapper mapper, XmlParser parser, ValidatorUtil validator, FileUtils fileUtils) {
        this.repository = repository;
        this.mapper = mapper;
        this.parser = parser;
        this.validator = validator;
        this.fileUtils = fileUtils;
    }

    @Override
    public String importPictures() throws IOException, JAXBException {
        StringBuilder result = new StringBuilder();
        SeedPicturesDto seedPicturesDto = this.parser.importFromXml(SeedPicturesDto.class, Constants.PICTURES_URL);
        seedPicturesDto.getPictures().forEach(
                dto-> {
                  if (this.validator.isValid(dto)){
                      if (this.repository.findByUrl(dto.getUrl()) == null) {
                          Picture picture = this.mapper.map(dto, Picture.class);
                          this.repository.saveAndFlush(picture);
                          result.append("Successfully imported picture - ").append(picture.getUrl()).append(System.lineSeparator());
                      } else {
                          result.append("Picture already exists in DB").append(System.lineSeparator());
                      }

                  } else {
                      result.append("Invalid picture").append(System.lineSeparator());
                  }
                }
        );
       return result.toString();
    }

    @Override
    public boolean areImported() {

        return this.repository.count() > 0;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {
        return this.fileUtils.readFileContent(Constants.PICTURES_URL);
    }


}
