package com.timirlanyat.odb.services;

import com.timirlanyat.odb.dal.repositories.HashRepository;
import com.timirlanyat.odb.dal.repositories.OrganizerRepository;
import com.timirlanyat.odb.dal.repositories.MemberRepository;
import com.timirlanyat.odb.exceptions.EmailExistsException;
import com.timirlanyat.odb.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private MemberRepository memberRepository;
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

        User user;

        if (accountDto.getOrganizer()==null) {

            user = new Member();
            user.setFirstName(accountDto.getFirstName());
            user.setLastName(accountDto.getLastName());
            user.setEmail(accountDto.getEmail());
            user.setPhoneNumber(accountDto.getPhoneNumber());
            user.setDateOfBirth(accountDto.getDateOfBirth());
            user.setSex(accountDto.getSex());
            user.setAdmin(false);
            user.setRoles(Arrays.asList("REC_MEMBER"));
            user = memberRepository.save((Member)user);
        }else{
            Organizer org = new Organizer();
            org.setFirstName(accountDto.getFirstName());
            org.setLastName(accountDto.getLastName());
            org.setEmail(accountDto.getEmail());
            org.setPhoneNumber(accountDto.getPhoneNumber());
            org.setDateOfBirth(accountDto.getDateOfBirth());
            org.setSex(accountDto.getSex());
            org.setAdmin(false);
            org.setRoles(Arrays.asList("REC_MEMBER"));
            org.setNumAgreement(accountDto.getAgreement());
            org.setApproved(false);
            user = (Member)organizerRepository.save(org);
        }

        hashRepo.save(new Hash().setUser((Member)user)
                .setHash(passwordEncoder.encode(accountDto.getPassword())));

        return user;

    }

    public Map<String,Object> getUserParameters(Principal principal){

        Map<String,Object> model= new HashMap<>();
         
        User user = memberRepository.findByEmail(principal.getName());
        organizerRepository.findById(user.getId()).ifPresent( organizer -> {
            if(organizer.getApproved())
                model.put("organizer",organizer);
        });

        if(user!=null)
            model.put("user",user);


        return model;
    }

    private boolean emailExist(String email) {
        User user = memberRepository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    public Organizer checkCurrentUser(Map<String, Object> model, Integer id, HttpServletResponse resp)
            throws IOException {

        Organizer org = (Organizer) model.get("organizer");

        if ( org == null || !org.getId().equals(id)) {
            resp.sendError(403);
            return null;
        }

        return org;
    }
}