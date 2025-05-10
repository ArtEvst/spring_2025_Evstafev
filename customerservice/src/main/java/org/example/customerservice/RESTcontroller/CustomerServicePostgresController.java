package org.example.customerservice.RESTcontroller;

import org.example.customerservice.model.CustomerService;
import org.example.customerservice.service.CustomerRouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class CustomerServicePostgresController {
//    private final CustomerServiceRepository repository;
    private final CustomerRouterService repository;

//    @Autowired
//    public CustomerServicePostgreController(CustomerServiceRepository repository) {
//        this.repository = repository;
//    }

    @Autowired
    public CustomerServicePostgresController(CustomerRouterService repository) {
        this.repository = repository;
    }

    @GetMapping("/customers")
    public Flux<CustomerService> getCustomerService() {
        return repository.getCustomerService();
    }

    @PutMapping("/put")
    public Mono<CustomerService> put(@RequestParam Long customerId,
                                     @RequestBody CustomerService customer) {
        if (customerId == null || customerId <= 0) {
            return Mono.error(new IllegalArgumentException("Invalid customer ID"));
        }
        if (customer == null) {
            return Mono.error(new IllegalArgumentException("Customer data cannot be null"));
        }
        return repository.put(customerId, customer);
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
        return repository.get(customerId);
    }
}
