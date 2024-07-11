package com.lpb.mid.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ConditionalOnProperty(
        name = {"lpb.cache.enabled"},
        matchIfMissing = true
)
@EnableConfigurationProperties({CacheProperties.class})
@Configuration
@EnableCaching
@Log4j2
public class CacheManagerConfig {
    @Autowired
    private CacheProperties cacheProperties;
    @Autowired
    private LettuceConnectionFactory lettuceConnectionFactory;
    @Bean(name =  "redisCacheManager")
    public CacheManager redisCacheManager() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
        if (cacheProperties.getCacheNames() != null && cacheProperties.getCacheNames().size() > 0){
            cacheProperties.getCacheNames().forEach((s, cacheName) -> {
                cacheConfigs.put(s, redisCacheConfiguration.entryTtl(Duration.ofMinutes(cacheName.getExpireTime())));
            });
        }
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(lettuceConnectionFactory)
                .withInitialCacheConfigurations(cacheConfigs)
                .cacheDefaults(redisCacheConfiguration).build();
    }
    @Bean(name = "cacheManager")
    @Primary
    public CacheManager cacheManager(Ticker ticker) {
        List<CaffeineCache> caches = new ArrayList<>();
        if (cacheProperties.getCacheNames() != null && cacheProperties.getCacheNames().size() > 0) {
            cacheProperties.getCacheNames().forEach((s, cacheName) -> {
                caches.add(this.buildCache(s, ticker, cacheName.getExpireTime()));
            });
        }

        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(caches);
        return manager;
    }
    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }
    private CaffeineCache buildCache(String name, Ticker ticker, long secondsToExpire) {
        if (secondsToExpire <= 0) {
            log.info("[CACHE] No Expired Time setup");
            return new CaffeineCache(name, Caffeine.newBuilder().ticker(ticker).build());
        }
        log.info("[CACHE] Expired Time: {}", secondsToExpire);
        return new CaffeineCache(name, Caffeine.newBuilder().expireAfterWrite(secondsToExpire, TimeUnit.SECONDS).ticker(ticker).build());
    }
    @Bean(name =  "customCacheResolver")
    public CacheResolver cacheResolver(Ticker ticker) {
        return new CustomCacheResolver(cacheProperties, redisCacheManager(), cacheManager(ticker));
    }
}
