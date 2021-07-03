package indi.simple.pms.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus配置类
 */
@Configuration
@MapperScan(value = "indi.simple.pms.mapper")
public class MybatisPlusConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor(){// mybatis-plus分页插件，没有的话不能进行分页
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType(DbType.MYSQL.getDb());// 设置数据库方言为mysql，可以不用设置，自定根据jdbc url来识别
        return page;
    }

    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() { // 乐观锁插件
        return new OptimisticLockerInterceptor();
    }
}
