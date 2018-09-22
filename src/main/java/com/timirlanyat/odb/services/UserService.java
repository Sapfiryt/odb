package com.timirlanyat.odb.services;

import com.timirlanyat.odb.dal.repositories.HashRepository;
import com.timirlanyat.odb.dal.repositories.UserRepository;
import com.timirlanyat.odb.exceptions.EmailExistsException;
import com.timirlanyat.odb.model.Hash;
import com.timirlanyat.odb.model.User;
import com.timirlanyat.odb.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private HashRepository hashRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerNewUserAccount(UserDto accountDto)
            throws EmailExistsException {

        if (emailExist(accountDto.getEmail())) {
            throw new EmailExistsException( accountDto.getEmail());
        }

        User user = new User();
        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setEmail(accountDto.getEmail());
        user.setRoles(Arrays.asList("REC_USER"));

        user=userRepo.save(user);

        hashRepo.save(new Hash().setUser(user)
                .setHash(passwordEncoder.encode(accountDto.getPassword())));

        return user;

    }
    private boolean emailExist(String email) {
        User user = userRepo.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }
}