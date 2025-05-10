package org.example.customerservice.repository.db2;

import org.example.customerservice.model.CustomerService;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepositoryDb2 extends ReactiveCrudRepository<CustomerService, Long> {
}
