package indi.simple.pms.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import indi.simple.pms.util.JsonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/6 23:29
 * @Description:
 */
@Configuration
public class JacksonConfig {
    @Bean
    @Primary
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper= JsonUtil.getObjectMapper();

        return objectMapper;
    }
}