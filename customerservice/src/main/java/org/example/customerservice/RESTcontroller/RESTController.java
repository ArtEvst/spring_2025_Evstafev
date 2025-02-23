package org.example.customerservice.RESTcontroller;

import org.example.customerservice.model.CustomerService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class RESTController {
    final Map<Long, CustomerService> repository = new HashMap<>();

    @GetMapping("/customers")
    public Flux<CustomerService> getCustomerService() {
        return Flux.fromIterable(repository.values());
    }

    public Map<Long, CustomerService> getRepository() {
        return repository;
    }

    @PostMapping("/put")
    public Mono<CustomerService> put(@RequestParam Long customerId,
                                     @RequestBody CustomerService customer) {
        repository.put(customerId, customer);
        return Mono.justOrEmpty(customer);
    }

    @GetMapping("/get")
    public Mono<CustomerService> get(@RequestParam Long customerId) {
        return Mono.justOrEmpty(repository.getOrDefault(customerId, null));
    }
}
