package com.moyao.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.moyao.generator.runtime.GenericDaoInterceptor;

@Configuration
public class MybatisConfig {

     @Bean
     public ConfigurationCustomizer customizer(){
         return configuration -> configuration.addInterceptor(new GenericDaoInterceptor(configuration.getMapperRegistry()));
     }
}
