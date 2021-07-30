package com.elantsev.netology.diplomacloud.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService  {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
         return null;
    }
}
