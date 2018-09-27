package com.timirlanyat.odb.services;

import com.timirlanyat.odb.dal.repositories.HashRepository;
import com.timirlanyat.odb.dal.repositories.OrganizerRepository;
import com.timirlanyat.odb.dal.repositories.UserRepository;
import com.timirlanyat.odb.exceptions.EmailExistsException;
import com.timirlanyat.odb.model.Hash;
import com.timirlanyat.odb.model.Organizer;
import com.timirlanyat.odb.model.User;
import com.timirlanyat.odb.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizerRepository organizerRepository;
    @Autowired
    private HashRepository hashRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerNewUserAccount(UserDto accountDto)
            throws EmailExistsException {

        if (emailExist(accountDto.getEmail())) {
            throw new EmailExistsException(accountDto.getEmail());
        }

        User user = new User();
        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setEmail(accountDto.getEmail());
        user.setPhoneNumber(accountDto.getPhoneNumber());

        if (accountDto.getOrganizer()!=null) {
            user.setRoles(Arrays.asList("REC_ORG","REC_USER"));
            Organizer org = new Organizer(user);
            org.setNumAgreement(accountDto.getAgreement());
            user = organizerRepository.save(org).getUser();

        } else {
            user.setRoles(Arrays.asList("REC_USER"));
            user = userRepository.save(user);
        }

        hashRepo.save(new Hash().setUser(user)
                .setHash(passwordEncoder.encode(accountDto.getPassword())));

        return user;

    }

    public Map<String,Object> getUserParameters(Principal principal){

        Map<String,Object> model= new HashMap<>();
         
        User user = userRepository.findByEmail(principal.getName());
        Organizer org = organizerRepository.findByUser(user);

        if(user!=null)
            model.put("user",user);


        if(org!=null)
            model.put("organizer",org);


        return model;
    }

    private boolean emailExist(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }
}