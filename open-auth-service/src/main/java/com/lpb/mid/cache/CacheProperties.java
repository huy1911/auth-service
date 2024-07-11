package com.lpb.mid.cache;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@ConfigurationProperties(
        prefix = "lpb.cache"
)
@Data
@NoArgsConstructor
public class CacheProperties {
    private Boolean enabled = true;
    private Map<String, CacheName> cacheNames = new LinkedHashMap<>();

    @Data
    @NoArgsConstructor
    public static class CacheName {
        private Boolean useRedis = false;
        private Long expireTime;
    }
}