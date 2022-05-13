package jpabook.jpashop.security.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("js082917");
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/csemall?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        return dataSourceBuilder.build();
    }
}
