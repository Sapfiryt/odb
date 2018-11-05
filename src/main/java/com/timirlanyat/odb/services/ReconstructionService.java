package com.timirlanyat.odb.services;


import com.timirlanyat.odb.dal.entity.AttributesInReconstructions;
import com.timirlanyat.odb.dal.repositories.AttributeRepository;
import com.timirlanyat.odb.dal.repositories.LocationRepository;
import com.timirlanyat.odb.dal.repositories.ReconstructionRepository;
import com.timirlanyat.odb.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.stereotype.Service;

import javax.management.InvalidAttributeValueException;
import javax.transaction.Transactional;
import java.util.Optional;


@Service
public class ReconstructionService {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private ReconstructionRepository reconstructionRepository;
    @Autowired
    private AttributeRepository attributeRepository;

    @Transactional
    public Reconstruction createNewReconstrution(ReconstructionDto dto, Organizer org) throws InvalidAttributeValueException {


        Reconstruction rec = new Reconstruction();

        rec.setAbout(dto.getAbout());
        rec.setCost(dto.getCost());
        rec.setDateOf(dto.getDateOf());
        rec.setMaxParticipant(dto.getMaxParticipants());
        rec.setMinParticipant(dto.getMinParticipants());
        rec.setName(dto.getName());
        rec.setYear(dto.getYear());
        rec.setStatus("open");
        rec.setOrganizer(org);

        for(int i=0; i<dto.getAttributesId().size();i++) {
            AttributesInReconstructions attr = new AttributesInReconstructions();
            System.out.println(dto.getAttributesId().get(i)+"||"+dto.getAmount().get(i));
            Optional<Attribute> found =  attributeRepository.findById(dto.getAttributesId().get(i));

            if(found.isPresent()) {
                attr.setAmountOf(dto.getAmount().get(i))
                        .setAttribute(found.get())
                        .setReconstruction(rec);

                attributeRepository.save(
                        found.get().setAmount(
                                found.get().getAmount()-dto.getAmount().get(i)));

                rec.getAttributes().add(attr);
            }
            else
                throw new InvalidAttributeValueException("Attribute not exsits");
        }

        Optional<Location> found =locationRepository.findById(dto.getLocationId());

        if(found.isPresent())
            rec.setLocation(found.get());
        else
            throw new InvalidAttributeValueException("Location not exsits");


        rec=reconstructionRepository.save(rec);

        return rec;

    }
}
