package indi.simple.pms.support.redis;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;

import java.time.Duration;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/24 23:52
 * @Description:
 */
public class MyRedisCacheManager extends RedisCacheManager {
    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration defaultCacheConfig;

    /**
     * 重写，返回MyRedisCache
     * 解析超时时间不能在{@link MyRedisCache#put(Object, Object)}中重写，因为在里面解析后还需要设置name比较麻烦，在这里重写name即可
     */
    @Override
    protected RedisCache createRedisCache(String name, @Nullable RedisCacheConfiguration cacheConfig) {
        RedisCacheConfiguration redisCacheConfiguration=cacheConfig != null ? cacheConfig : defaultCacheConfig;
        if(name.contains("#")){
            String[] strings=name.split("#");
            name=strings[0];
            redisCacheConfiguration=redisCacheConfiguration.entryTtl(Duration.parse(strings[1]));
        }
        return new MyRedisCache(name, cacheWriter, redisCacheConfiguration);
    }

    public MyRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }
}
