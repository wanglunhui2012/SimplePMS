package indi.simple.pms.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:38
 * @Description:
 */
@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<Object, Object> redisTemplate;
    private final CacheProperties cacheProperties;
    private String keyPrefix; // @Cache注解默认带有keyPrefix，我们自己的工具需要手动添加

    @PostConstruct
    public void init(){
        keyPrefix=cacheProperties.getRedis().getKeyPrefix();
    }

    public String getKeyPrefix(){
        return this.keyPrefix;
    }

    public void hashPutWithExpire(String key, String hashKey, Object value, long timeout, TimeUnit timeUnit){
        String realKey=keyPrefix+key;

        this.redisTemplate.opsForHash().put(realKey, hashKey, value);
        this.redisTemplate.expire(realKey, timeout, timeUnit);
    }

    public Long hashDelete(String key, List<String> hashKeyList){
        String realKey=keyPrefix+key;

        Long affectRows = this.redisTemplate.opsForHash().delete(realKey,hashKeyList.toArray());
        return affectRows;
    }

    public Object hashGet(String key,String hashKey){
        String realKey=keyPrefix+key;

        return this.redisTemplate.opsForHash().get(realKey,hashKey);
    }

    public Set<Object> hashKeys(String key){
        String realKey=keyPrefix+key;

        return this.redisTemplate.keys(realKey);
    }

    public Object stringGet(String key){
        Object object=this.redisTemplate.opsForValue().get(key);

        return object;
    }

    public boolean keyDelete(String key){
        String realKey=keyPrefix+key;

        boolean flag=this.redisTemplate.delete(realKey);

        return flag;
    }

    public Object keyGet(String key){
        String realKey=keyPrefix+key;

        return this.redisTemplate.opsForValue().get(realKey);
    }

}
