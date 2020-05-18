package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.config.GlobalConstants;
import softuni.exam.models.dtos.seedofferdto.SeedOfferDto;
import softuni.exam.models.dtos.seedofferdto.SeedOffersDto;
import softuni.exam.models.entities.Car;
import softuni.exam.models.entities.Offer;
import softuni.exam.models.entities.Picture;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.FilesUtil;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Set;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final PictureRepository pictureRepository;
    private final CarRepository carRepository;
    private final SellerRepository sellerRepository;
    private final FilesUtil reader;
    private final XmlParser parser;
    private final ValidationUtil validator;
    private final ModelMapper mapper;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, PictureRepository pictureRepository,
                            CarRepository carRepository, SellerRepository sellerRepository,
                            FilesUtil reader, XmlParser parser, ValidationUtil validator, ModelMapper mapper) {
        this.offerRepository = offerRepository;
        this.pictureRepository = pictureRepository;
        this.carRepository = carRepository;
        this.sellerRepository = sellerRepository;
        this.reader = reader;
        this.parser = parser;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return this.reader.readFileContent(GlobalConstants.OFFERS_URL);
    }

    @Transactional
    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder result = new StringBuilder();

        SeedOffersDto seedOffersDto = this.parser.importFromXml(SeedOffersDto.class, GlobalConstants.OFFERS_URL);

        for (SeedOfferDto offerDto : seedOffersDto.getOffers()) {
            if (this.validator.isValid(offerDto)) {
                String description = offerDto.getDescription();
                String addedOnd = offerDto.getAddedOn();
                if (this.offerRepository.findByDescriptionAndAddedOn(description, addedOnd) == null) {
                    Offer offer = this.mapper.map(offerDto, Offer.class);
                    offer.setAddedOn(generateAddedOn(offerDto.getAddedOn()));
                    Car car = this.carRepository.findById(offerDto.getCar().getId()).orElse(null);
                    Seller seller = this.sellerRepository.findById(offerDto.getSeller().getId()).orElse(null);
                    if (car != null && seller != null) {
                        Set<Picture> pictures = this.pictureRepository.findAllByIdOfCar(car.getId());
                        car.setPictures(pictures);
                        offer.setCar(car);
                        offer.setSeller(seller);
                        offer.setPictures(pictures);
                        this.offerRepository.saveAndFlush(offer);
                        result.append(String.format("Successfully import offer %s - %s%n",
                                offer.getAddedOn(), offer.isHasGoldStatus()));
                    } else {
                        result.append("Invalid offer").append(System.lineSeparator());
                    }
                } else {
                    result.append("Offer already in DB!").append(System.lineSeparator());
                }
            } else {
                result.append("Invalid offer").append(System.lineSeparator());
            }
        }


        return result.toString();
    }

    private String generateAddedOn(String addedOn) {
        String result = "";
        String date = addedOn.substring(0,10);
        String time = addedOn.substring(11);
        result = date+ "T" + time;
        return result;
    }
}
