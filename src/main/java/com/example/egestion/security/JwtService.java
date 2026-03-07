package com.example.egestion.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
@Component
public class JwtService {
    @Value("${app.jwt.key:AUCUNE_VALEUR}")
    private String encodedkey;
    private  SecretKey key ;
    @PostConstruct
    public void init() {
        if (encodedkey == null || encodedkey.equals("AUCUNE_VALEUR")) {
            throw new RuntimeException("La clé JWT n'a pas été trouvée dans la configuration !");
        }
        byte[] keyBytes = Decoders.BASE64.decode(encodedkey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    public String jwtGenerator(String username){

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();

    }
    public String getUsername(String jwtToken){
        return Jwts.parser()
                .verifyWith(key)
                .build().parseSignedClaims(jwtToken)
                .getPayload()
                .getSubject();
    }
}
