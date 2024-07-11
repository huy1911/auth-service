package com.lpb.mid.service;

import com.lpb.mid.entity.RefreshToken;
import com.lpb.mid.repo.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {
    @Value("${security.jwt.jwtExpirationMs:}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    //    @Cacheable(
//            value = "refreshToken",
//            key = "#userId + #token",
//            unless = "#result == null",
//            cacheManager = "redisCacheManager"
//    )
    public RefreshToken createRefreshToken(String userId, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .id(UUID.randomUUID().toString())
                .expiredDate(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(refreshTokenDurationMs)))
                .token(token)
                .userId(userId)
                .status("O")
                .build();
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

}
