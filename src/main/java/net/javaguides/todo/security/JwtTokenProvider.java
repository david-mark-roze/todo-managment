package net.javaguides.todo.security;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String secret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long expirationMillis;

    public String generateToken(Authentication authentication){
        String userName = authentication.getName();
        Date currentDate = new Date();
        // The expiration date is the current date plus the amount of expiration time in milliseconds
        Date expiryDate = new Date(currentDate.getTime() + expirationMillis);

        return Jwts.builder().setSubject(userName).
                issuedAt(currentDate).
                expiration(expiryDate).
                signWith(key()).
                compact();
    }

    public String getUserNameFromToken(String token){
        return parser().
                parseSignedClaims(token).
                getPayload().getSubject();
    }

    public boolean validateToken(String token){
        parser().parse(token);
        // An exception will be thrown from the parse(..) method if the token is not valid
        return true;
    }

    private SecretKey key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    private JwtParser parser(){
        return Jwts.parser().
                verifyWith(key()).
                build();
    }
}
