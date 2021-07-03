package indi.simple.pms.support.redis;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/24 23:44
 * @Description: 默认RedisCache构造器私有，这里继承一下就能使用
 */
public class MyRedisCache extends RedisCache {
    public MyRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
    }
}
