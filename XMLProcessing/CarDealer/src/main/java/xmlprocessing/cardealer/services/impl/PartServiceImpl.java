package xmlprocessing.cardealer.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xmlprocessing.cardealer.config.Constants;
import xmlprocessing.cardealer.domain.dtos.seedpartdto.SeedPartsDto;
import xmlprocessing.cardealer.domain.entities.Part;
import xmlprocessing.cardealer.domain.entities.Supplier;
import xmlprocessing.cardealer.repositories.PartRepository;
import xmlprocessing.cardealer.repositories.SupplierRepository;
import xmlprocessing.cardealer.services.PartService;
import xmlprocessing.cardealer.utils.ValidatorUtil;
import xmlprocessing.cardealer.utils.XmlParserUtil;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.Random;

@Service
public class PartServiceImpl implements PartService {

    private final ValidatorUtil validator;
    private final ModelMapper mapper;
    private final PartRepository partRepository;
    private final SupplierRepository supplierRepository;
    private final Random random;
    private final XmlParserUtil parser;

    @Autowired
    public PartServiceImpl(ValidatorUtil validator, ModelMapper mapper,
                           PartRepository partRepository,
                           SupplierRepository supplierRepository, Random random, XmlParserUtil parser) {
        this.validator = validator;
        this.mapper = mapper;
        this.partRepository = partRepository;
        this.supplierRepository = supplierRepository;
        this.random = random;
        this.parser = parser;
    }

    @Transactional
    @Override
    public void seedParts() throws JAXBException, FileNotFoundException {
        if (this.partRepository.count() != 0) {
            return;
        }
        SeedPartsDto seedPartsDto = this.parser.importFromXml(SeedPartsDto.class, Constants.PARTS_URL);

        seedPartsDto.getPartDtos().stream()
                .map(spdto -> {
                    Part part = this.mapper.map(spdto, Part.class);
                    part.setSupplier(generateRandomSupplier());
                    return part;
                }).forEach(this.partRepository::saveAndFlush);

    }

    private Supplier generateRandomSupplier() {
        long supplierId = this.random.nextInt((int)this.supplierRepository.count()) + 1;
        return this.supplierRepository.findById(supplierId).orElse(null);
    }
}
