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
        dataSourceBuilder.password("a8798233a");
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/csemall?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC");
        return dataSourceBuilder.build();
    }
}
