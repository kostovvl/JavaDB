package xmlprocessing.cardealer.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xmlprocessing.cardealer.config.Constants;
import xmlprocessing.cardealer.domain.dtos.localsuppliersdto.LocalSupplierDto;
import xmlprocessing.cardealer.domain.dtos.localsuppliersdto.LocalSuppliersDto;
import xmlprocessing.cardealer.domain.dtos.seedsuppliersdto.SeedSuppliersDto;
import xmlprocessing.cardealer.domain.entities.Supplier;
import xmlprocessing.cardealer.repositories.SupplierRepository;
import xmlprocessing.cardealer.services.SupplierService;
import xmlprocessing.cardealer.utils.ValidatorUtil;
import xmlprocessing.cardealer.utils.XmlParserUtil;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final ValidatorUtil validator;
    private final ModelMapper mapper;
    private final SupplierRepository repository;
    private final XmlParserUtil parser;

    @Autowired
    public SupplierServiceImpl(ValidatorUtil validator,
                               ModelMapper mapper, SupplierRepository repository, XmlParserUtil parser) {
        this.validator = validator;
        this.mapper = mapper;
        this.repository = repository;
        this.parser = parser;
    }


    @Override
    public void seedSuppliers() throws JAXBException, FileNotFoundException {
        if (this.repository.count() != 0) {
            return;
        }
        SeedSuppliersDto seedSuppliersDto = this.parser.importFromXml(SeedSuppliersDto.class, Constants.SUPPLIERS_URL);
        seedSuppliersDto.getSuppliers().stream()
                .map(s -> {
                    Supplier supplier = this.mapper.map(s, Supplier.class);
                    return supplier;
                } ).forEach(supplier -> this.repository.saveAndFlush(supplier));

    }

    @Override
    public void writeLocalSuppliers() throws JAXBException {
        LocalSuppliersDto localSuppliersDto = new LocalSuppliersDto();
        localSuppliersDto.setSuppliers(
                this.repository.findAllLocalSuppliers().stream()
                .map(s -> {
                    LocalSupplierDto localSupplierDto = this.mapper.map(s, LocalSupplierDto.class);
                    localSupplierDto.setPartsCount(s.getPars().size());
                    return localSupplierDto;
                }).collect(Collectors.toList())
        );
        this.parser.exportToXML(localSuppliersDto, Constants.LOCAL_SUPPLIERS_URL);
    }
}
