package kavi.example.hello.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

//jwt token is generated and validated in this class
@Component

public class JwtUtil {
    private final String SECRET=" Akavipriya 123456789 kavipriyaA This is your secret, So keep the secret a secret";
    private final long EXPIRATION=1000*60*60;
    // token has to be a Key datatype so for jwt to work
    private final Key secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));


    //seals and generate a token
    public String generateToken(String email)
    {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date (System.currentTimeMillis()+EXPIRATION))
                .signWith(secretKey, SignatureAlgorithm.HS256 ) //digital signature
                .compact();
    }

    public boolean validateJwtToken(String token)
    {
        try
        {
            extractEmail(token);
            return true;
        }
        catch(JwtException ex)
        { return false;}
    }

    public String extractEmail(String token) {
        //opens the seal and cheks if its valid and return email
        // returns email coz token is generated out of email , on extracting we get email
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token) //checks if token is valid
                .getBody()
                .getSubject();
    }
}
