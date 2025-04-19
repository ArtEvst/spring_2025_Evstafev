package org.example.customerservice.repository;

import org.example.customerservice.model.CustomerService;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CustomerServiceRepository extends ReactiveCrudRepository<CustomerService, Long> {
}
