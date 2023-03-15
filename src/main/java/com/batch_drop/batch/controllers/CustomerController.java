package com.batch_drop.batch.controllers;

import com.batch_drop.batch.dao.CustomerRepository;
import com.batch_drop.batch.pojo.Customer;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerRepository repository;
    public CustomerController(CustomerRepository repository){
        this.repository = repository;
    }

    @GetMapping
    public List<Customer> all(){
        return repository.findAll();
    }
    @PostMapping
    public Customer newCustomer(@RequestBody Customer customer){
        return repository.save(customer);
    }
}
