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
        return bonusFunctionCall.executeFunction(Double.class, empno);
    }

}
