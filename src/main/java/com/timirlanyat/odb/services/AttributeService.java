package com.timirlanyat.odb.services;

import com.timirlanyat.odb.dal.repositories.AttributeRepository;
import com.timirlanyat.odb.model.Attribute;
import com.timirlanyat.odb.model.AttributeDto;
import com.timirlanyat.odb.model.Location;
import com.timirlanyat.odb.model.LocationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InvalidAttributeValueException;
import javax.transaction.Transactional;
import java.io.IOException;

@Service
@Transactional
public class AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;

    @Transactional
    public Attribute createNewAttribute(AttributeDto dto){


        Attribute attribute = new Attribute();

        attribute.setAmount(dto.getAmount());
        attribute.setCost(dto.getCost());
        attribute.setDescription(dto.getDescription());
        attribute.setName(dto.getName());
        attribute.setType(dto.getType());


        attribute = attributeRepository.save(attribute);

        return attribute;

    }
}
