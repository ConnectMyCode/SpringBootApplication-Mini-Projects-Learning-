package org.spring.core.learn.spring13datajpa.controller;



import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.spring.core.learn.spring13datajpa.entity.Customer;
import org.spring.core.learn.spring13datajpa.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


/*WebMvc Annotation will only mock the WEB layer and not the Service and Repository .   Here purpose is to only test if the right endpoint is being hit or not
    So no real Service layer methods and repository are getting hit .



/**
 * CONCEPT: @WebMvcTest(CustomerController.class) -> Spring boots ONLY the web
 * layer needed to test this one controller: Spring MVC's DispatcherServlet,
 * JSON message converters, etc. It does NOT boot @Service beans, does NOT
 * connect to a real database, and does NOT load the full application context.
 * This makes it fast, while still exercising the real HTTP request pipeline
 * (unlike a plain unit test, which would call controller methods directly
 * as Java method calls, bypassing routing/serialization entirely).
 */





@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {


    // CONCEPT: MockMvc -> lets us send a simulated HTTP request (GET/POST/etc)
    // and assert on the actual HTTP response, without starting a real server.
    @Autowired
    private MockMvc  mockMvc;

    // CONCEPT: @MockBean -> replaces the REAL CustomerRepository bean in the
    // Spring context with a Mockito mock, for the duration of this test class
   @Mock
   private CustomerRepository customerRepository;

   @Autowired
    private ObjectMapper objectMapper;  // auto-configured by Spring Boot for JSON (de)serialization


    @Test
    @DisplayName("GET /api/customers -> 200 ok returns a list of customers List<Customers>")
    void getAllCustomers_returnsList() throws Exception {
        Customer c1 = new Customer(1L, "Ravi Kumar", "ravi@example.com", new ArrayList<>());
        Customer c2 = new Customer(2L, "Sneha Rao", "sneha@example.com", new ArrayList<>());

        List<Customer> listOFCustomer= List.of(c1, c2);

        when(customerRepository.findAll()).thenReturn(listOFCustomer);


        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$[0].name").value("Ravi Kumar"))
                .andExpect( jsonPath("$[1].email").value("sneha@example.com"))
                .andExpect(jsonPath("$.length()").value(2));

    }


    @Test
    @DisplayName("GET /api/customers/{id} -> 200 OK with the matching customer")
    void getCustomerById_found() throws Exception {
        Customer c1 = new Customer(1L, "Ravi Kumar", "ravi@example.com", new ArrayList<>());

        when(customerRepository.findById(1L)).thenReturn(Optional.of(c1));

        mockMvc.perform(get("/api/customers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ravi Kumar"));
    }
//💖
    @Test
    @DisplayName("POST api/customers -> 200 OK, saves and echoes back the created customer")
    void createCustomer_savesAndReturnsCustomer() throws Exception
    {
        Customer toSave = new Customer(null, "New Customer", "new@example.com", new ArrayList<>());
        Customer saved = new Customer(3L, "New Customer", "new@example.com", new ArrayList<>());
        when(customerRepository.save(any(Customer.class))).thenReturn(saved);

      /*  in method its expecting  @RequestBody means a Json Body as an Request of Customer Type.
        Using ObjectMapper method :writeValueAsString we are turning our Java Object(toSave) into jsonBody similar to what POSTMAN would send:

        // CONCEPT: objectMapper.writeValueAsString -> turns our Java object into
        // the JSON string we send as the request body, mirroring what a real
        // frontend/Postman client would send.
       */

        mockMvc.perform(post("/app/customers")
                .contentType(MediaType.APPLICATION_JSON)     //Here we are giving a Clue(called as "Content-Type header" ) what type of data RequestBody is If it is JSON Type then MappingJackson2HttpRequester Converted is used to convert from JsonBody into Object ...
                .content(objectMapper.writeValueAsString(toSave)))   //In here ObjectMapper is a part of Jackson library this where actual conversion takes place
                .andExpect(status().isOk())    //By the time the Method inside the controller is hit i.e.,{customerRepository.save(customer)}  the Jsom body is already converted into an {Customer customer} type Object...
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("New Customer"));

        verify(customerRepository, times(1)).save(any(Customer.class));
    }


    @Test
    @DisplayName("GET /app/customers/search?name=rav -> delegates to method-name query")
    void searchByName_delegatesToRepository() throws Exception {
        Customer c1 = new Customer(1L, "Ravi Kumar", "ravi@example.com", new ArrayList<>());
        when(customerRepository.findByNameContainingIgnoreCase("ravi")).thenReturn(List.of(c1));


        mockMvc.perform(get("app/customers/search").param("name", "ravi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ravi Kumar"));

        verify(customerRepository).findByNameContainingIgnoreCase("ravi");
    }


    @Test
    @DisplayName("DELETE /api/customers/{id} -> 200 OK, delegates to deleteById")
    void deleteCustomer_delegatesToRepository() throws Exception {
        mockMvc.perform(delete("api/customers/{id}", 1L))
                .andExpect(status().isOk());

        verify(customerRepository, times(1)).deleteById(1L);
    }




    }



