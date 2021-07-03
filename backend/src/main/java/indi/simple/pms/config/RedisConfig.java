package indi.simple.pms.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import indi.simple.pms.support.redis.MyRedisCacheManager;
import indi.simple.pms.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 21:30
 * @Description:
 * {@link org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration}@Import并配置了LettuceConnectionConfiguration
 */
@EnableCaching // 开启Redis注解支持
@Configuration
@Slf4j
@EnableConfigurationProperties(CacheProperties.class)// 开启CacheProperties，否则下面注入失败，RedisCacheConfiguration中在@Conditional(CacheCondition.class)自动开启了CacheProperties
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * 自定义CacheManager
     * 可以重写{@link org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration#cacheManager}
     * 里面的RedisCacheConfiguration被ObjectProvider修饰，如果未配置则调用determineConfiguration()，里面的ObjectProvider.getIfAvailable()就是不存在则createConfiguration()创建RedisCacheConfiguration
     * 如果这里配置了，则里面的ObjectProvider.getIfAvailable()就是存在则不调用createConfiguration()即不创建RedisCacheConfiguration
     * 也可以创建CacheManagerCustomizer的Bean，进行Cache的后置处理，但是没有方法科修改CacheManager
     * @Bean
     * public CacheManagerCustomizer<RedisCacheManager> redisCacheManagerCacheManagerCustomizer(){
     *      return cacheManager -> {
     *          cacheManager.getCacheConfigurations().put("t1",RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(2)));
     *          cacheManager.getCacheConfigurations().put("t2",RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(2)));
     *     };
     * }
     */
    @Bean
    RedisCacheManager cacheManager(CacheProperties cacheProperties,
                                   RedisConnectionFactory redisConnectionFactory,
                                   GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer) {
        RedisCacheConfiguration redisCacheConfiguration=createConfiguration(cacheProperties, genericJackson2JsonRedisSerializer, Duration.ofDays(1)); // 默认1天
        /*// 解析超时时间的第一种方式是默认是一个配置文件，而不同超时时间的是其他的超时时间不同的配置文件，另一种方法是自定义MyRedisCacheManager并在里面解析超时时间，本项目采用第2种
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(redisCacheConfiguration);
        // t1和t2缓存名不是默认的1天，而是1分钟和2分钟
        builder.withCacheConfiguration("t1",redisCacheConfiguration.entryTtl(Duration.ofMinutes(1)));// entryTtl()方法返回的是一个新的RedisCacheConfiguration，所以不用再调用createConfiguration()
        builder.withCacheConfiguration("t2",redisCacheConfiguration.entryTtl(Duration.ofMinutes(2)));// entryTtl()方法返回的是一个新的RedisCacheConfiguration，所以不用再调用createConfiguration()
        return builder.build();*/
        RedisCacheWriter redisCacheWriter=RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        return new MyRedisCacheManager(redisCacheWriter,redisCacheConfiguration);
    }
    private RedisCacheConfiguration createConfiguration(CacheProperties cacheProperties, GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer, Duration duration) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        /**
         * 修改{@link org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration#createConfiguration}的序列化器，默认JdkSerializationRedisSerializer
         */
        //config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new JdkSerializationRedisSerializer(classLoader)));
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericJackson2JsonRedisSerializer));
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }else{
            config = config.entryTtl(duration);
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }

        return config;
    }

    /**
     * {@link org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration#redisTemplate}
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,StringRedisSerializer stringRedisSerializer,GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        template.setConnectionFactory(redisConnectionFactory);
        template.setEnableTransactionSupport(false); // 默认关闭事务支持
        return template;
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        return stringRedisSerializer;
    }
    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
        ObjectMapper objectMapper = JsonUtil.newObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, true);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        return genericJackson2JsonRedisSerializer;
    }

    /**
     * 默认定义了{@link org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration#cacheManager}
     * 定义多个CacheManager见https://www.baeldung.com/spring-multiple-cache-managers
     */
    //@Bean
    @Override
    public CacheManager cacheManager(){
        return super.cacheManager();
    }

    /**
     * 定义多个CacheResolver同上
     */
    //@Bean
    @Override
    public CacheResolver cacheResolver() {
        return super.cacheResolver();
    }
    /**
     * 自定义键生成器
     * 默认是SimpleKey+[方法参数]
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(target.getClass().getSimpleName());
            stringBuilder.append(".");
            stringBuilder.append(method.getName());
            stringBuilder.append("(");
            stringBuilder.append(StringUtils.arrayToDelimitedString(params,","));
            stringBuilder.append(")");
            return stringBuilder.toString();
        };
    }
    /**
     * 自定义错误处理器
     */
    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                log.error("Redis get error,key:{}", key);
                e.printStackTrace();
            }
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                log.error("Redis put error,key:{},value:{}", key, value);
                e.printStackTrace();
            }
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                log.error("Redis evict error,key:{}", key);
                e.printStackTrace();
            }
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                log.error("Redis clear error");
                e.printStackTrace();
            }
        };
    }
}
