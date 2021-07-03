package indi.simple.pms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;


@SpringBootApplication
public class SimplePMSApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimplePMSApplication.class, args);
    }

}
