package com.timirlanyat.odb.services;


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

        Optional<Location> found =locationRepository.findById(dto.getLocationId());

        if(found.isPresent())
            rec.setLocation(found.get());
        else
            throw new InvalidAttributeValueException("Location not exsits");

        rec=reconstructionRepository.save(rec);

        return rec;

    }
}
