package com.timirlanyat.odb.services;

import com.timirlanyat.odb.dal.repositories.LocationRepository;
import com.timirlanyat.odb.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InvalidAttributeValueException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Transactional
    public Location createNewLocation(LocationDto dto) throws InvalidAttributeValueException {


        Location location = new Location();

        location.setAddress(dto.getAddress());
        location.setCountry(dto.getCountry());
        location.setName(dto.getName());

        try {
            location.setPhoto(dto.getPhoto().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        location = locationRepository.save(location);

        return location;

    }
}
