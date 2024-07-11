package com.lpb.mid.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class CustomCacheResolver implements CacheResolver {
    private final CacheManager permissionRedisCacheManager;
    private final CacheManager permissionCacheManager;
    private final CacheProperties cacheProperties;
    public CustomCacheResolver(CacheProperties cacheProperties, CacheManager permissionRedisCacheManager, CacheManager permissionCacheManager) {
        this.permissionRedisCacheManager = permissionRedisCacheManager;
        this.permissionCacheManager = permissionCacheManager;
        this.cacheProperties = cacheProperties;

    }

    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        Collection<Cache> caches = new ArrayList<>();
        Set<String> cacheNames = context.getOperation().getCacheNames();
        if (isEmpty(cacheNames)) return caches;
        String cacheName = cacheNames.toArray()[0].toString();
        if (!cacheProperties.getCacheNames().containsKey(cacheName)){
            caches.add(permissionCacheManager.getCache(cacheName));
            return caches;
        }
        CacheProperties.CacheName cacheProps = cacheProperties.getCacheNames().get(cacheName);
        if (cacheProps.getUseRedis()){
            caches.add(permissionRedisCacheManager.getCache(cacheName));
        } else {
            caches.add(permissionCacheManager.getCache(cacheName));
        }
        return caches;
    }
    public static <T> boolean isEmpty(Collection<T> list) {
        return list == null || list.isEmpty();
    }
}