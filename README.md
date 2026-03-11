# Multi-Database Configuration in Spring Boot (PostgreSQL + Oracle + MongoDB)

This document explains **how a Spring Boot application can connect to multiple databases simultaneously** and how Spring internally routes requests to the correct database.

The example architecture uses:

* **PostgreSQL** → Relational database for user data
* **Oracle** → Relational database for product data
* **MongoDB** → NoSQL database for document/file storage

The goal is to help anyone reading this repository understand **how the configuration works internally**, **how Spring creates beans**, and **how a request ultimately reaches the correct database**.

---

# 1. Overview

In a typical Spring Boot application, there is **one database**.
However, some enterprise applications require **multiple databases** for different data domains.

Example:

| Database   | Purpose            |
| ---------- | ------------------ |
| PostgreSQL | User management    |
| Oracle     | Product management |
| MongoDB    | Document storage   |

Instead of one persistence pipeline, the application contains **three independent persistence pipelines**.

```
Spring Boot Application
        │
 ┌──────┼───────────┐
 │      │           │
Postgres Pipeline  Oracle Pipeline  Mongo Pipeline
```

Each pipeline manages its own:

* Connection
* ORM configuration
* Transaction management
* Repository layer

---

# 2. Application Startup Flow

When the application starts:

```
SpringApplication.run()
```

Spring performs **component scanning** and finds configuration classes annotated with:

```
@Configuration
```

Example configuration classes in this project:

```
JpaCommonConfig
PostgresConfig
OracleConfig
MongoConfig
```

Spring reads these classes and **creates beans inside the Application Context**.

---

# 3. Shared JPA Configuration

Relational databases (PostgreSQL and Oracle) use **JPA with Hibernate**.

To avoid duplicating code, a shared configuration is used.

### JpaCommonConfig

```java
@Configuration
public class JpaCommonConfig {

    @Bean
    public JpaVendorAdapter jpaVendorAdapter(){
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder(
            JpaVendorAdapter jpaVendorAdapter) {

        return new EntityManagerFactoryBuilder(
                jpaVendorAdapter,
                (dataSource) -> new HashMap<>(),
                null
        );
    }
}
```

### What This Does

**JpaVendorAdapter**

Defines which JPA implementation is used.

```
HibernateJpaVendorAdapter
```

Responsibilities:

* SQL dialect detection
* DDL generation
* SQL formatting
* Vendor-specific optimizations

---

**EntityManagerFactoryBuilder**

A helper builder used to create **EntityManagerFactory instances** for each database.

```
EntityManagerFactoryBuilder
       │
       ▼
LocalContainerEntityManagerFactoryBean
       │
       ▼
EntityManagerFactory
```

---

# 4. PostgreSQL Configuration

PostgreSQL uses **Spring Data JPA**.

### PostgresConfig

```java
@Configuration
@EnableJpaRepositories(
        basePackages = "com.ms.repository.postgres",
        entityManagerFactoryRef = "postgresEntityManager",
        transactionManagerRef = "postgresTransactionManager"
)
public class PostgresConfig {
```

This annotation tells Spring:

```
All repositories in:
com.ms.repository.postgres
```

should use:

```
postgresEntityManager
postgresTransactionManager
```

---

## 4.1 DataSource Properties

```java
@Bean
@ConfigurationProperties("spring.datasource.postgres")
public DataSourceProperties postgresDataSourceProperties() {
    return new DataSourceProperties();
}
```

Spring reads values from:

```
application.yml
```

Example:

```
spring:
  datasource:
    postgres:
      url: jdbc:postgresql://localhost:5432/userdb
      username: postgres
      password: password
```

These values are mapped into a `DataSourceProperties` object.

---

## 4.2 DataSource

```java
@Bean
public DataSource postgresDataSource() {
    return postgresDataSourceProperties()
            .initializeDataSourceBuilder()
            .build();
}
```

This creates a **connection pool** using **HikariCP**.

Responsibilities:

* Manage database connections
* Reuse connections efficiently
* Handle JDBC driver communication

---

## 4.3 EntityManagerFactory

