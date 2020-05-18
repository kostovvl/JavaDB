package json.cardealer.services.impl;

import com.google.gson.Gson;
import json.cardealer.config.Constants;
import json.cardealer.domain.dtos.createdtos.CreateSupplierDto;
import json.cardealer.domain.dtos.writedtos.WriteLocalSupplierDto;
import json.cardealer.domain.entites.Supplier;
import json.cardealer.repositories.SupplierRepository;
import json.cardealer.services.SupplierService;
import json.cardealer.utils.FilesUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;
    private final FilesUtil filesUtil;
    private final Gson gson;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository,
                               ModelMapper modelMapper, FilesUtil filesUtil, Gson gson) {
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.filesUtil = filesUtil;
        this.gson = gson;
    }

    @Override
    public void seedSuppliers() throws IOException {
        if (this.supplierRepository.count() != 0) {
            return;
        }
        String suppliersInput = this.filesUtil.readFileContent(Constants.SUPPLIERS_FILE_URL);
        CreateSupplierDto[] createSupplierDtos =
                this.gson.fromJson(suppliersInput, CreateSupplierDto[].class);
        Arrays.stream(createSupplierDtos)
                .map(o -> {
                    return this.modelMapper.map(o, Supplier.class);
                }).forEach(this.supplierRepository::saveAndFlush);

    }

    @Override
    public void writeLocalSuppliers() throws IOException {
        Boolean isImporter = false;
        List<WriteLocalSupplierDto> writeLocalSupplierDtos =
                this.supplierRepository.findAllByImporter(isImporter)
                .stream().map(o -> {
                    WriteLocalSupplierDto writeLocalSupplierDto = this.modelMapper.map(o, WriteLocalSupplierDto.class);
                    writeLocalSupplierDto.setPartsCount(o.getPars().size());
                    return writeLocalSupplierDto;
                }).collect(Collectors.toList());

        this.filesUtil.write(this.gson.toJson(writeLocalSupplierDtos), Constants.LOCAL_SUPPLIERS_FILE_URL);

    }
}
