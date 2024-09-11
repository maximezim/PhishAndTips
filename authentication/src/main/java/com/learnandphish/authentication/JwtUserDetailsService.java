package com.learnandphish.authentication;

import com.learnandphish.authentication.dao.UserData;
import com.learnandphish.authentication.dao.Dao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 *
 * @author Robin Lafontaine
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        System.out.println("user:"+ user);

        if (user != null) {
            Collection<String> mappedAuthorities = Arrays.asList(userData.getRole().split(","));
            // if not using role based authentication, we can pass empty List instead of mappedAuthorities
            User user = new User(email, new BCryptPasswordEncoder().encode(userData.getPassword()), mappedAuthorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
            return user;
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}