```java
@Bean
public LocalContainerEntityManagerFactoryBean postgresEntityManager(
        EntityManagerFactoryBuilder builder,
        @Qualifier("postgresDataSource") DataSource dataSource) {

    Map<String, Object> props = new HashMap<>();
    props.put("hibernate.hbm2ddl.auto", "create");

    return builder
            .dataSource(dataSource)
            .packages("com.ms.model.postgres")
            .persistenceUnit("postgres")
            .properties(props)
            .build();
}
```

This creates the **Hibernate persistence unit**.

Responsibilities:

* Scan entity classes
* Map entities to tables
* Generate SQL queries
* Manage Hibernate sessions

---

## 4.4 Transaction Manager

```java
@Bean
public PlatformTransactionManager postgresTransactionManager(
        @Qualifier("postgresEntityManager")
        LocalContainerEntityManagerFactoryBean entityManagerFactory) {

    return new JpaTransactionManager(entityManagerFactory.getObject());
}
```

Handles:

* Begin transaction
* Commit transaction
* Rollback transaction

Used with:

```
@Transactional
```

---

# 5. Oracle Configuration

Oracle configuration is **almost identical to PostgreSQL**, but uses:

```
OracleDialect
```

and a different repository package.

```
com.ms.repository.oracle
```

This ensures Oracle repositories use the **Oracle datasource and entity manager**.

---

# 6. MongoDB Configuration

MongoDB uses **Spring Data MongoDB** instead of JPA.

### MongoConfig

```java
@Configuration
@EnableMongoRepositories(
        basePackages = "com.ms.repository.mongo"
)
public class MongoConfig {

    @Bean
    public MongoClient mongoClient() {
        String uri = "mongodb+srv://username:password@cluster.mongodb.net/my_mongo_db";
        return MongoClients.create(uri);
    }
}
```

This configuration creates:

```
MongoClient
MongoTemplate
MongoRepository proxies
```

MongoDB stores **documents**, not tables.

Example entity:

```
@Document(collection = "documents")
```

---

# 7. How Spring Chooses the Correct Database

Spring determines the database **based on the repository package**.

Example:

```
com.ms.repository.postgres
```

Uses:

```
postgresEntityManager
postgresDataSource
```

---

```
com.ms.repository.oracle
```

Uses:

```
oracleEntityManager
oracleDataSource
```

---

```
com.ms.repository.mongo
```

Uses:

```
MongoClient
```

---

# 8. Request Flow

When a request arrives:

```
Client
   │
   ▼
Controller
   │
   ▼
Service
   │
   ▼
Repository
   │
   ▼
Database
```

Example request:

```
POST /users
```

Flow:

```
UserController
     ↓
UserService
     ↓
UserRepository
     ↓
PostgreSQL Database
```

Because `UserRepository` is located in:

```
com.ms.repository.postgres
```

---

Another example:

```
POST /products
```

Flow:

```
ProductController
     ↓
ProductService
     ↓
ProductRepository
     ↓
Oracle Database
```

---

Mongo example:

```
POST /documents
```

Flow:

```
DocumentController
     ↓
DocumentService
     ↓
DocumentRepository
     ↓
MongoDB
```

---

# 9. Final Architecture

```
                    Spring Boot Application
                           │
 ┌─────────────────────────┼─────────────────────────┐
 │                         │                         │
PostgreSQL Pipeline   Oracle Pipeline          Mongo Pipeline
 │                         │                         │
DataSource              DataSource                MongoClient
 │                         │                         │
EntityManagerFactory   EntityManagerFactory       MongoTemplate
 │                         │                         │
TransactionManager     TransactionManager          -
 │                         │                         │
Repositories           Repositories              MongoRepositories
 │                         │                         │
Postgres DB              Oracle DB                 MongoDB
```

---

# 10. Best Practices
### Separate Packages by Database

```
model/postgres
model/oracle
model/mongo

repository/postgres
repository/oracle
repository/mongo
```

This keeps database logic **clean and maintainable**.

---

# 11. Summary

This multi-database architecture allows a single Spring Boot application to:

* Connect to **multiple relational databases**
* Use **different persistence technologies**
* Route requests automatically based on repository configuration

Key concepts involved:

* DataSource
* EntityManagerFactory
* TransactionManager
* Repository package scanning
* MongoDB client configuration

This approach is widely used in **large enterprise applications** where different data domains require **different storage technologies**.
