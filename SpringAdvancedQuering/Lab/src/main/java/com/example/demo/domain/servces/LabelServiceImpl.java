package com.example.demo.domain.servces;

import com.example.demo.domain.repositories.LabelRepository;
import com.example.demo.domain.servces.interfaces.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LabelServiceImpl implements LabelService {

    @Autowired
    private final LabelRepository labelRepository;

    public LabelServiceImpl(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }
}
