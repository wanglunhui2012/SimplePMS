package indi.simple.pms.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 21:30
 * @Description:
 */
@Configuration
public class ValidationConfig {
    public ValidationConfig() {
    }

    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation
                .byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory();

        return validatorFactory.getValidator();
    }
}
