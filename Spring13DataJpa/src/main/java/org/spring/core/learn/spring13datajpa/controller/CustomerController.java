package org.spring.core.learn.spring13datajpa.controller;


import org.spring.core.learn.spring13datajpa.entity.Customer;
import org.spring.core.learn.spring13datajpa.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/customers")
public class CustomerController {

@Autowired
    private CustomerRepository customerRepository;


@PostMapping
public Customer createCustomer(@RequestBody Customer customer){
    return customerRepository.save(customer);
}


@GetMapping
    public List<Customer> getAllCustomers()
{
   return customerRepository.findAll();
}

@GetMapping("/{id}")
    public Customer getCustomer(@PathVariable Long id)
{
    return customerRepository.findById(id).orElseThrow(() -> new RuntimeException(("Customer not found: " + id ) ));
}

@GetMapping("/search")
public List<Customer> serchByName(@RequestParam String name){
    return customerRepository.findByNameContainingIgnoreCase(name);
}


@DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id){
            customerRepository.deleteById(id);
}




}
