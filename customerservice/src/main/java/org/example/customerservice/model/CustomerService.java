package org.example.customerservice.model;

import org.springframework.data.annotation.Id;

public record CustomerService(@Id Long id, String firstName, String lastName, Integer age, String phoneNumber) {
}
