package json.cardealer.services.impl;

import com.google.gson.Gson;
import json.cardealer.config.Constants;
import json.cardealer.domain.dtos.createdtos.CreatePartDto;
import json.cardealer.domain.entites.Part;
import json.cardealer.domain.entites.Supplier;
import json.cardealer.repositories.PartRepository;
import json.cardealer.repositories.SupplierRepository;
import json.cardealer.services.PartService;
import json.cardealer.utils.FilesUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

@Service
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;
    // Поставям supplierRepository  с ясната идея, че не е правилно, но го правя, да са си спеся
    // boilerplate code
    private final SupplierRepository supplierRepository;
    private final Gson gson;
    private final ModelMapper mapper;
    private final FilesUtil filesUtil;
    private final Random random;

    @Autowired
    public PartServiceImpl(PartRepository partRepository, SupplierRepository supplierRepository,
                           Gson gson, ModelMapper mapper, FilesUtil filesUtil, Random random) {
        this.partRepository = partRepository;
        this.supplierRepository = supplierRepository;
        this.gson = gson;
        this.mapper = mapper;
        this.filesUtil = filesUtil;
        this.random = random;
    }

    @Override
    public void seedParts() throws IOException {
        if (this.partRepository.count() != 0) {
            return;
        }
        String partsInput = this.filesUtil.readFileContent(Constants.PARTS_FILE_URL);
        CreatePartDto[] createPartDtos =
                this.gson.fromJson(partsInput, CreatePartDto[].class);
        Arrays.stream(createPartDtos)
                .map(o -> {
                    o.setSupplier(generateRandomSupplier());
                    return this.mapper.map(o, Part.class);
                }).forEach(this.partRepository::saveAndFlush);

    }

    private Supplier generateRandomSupplier() {
        long supplierId = this.random.nextInt((int)this.supplierRepository.count()) + 1;
        return this.supplierRepository.getOne(supplierId);
    }
}
