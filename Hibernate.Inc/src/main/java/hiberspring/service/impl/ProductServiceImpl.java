package hiberspring.service.impl;

import hiberspring.common.Constants;
import hiberspring.domain.dtos.seedproductsdtos.SeedProductDto;
import hiberspring.domain.dtos.seedproductsdtos.SeedProductsDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Product;
import hiberspring.domain.entities.Town;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.ProductRepository;
import hiberspring.service.ProductService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final FileUtil reader;
    private final StringBuilder result;
    private final XmlParser parser;
    private final ValidationUtil validator;
    private final ModelMapper mapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, BranchRepository branchRepository,
                              FileUtil reader, StringBuilder result, XmlParser parser,
                              ValidationUtil validator, ModelMapper mapper) {
        this.productRepository = productRepository;
        this.branchRepository = branchRepository;
        this.reader = reader;
        this.result = result;
        this.parser = parser;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public Boolean productsAreImported() {
        return this.productRepository.count() > 0;
    }

    @Override
    public String readProductsXmlFile() throws IOException {
        return this.reader.readFile(Constants.PRODUCTS_URL);
    }

    @Transactional
    @Override
    public String importProducts() throws JAXBException, FileNotFoundException {

        SeedProductsDto seedProductsDto = this.parser.readXml(SeedProductsDto.class, Constants.PRODUCTS_URL);

        for (SeedProductDto productDto : seedProductsDto.getProducts()) {
            if (this.validator.isValid(productDto)) {
                if (this.productRepository.findByName(productDto.getName()) == null) {
                    Branch branch = this.branchRepository.findByName(productDto.getBranch());
                    if (branch != null) {
                        Product product = this.mapper.map(productDto, Product.class);
                        product.setBranch(branch);
                        this.productRepository.saveAndFlush(product);
                        this.result.append(String.format("Successfully imported Product %s.%n", product.getName()));
                    } else {
                        this.result.append("Error: Invalid data.").append(System.lineSeparator());
                    }
                } else {
                    this.result.append("Already in DB.").append(System.lineSeparator());
                }
            } else {
                this.result.append("Error: Invalid data.").append(System.lineSeparator());
            }
        }


        return this.result.toString();
    }
}
