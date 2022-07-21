package org.example.javaConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Configuration
@ComponentScan(basePackages = "org.example",useDefaultFilters = false,includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,classes = Service.class),@ComponentScan.Filter(type = FilterType.ANNOTATION,classes = Repository.class)})
//@ComponentScan(basePackages = "org.example.service",useDefaultFilters = false)
public class JavaConfig {
    @Bean
    SayHello sayHello() {
        return new SayHello();
    }
}
