package indi.simple.pms.config;

import indi.simple.pms.util.SpringUtil;
import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 21:31
 * @Description:
 */
public class MyBatisRedisCache implements Cache {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisRedisCache.class);
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final String id;
    private RedisTemplate<String, Object> redisTemplate;
    private static final long EXPIRE_TIME_IN_MINUTES = 30L;

    public MyBatisRedisCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("缓存实例必须有id!");
        } else {
            this.id = id;
        }
    }

    public String getId() {
        return this.id;
    }

    public void putObject(Object key, Object value) {
        LOGGER.debug("增加缓存,key:{},value:{}", new Object[]{key.toString(), value, this.toString()});

        try {
            RedisTemplate redisTemplate = this.getRedisTemplate();
            ValueOperations opsForValue = redisTemplate.opsForValue();
            opsForValue.set(key, value, 30L, TimeUnit.MINUTES);
        } catch (Throwable var5) {
            LOGGER.error("增加缓存失败!", var5);
        }

    }

    public Object getObject(Object key) {
        LOGGER.debug("获取缓存,key:{}", key.toString());

        try {
            RedisTemplate redisTemplate = this.getRedisTemplate();
            ValueOperations opsForValue = redisTemplate.opsForValue();
            return opsForValue.get(key.toString());
        } catch (Throwable var4) {
            LOGGER.error("获取缓存失败!", var4);
            return null;
        }
    }

    public Object removeObject(Object key) {
        LOGGER.debug("删除缓存,key:{}", key.toString());
        Object object = null;

        try {
            RedisTemplate redisTemplate = this.getRedisTemplate();
            object = redisTemplate.opsForValue().get(key.toString());
            redisTemplate.delete(key.toString());
        } catch (Throwable var4) {
            LOGGER.error("删除缓存失败!", var4);
        }

        return object;
    }

    public void clear() {
        LOGGER.debug("清空缓存");
        RedisTemplate redisTemplate = this.getRedisTemplate();
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return null;
            }
        });
    }

    public int getSize() {
        LOGGER.debug("获取缓存实例大小");
        return 0;
    }

    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }

    private RedisTemplate<String, Object> getRedisTemplate() {
        if (this.redisTemplate == null) {
            this.redisTemplate = (RedisTemplate) SpringUtil.getBean("redisTemplate");
        }

        return this.redisTemplate;
    }
}
