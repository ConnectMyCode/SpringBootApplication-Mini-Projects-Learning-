package org.spring.core.learn.spring12jdbctemplate.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;





@Repository
public class BonusRepository {


    @Autowired
    private SimpleJdbcCall bonusFunctionCall;

    public double calculateBonus(int empno)
    {
        // executeFunction(returnType, inputParams...) -> calls the DB function
        // and maps the returned scalar value into the given Java type.

        return bonusFunctionCall.executeFunction(Double.class, empno);
    }

}
