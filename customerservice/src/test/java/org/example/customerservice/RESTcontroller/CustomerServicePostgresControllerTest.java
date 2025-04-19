package org.example.customerservice.RESTcontroller;

import org.example.customerservice.model.CustomerService;
import org.example.customerservice.repository.CustomerServiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerServicePostgresControllerTest {

    @Autowired
    private CustomerServiceRepository repository;

    @Autowired
    WebTestClient webTestClient;

    @Test
    void contextLoads() {
        assertNotNull(webTestClient);
    }

//    @Test
//    void testGetAllCustomers() {
//        webTestClient.get()
//                .uri(uribuilder -> uribuilder.path("/api/v1/customers").build())
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .json("[]"
////                        """
////                        [
////                         {"id":-2,"firstName":"Petr",   "lastName":"Petrov",  "age":"20","phone":"+79111111111"},
////                         {"id":-1,"firstName":"Aleksey","lastName":"Alekseev","age":"19","phone":"+79000000000"}
////                        ]
////                        """
//                );
//    }

    @Test
    void testPut() {
        CustomerService customerService = new CustomerService(null, "Sergey", "Sergeev", 23, "+78888888888");

        long sizeBefore = repository.findAll().count().block();

        webTestClient.put()
                .uri(uribuilder -> uribuilder.path("/api/v1/put")
                                             .queryParam("customerId", "17")
                                             .build())
                .bodyValue(customerService)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                          "firstName": "Sergey",
                          "lastName": "Sergeev",
                          "age": 23,
                          "phoneNumber": "+78888888888"
                        }
                        """);

        long sizeAfter = repository.findAll().count().block();

        assertEquals(sizeBefore + 1, sizeAfter);
    }

    @Test
    void testSave() {
        CustomerService customerService = new CustomerService(null, "Ivan", "Ivanov", 23, "+79999999999");

        long sizeBefore = repository.findAll().count().block();

        webTestClient.post()
                .uri(uribuilder -> uribuilder.path("/api/v1/save")
                        .build())
                .bodyValue(customerService)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                          "firstName": "Ivan",
                          "lastName": "Ivanov",
                          "age": 23,
                          "phoneNumber": "+79999999999"
                        }
                        """)
                .returnResult()
                .getResponseBody();

        long sizeAfter = repository.findAll().count().block();

        assertEquals(sizeBefore + 1, sizeAfter);
    }

    @Test
    void testSaveWithId() {
        CustomerService customerService = new CustomerService(13L, "Egor", "Egorov", 24, "+79999999999");

        long sizeBefore = repository.findAll().count().block();

        webTestClient.post()
                .uri(uribuilder -> uribuilder.path("/api/v1/save")
                        .build())
                .bodyValue(customerService)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().is5xxServerError();

        long sizeAfter = repository.findAll().count().block();

        assertEquals(sizeBefore, sizeAfter);
    }
}