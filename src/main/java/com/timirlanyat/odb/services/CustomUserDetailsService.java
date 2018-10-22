package com.timirlanyat.odb.services;

import com.timirlanyat.odb.dal.repositories.HashRepository;
import com.timirlanyat.odb.dal.repositories.MemberRepository;
import com.timirlanyat.odb.dal.repositories.OrganizerRepository;
import com.timirlanyat.odb.model.Organizer;
import com.timirlanyat.odb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    private MemberRepository userRepo;
    @Autowired
    private HashRepository hashRepo;
    @Autowired
    private OrganizerRepository organizerRepository;


    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepo.findByEmail(email);

        if (user == null) throw new UsernameNotFoundException(
                "No user found with username: " + email);

        Optional<Organizer> queryResult = organizerRepository.findById(user.getId());
        if(queryResult.isPresent())
            user = queryResult.get();



        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;


        return  new org.springframework.security.core.userdetails.User
                (user.getEmail(),
                        hashRepo.findById(user.getId()).get().getHash(), enabled, accountNonExpired,
                        credentialsNonExpired, accountNonLocked,
                        getAuthorities(user.getRoles()));
    }

    private static List<GrantedAuthority> getAuthorities (List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
