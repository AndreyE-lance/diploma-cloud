package com.elantsev.netology.diplomacloud.service;

import com.elantsev.netology.diplomacloud.config.jwt.JwtTokenProvider;
import com.elantsev.netology.diplomacloud.dto.Login;
import com.elantsev.netology.diplomacloud.exception.ErrorBadCredentials;
import com.elantsev.netology.diplomacloud.repository.AuthoritiesRepository;
import com.elantsev.netology.diplomacloud.utils.CloudLogger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthoritiesRepository authoritiesRepository;

    private final CloudLogger cloudLogger;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, AuthoritiesRepository authoritiesRepository, CloudLogger cloudLogger) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authoritiesRepository = authoritiesRepository;
        this.cloudLogger = cloudLogger;
    }

    public String getToken(Login login) {
        try {
            Authentication authResult = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getLogin(), login.getPassword()));
            String token = jwtTokenProvider.createToken(login.getLogin(), authoritiesRepository.findAuthoritiesById_Username(login.getLogin()));
            authoritiesRepository.findAuthoritiesById_Username(login.getLogin()).forEach(a -> System.out.println(a.toString()));
            cloudLogger.logInfo("Token created successfully for "+login.getLogin());
            return token;
        } catch (AuthenticationException e) {
            cloudLogger.logError("Bad credentials for "+login.getLogin());
            throw new ErrorBadCredentials("Service said: Bad credentials!");
        }

    }

    public Boolean removeToken(String authToken) {
        String userName = jwtTokenProvider.getUserName(authToken.substring(7));
        cloudLogger.logInfo("Token deleted successfully for " + userName);
        return true;
    }
}
