package com.ms.config;

import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.ms.repository.oracle", // oracle repositories
        entityManagerFactoryRef = "oracleEntityManager",
        transactionManagerRef = "oracleTransactionManager"
)
public class OracleConfig {

    // Reads oracle datasource properties
    @Bean
    @ConfigurationProperties("spring.datasource.oracle")
    public DataSourceProperties oracleDataSourceProperties() {
        return new DataSourceProperties();
    }

    // Oracle datasource bean
    @Bean
    public DataSource oracleDataSource() {
        return oracleDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    // Oracle entity manager
    @Bean
    public LocalContainerEntityManagerFactoryBean oracleEntityManager(
            EntityManagerFactoryBuilder builder, @Qualifier("oracleDataSource") DataSource dataSource) {

        Map<String,Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto","create");
        props.put("hibernate.dialect","org.hibernate.dialect.OracleDialect");

        return builder
                .dataSource(dataSource)
                .packages("com.ms.model.oracle") // oracle entities
                .persistenceUnit("oracle")
                .properties(props)
                .build();
    }

    // Oracle transaction manager
    @Bean
    public PlatformTransactionManager oracleTransactionManager(
            @Qualifier("oracleEntityManager")
            LocalContainerEntityManagerFactoryBean entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory.getObject());
    }

}