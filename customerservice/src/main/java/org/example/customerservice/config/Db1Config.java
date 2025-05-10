package org.example.customerservice.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import static io.r2dbc.spi.ConnectionFactoryOptions.USER;
import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;

@Configuration
@EnableR2dbcRepositories(
        basePackages = "org.example.customerservice.repository.db1",
        entityOperationsRef = "db1R2dbcEntityOperations"
)
public class Db1Config {

    @Bean
    public ConnectionFactory db1ConnectionFactory(@Value("${spring.r2dbc.primary.url}") String url,
                                                  @Value("${spring.r2dbc.primary.username}") String username,
                                                  @Value("${spring.r2dbc.primary.password}") String password) {
//        return ConnectionFactories.get(url + "?user=" + username + "&password=" + password);
        ConnectionFactoryOptions baseOptions = ConnectionFactoryOptions.parse(url);
        ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
                .from(baseOptions)
                .option(USER, username)
                .option(PASSWORD, password)
                .build();

        return ConnectionFactories.get(options);
    }

    @Bean
    public R2dbcEntityOperations db1R2dbcEntityOperations(@Qualifier("db1ConnectionFactory") ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }
}
