package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.config.GlobalConstants;
import softuni.exam.models.dtos.SeedSellerDto;
import softuni.exam.models.dtos.SeedSellersDto;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.FilesUtil;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@Service
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final FilesUtil reader;
    private final XmlParser parser;
    private final ValidationUtil validator;
    private final ModelMapper mapper;

    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository, FilesUtil reader,
                             XmlParser parser, ValidationUtil validator,
                             ModelMapper mapper) {
        this.sellerRepository = sellerRepository;
        this.reader = reader;
        this.parser = parser;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return this.reader.readFileContent(GlobalConstants.SELLERS_URL);
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        StringBuilder result = new StringBuilder();

        SeedSellersDto seedSellersDto = this.parser.importFromXml(SeedSellersDto.class,
                GlobalConstants.SELLERS_URL);
        for (SeedSellerDto sellerDto : seedSellersDto.getSellers()) {
            if (this.validator.isValid(sellerDto)) {
                if (this.sellerRepository.findByEmail(sellerDto.getEmail()) == null) {
                    Seller seller = this.mapper.map(sellerDto, Seller.class);
                    this.sellerRepository.saveAndFlush(seller);
                    result.append(String.format("Successfully import seller %s - %s%n",
                            seller.getLastName(), seller.getEmail()));
                } else {
                    result.append("Seller already in DB!").append(System.lineSeparator());
                }
            } else {
                result.append("Invalid seller").append(System.lineSeparator());
            }
        }


        return result.toString();
    }
}
