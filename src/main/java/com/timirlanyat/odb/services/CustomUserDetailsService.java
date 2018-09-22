package com.timirlanyat.odb.services;

import com.timirlanyat.odb.dal.repositories.HashRepository;
import com.timirlanyat.odb.dal.repositories.UserRepository;
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

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository userRepo;
    @Autowired
    private HashRepository hashRepo;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepo.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException(
                "No user found with username: " + email);

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