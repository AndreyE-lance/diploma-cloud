package com.elantsev.netology.diplomacloud.service;

import com.elantsev.netology.diplomacloud.config.jwt.JwtTokenProvider;
import com.elantsev.netology.diplomacloud.dto.Login;
import com.elantsev.netology.diplomacloud.repository.AuthoritiesRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;


@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthoritiesRepository authoritiesRepository;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, AuthoritiesRepository authoritiesRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authoritiesRepository = authoritiesRepository;
    }

    public String getToken(Login login){
        try {
            Authentication authResult = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getLogin(), login.getPassword()));
            System.out.println(authResult.isAuthenticated());
            if(authResult!=null){
                String token = jwtTokenProvider.createToken(login.getLogin(),authoritiesRepository.findAuthoritiesById_Username(login.getLogin()));
                authoritiesRepository.findAuthoritiesById_Username(login.getLogin()).forEach(a-> System.out.println(a.toString()));
                System.out.println(jwtTokenProvider.getUserName(token));
                return token;
            }
        } catch (Exception e){//тут нужны эксепшны
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Boolean removeToken(String authToken) {
        return true;
    }
}
