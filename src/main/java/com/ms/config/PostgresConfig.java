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
        basePackages = "com.ms.repository.postgres", // repositories using postgres
        entityManagerFactoryRef = "postgresEntityManager",
        transactionManagerRef = "postgresTransactionManager"
)
public class PostgresConfig {

    // Reads postgres datasource properties from application.yml
    @Bean
    @ConfigurationProperties("spring.datasource.postgres")
    public DataSourceProperties postgresDataSourceProperties() {
        return new DataSourceProperties();
    }

    // Creates postgres datasource
    @Bean
    public DataSource postgresDataSource() {
        return postgresDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    // Entity manager for postgres
    @Bean
    public LocalContainerEntityManagerFactoryBean postgresEntityManager(
            EntityManagerFactoryBuilder builder, @Qualifier("postgresDataSource") DataSource dataSource) {

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "create");

        return builder
                .dataSource(dataSource)
                .packages("com.ms.model.postgres") // entity package
                .persistenceUnit("postgres")
                .properties(props)
                .build();
    }

    // Transaction manager for postgres
    @Bean
    public PlatformTransactionManager postgresTransactionManager(
            @Qualifier("postgresEntityManager")
            LocalContainerEntityManagerFactoryBean entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory.getObject());
    }

}