========================
Spring Boot + JDBC
========================

=> JDBC : Java Database Connectivity

=> JDBC Technology allows A Java application to perform CRUD operations.

=> C(Create) R(Read) U(Update) D(Delete)

=> The Steps required to Perform a Database operation(s) using JDBC Technology:
Step-1: Load the JDBC Driver

          Step-2: Establish a Connection

          Step-3: Create a Statement 

          Steo-4: Execute the SQL

          Step-5: Process the result

          Step-6: Close the connection.

=> Each program/application requires the same above steps to perform a Database operation.

=> This is a repeatative code, also called as Boiler-plate code.

=> In Spring Boot + JDBC, The Spring Boot will automatically handles this boiler-plate code.

=> As a developer, you need to tell the Spring Boot about,
** what SQL query you want to execute,

             ** What you want to do with the result.


==================
JdbcTemplate class
==================

      => It is the main class/central class in the Spring Boot JDBC API. It provides
         the methods to perform SQL operations.

      => JdbcTemplate class depends on the DataSource object.

      => As a developer, you have to do 2 things.

         1) add  "spring-boot-starter-jdbc" dependency in pom.xml

         2) configure the datasource properties in application.properties file.

      => By looking at datasource properties, Spring Boot will construct a DataSource object.

      => By looking at "spring-boot-starter-jdbc" dependency, Spring Boot registers a JdbcTemplate 
         bean in the Spring ApplicationContext container.

      => To run the SQL queries from your repository bean, you have to invoke the methods of
         the JdbcTemplate class.
    
    /**
    * This class demonstrates EVERY JdbcTemplate method from your notes:
    *   1) execute()        -> DDL  (CREATE TABLE)
    *   2) update()         -> DML  (INSERT / DELETE)
    *   3) queryForMap()     -> single row as Map
    *   4) queryForList()    -> multiple rows as List<Map>
    *   5) queryForObject()  -> single scalar value
    *   6) query() + RowMapper<T> -> multiple rows as List<Employee> (Java objects, not Maps)
           */
    

    
    ========================================================================

    What is RowMapper<T> interface?
    ===============================
       -- It is an interface provided by Spring JDBC API, and it is used to convert
          each row from Database into a Java object.

       -- By default, JdbcTemplate class maps/converts each row from a Database into a Map object.

       -- But, we want to convert it into a Java object, not into a Map object.

       -- Then we have to use RowMapper<T> interface.

       -- RowMapper<T> is a functional interface, it has a single abstract method.
               public interface RowMapper<T> {
                     public abstract  T  mapRow(ResultSet rs, int index);
               }

    
    =======================================================================

    SimpleJdbcCall class:               
    â€¢   This class is provided in Spring JDBC module, to simplify the execution
    of a stored procedure and functions in a Database.
    
    â€¢   SimpleJdbcCall abstracts all the complexities of CallableStatement of JDBC
    and makes it easy to call a procedure or a function.
    
    â€¢   When spring-boot-starter-jdbc dependency is added to pom.xml,
    the Spring Boot will assume that the application is going to perform
    some SQL operations and Spring Boot will automatically registers
    JdbcTemplate and DataSource beans in the application context.
    
    â€¢   So, we need to explicity register SimpleJdbcCall object as a bean in
    application context. It can be done by defining @Bean method in
    a @Configuration class.