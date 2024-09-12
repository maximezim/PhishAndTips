package com.learnandphish.authentication;

import com.learnandphish.authentication.UserData;
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
import java.util.Collections;
import java.util.stream.Collectors;

/**
 *
 * @author Robin Lafontaine
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDataRepository userDataRepository;

    @Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserData userData = userDataRepository.findByEmail(email)
            .stream()
            .findFirst()
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    return new User(email, userData.getPasswordHash(),
            Collections.singletonList(new SimpleGrantedAuthority(userData.getRole().name())));
}
}