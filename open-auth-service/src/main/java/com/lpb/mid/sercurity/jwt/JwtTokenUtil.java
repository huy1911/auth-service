package com.lpb.mid.sercurity.jwt;

import com.lpb.mid.model.request.LoginRequest;
import com.lpb.mid.model.response.JwtDTOResponse;
import com.lpb.mid.service.UserPrincipal;
import com.lpb.mid.utils.DateUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Value("${security.jwt.secret:}")
    private String jwtSecret;

    @Value("${security.jwt.expiration:}")
    private int jwtExpirationMs;

    @Cacheable(
            cacheNames = "JwtDTOResponse",
            unless = "#result == null",
            value = "JwtDTOResponse",
            cacheResolver = "customCacheResolver"
    )
    public JwtDTOResponse generateJwtToken(Authentication authentication, LoginRequest loginRequest) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date dateExp = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(jwtExpirationMs));
        String jwt = Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setId(userPrincipal.getUsername() + userPrincipal.getAppId() + System.currentTimeMillis())
                .setIssuedAt(new Date())
                .setExpiration(dateExp)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .claim("roles", userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .claim("appId", userPrincipal.getAppId())
                .claim("whiteListIp", userPrincipal.getWhiteListIp())
                .claim("userName", userPrincipal.getUsername())
                .claim("customerNo", loginRequest.getCustomerNo())
                .claim("branchCode",loginRequest.getBranchCode())
                .compact();
        return JwtDTOResponse.builder()
                .exp(DateUtils.convertStringToLocalDateTime(dateExp))
                .jwt(jwt)
                .build();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
