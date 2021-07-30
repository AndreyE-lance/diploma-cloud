package com.elantsev.netology.diplomacloud.config.jwt;

import com.elantsev.netology.diplomacloud.model.Authority;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    @Value("${jwt.token.secret}")
    private String salt;
    @Value("${jwt.token.expired}")
    private long expiredMs;

    @PostConstruct
    protected void init(){
        salt = Base64.getEncoder().encodeToString(salt.getBytes());
    }

    public String createToken(String username, List<Authority> authorityList){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles",getAuthorityNames(authorityList));
        Date now = new Date();
        Date validity = new Date(now.getTime()+expiredMs);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256,salt)
                .compact();
    }

    public Authentication getAuthentication(String token){
        return null;
    }

    public String getUserName(String token){
        return Jwts.parser().setSigningKey(salt).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req){
        String bearerToken = req.getHeader("Authorization");
        if(bearerToken!=null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7,bearerToken.length());
        }
        return null;
    }

    public boolean validateToken(String token){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(salt).parseClaimsJws(token);
            if(claims.getBody().getExpiration().before(new Date())){
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e){
            ///
        }

        return false;
    }

    public List<String> getAuthorityNames(List<Authority> authorityList){
        List<String> result = new ArrayList<>();
        authorityList.forEach(auth -> result.add(auth.getId().getAuthority()));
        return result;
    }
}
