package org.example.customerservice.RESTcontroller;

import org.example.customerservice.model.CustomerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
class RESTControllerTest {
    @Autowired
    RESTController RESTcontroller;

    @Autowired
    WebTestClient webTestClient;

    @Test
    void contextLoads() {
        assertNotNull(webTestClient);
    }

    @Test
    void put() {
        CustomerService customer = new CustomerService(10L, "Ivan", "Ivanov",
                10, "79999999999");

        long sizeBefore = RESTcontroller.getRepository().size();

        webTestClient.post()
                .uri("/api/v1/put?customerId=1")
                .bodyValue(customer)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                          "id": 10,
                          "firstName": "Ivan",
                          "lastName": "Ivanov",
                          "age": 10,
                          "phoneNumber": "79999999999"
                        }
                        """);

        long sizeAfter = RESTcontroller.getRepository().size();
        assertEquals(0, sizeBefore);
        assertEquals(sizeBefore + 1, sizeAfter);
    }

    @Test
    void get() {
        RESTcontroller.getRepository().put(1L, new CustomerService(1L, "Ivan", "Ivanov",
                10, "79999999999"));
        RESTcontroller.getRepository().put(2L, new CustomerService(1L, "Ivan", "Ivanov",
                10, "79999999999"));
        RESTcontroller.getRepository().put(3L, new CustomerService(1L, "Ivan", "Ivanov",
                10, "79999999999"));
        RESTcontroller.getRepository().put(4L, new CustomerService(1L, "Ivan", "Ivanov",
                10, "79999999999"));
        RESTcontroller.getRepository().put(5L, new CustomerService(1L, "Ivan", "Ivanov",
                10, "79999999999"));

        long sizeBefore = RESTcontroller.getRepository().size();

        webTestClient.get()
                .uri("/api/v1/get?customerId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(
                        """
                                {
                                  "id": 1,
                                  "firstName": "Ivan",
                                  "lastName": "Ivanov",
                                  "age": 10,
                                  "phoneNumber": "79999999999"
                                }
                                """
                );
        assertEquals(5, sizeBefore);
    }

    @Test
    void getCustomerService() {
        RESTcontroller.getRepository().put(1L, new CustomerService(1L, "Ivan", "Ivanov",
                10, "79999999999"));
        webTestClient.get()
                .uri("/api/v1/customers")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(
                        """
                                [{
                                  "id": 1,
                                  "firstName": "Ivan",
                                  "lastName": "Ivanov",
                                  "age": 10,
                                  "phoneNumber": "79999999999"
                                }]
                                """
                );
    }
}