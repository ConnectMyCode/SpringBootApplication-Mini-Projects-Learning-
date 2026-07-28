package org.spring.core.learn.spring12jdbctemplate.runner;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnectivityHealth implements ApplicationRunner {


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {

                // Intention: verify DB connectivity at application startup,
                // BEFORE any repository/business logic tries to use it.
                // "SELECT 1" is a standard, table-independent query used purely
                // to test that a connection can be opened and a query executed.
                try {
                    Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

                    if (result != null && result == 1) {
                        System.out.println("✅ Database connectivity check PASSED - connection is healthy.");
                    } else {
                        System.out.println("⚠️ Database connectivity check returned unexpected result: " + result);
                    }

                } catch (Exception e) {
                    // Catching broadly here on purpose: at this early health-check stage,
                    // we just want to know something is wrong and what it is —
                    // not handle specific SQL exception subtypes yet.
                    e.printStackTrace();
                    System.out.println("❌ Database connectivity check FAILED: " + e.getMessage());
                }
            }
        }

