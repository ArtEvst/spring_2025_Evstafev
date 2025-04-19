package org.example.customerservice.repository.db1;

import org.example.customerservice.model.CustomerService;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepositoryDb1 extends ReactiveCrudRepository<CustomerService, Long> {
}