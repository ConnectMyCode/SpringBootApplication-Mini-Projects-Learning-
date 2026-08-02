package org.spring.core.learn.spring12jdbctemplate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.sql.DataSource;

@Configuration
public class JdbcCallConfig {

    @Autowired
    private DataSource dataSource;


    @Bean
    public SimpleJdbcCall bonusFunctionalCall()
    {
        // withFunctionName() tells Spring which DB function to wrap.
        // withReturnValue() is important for MySQL functions so Spring knows
        // to treat the call as a function (returns a value) not a void procedure.


        return new SimpleJdbcCall(dataSource)
                .withFunctionName("bonus_function")
                .withReturnValue();
    }

}


/*
3. JdbcTemplate is the Universal Interface
Spring Boot auto-configures JdbcTemplate and NamedParameterJdbcTemplate because they provide the standard interface to execute any arbitrary SQL. SimpleJdbcCall is a metadata-driven helper wrapper around them. Spring Boot expects that if you need specialized helpers like SimpleJdbcCall or SimpleJdbcInsert, you will construct them yourself using the auto-configured JdbcTemplate or DataSource.

8:55 AM, 7/28/2026
KEep the answers short and precise and stick to the context only be precise and conceise extremely with use of simple lannguage
8:57 AM, 7/28/2026
SimpleJdbcCall is not auto-registered because:

It is specific to a database function: A SimpleJdbcCall is set up for a specific database function (like "bonus_function" in

JdbcCallConfig.java
).
Spring Boot doesn't know your database functions: Spring Boot cannot predict the names or parameters of your database functions, so it cannot auto-configure them.
JdbcTemplate is generic: Spring Boot only auto-registers JdbcTemplate because it is generic and works for any SQL query.

*/