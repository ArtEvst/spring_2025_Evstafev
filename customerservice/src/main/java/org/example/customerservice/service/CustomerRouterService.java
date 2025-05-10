package org.example.customerservice.service;

import org.example.customerservice.model.CustomerService;
import org.example.customerservice.repository.db1.CustomerRepositoryDb1;
import org.example.customerservice.repository.db2.CustomerRepositoryDb2;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerRouterService {

    private final CustomerRepositoryDb1 db1Repository;
    private final CustomerRepositoryDb2 db2Repository;

    public CustomerRouterService(
                                 CustomerRepositoryDb1 db1Repository,
                                 CustomerRepositoryDb2 db2Repository) {
        this.db1Repository = db1Repository;
        this.db2Repository = db2Repository;
    }

    private ReactiveCrudRepository<CustomerService, Long> resolveRepo(Long customerId) {
        return customerId % 2 == 0 ? db1Repository : db2Repository;
    }

    private ReactiveCrudRepository<CustomerService, Long> resolveRepo() {
        return resolveRepo(0L);
    }

    public Flux<CustomerService> getCustomerService() {
        return resolveRepo().findAll();
    }

    public Mono<CustomerService> put(Long customerId,
                                     CustomerService customer) {
        return resolveRepo(customerId).save(customer);
    }

    public Mono<CustomerService> save(CustomerService customer) {
        return resolveRepo().save(customer);
    }

    public Mono<CustomerService> get(Long customerId) {
        return resolveRepo(customerId).findById(customerId);
    }
}