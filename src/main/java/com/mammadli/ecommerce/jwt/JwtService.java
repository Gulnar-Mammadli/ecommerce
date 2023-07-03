package com.mammadli.ecommerce.jwt;

import com.mammadli.ecommerce.config.MyUserDetailsService;
import com.mammadli.ecommerce.model.User;
import com.mammadli.ecommerce.reposirory.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    private final MyUserDetailsService userDetailsService;

    private final UserRepository userRepository;

    public String generateToken(String userName){
        Map<String,Object> claims = new HashMap<>();
        User user = getUserByName(userName);

        claims.put("roles", user.getRole());
        claims.put("userId", user.getId());
        claims.put("userName", user.getUserName());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(signingKey(), SignatureAlgorithm.HS256).compact();
    }


    private String createToken(Map<String, Object> claims, String userName) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
        String rolesClaim = roles.toString();
        log.info("rolesClaim  {} ", rolesClaim);
        int start = rolesClaim.indexOf("[");
        int end = rolesClaim.indexOf("]");
        rolesClaim = rolesClaim.substring(start + 1, end);
        log.info("claims  {} ", rolesClaim);

        claims.put("roles", rolesClaim);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(signingKey(), SignatureAlgorithm.HS256).compact();
    }

    public User getUserByName(String userName){
        Optional<User> user = userRepository.findByUserName(userName);
        return user.orElse(null);
    }
    public static final String SECRET = "4D6351665468576D5A7134743777217A25432A462D4A614E645267556B586E32";

    private Key signingKey() {
        byte[] keyDecoder = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyDecoder);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(signingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRoles(String token) {
        String claimRoles = extractAllClaims(token).get("roles", String.class);
        return  claimRoles;
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        log.info("extractRoles  {} ", extractRoles(token));
        return (username.equals(userDetails.getUsername())  && !isTokenExpired(token));
    }

    public Boolean validateToken(String token){
        token = token.split(" ")[1];
        return !isTokenExpired(token);
    }
}
