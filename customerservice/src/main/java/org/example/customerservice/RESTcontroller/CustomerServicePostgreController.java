package org.example.customerservice.RESTcontroller;

import org.example.customerservice.model.CustomerService;
import org.example.customerservice.repository.CustomerServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CustomerServicePostgreController {
    private final CustomerServiceRepository repository;

    @Autowired
    public CustomerServicePostgreController(CustomerServiceRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/customers")
    public Flux<CustomerService> getCustomerService() {
        return repository.findAll();
    }

    @PutMapping("/put")
    public Mono<CustomerService> put(@RequestParam Long customerId,
                                     @RequestBody CustomerService customer) {
        if (customer == null) {
            return Mono.error(new IllegalArgumentException("Customer data cannot be null"));
        }
        return repository.save(customer);
    }

    @PostMapping("/save")
    public Mono<CustomerService> save(@RequestBody CustomerService customer) {
        return repository.save(customer);
    }

    @GetMapping("/get")
    public Mono<CustomerService> get(@RequestParam Long customerId) {
        if (customerId == null || customerId <= 0) {
            return Mono.error(new IllegalArgumentException("Invalid customer ID"));
        }
        return repository.findById(customerId);
    }
}
