package com.banking.customer.controller;

import com.banking.customer.config.CustomerServiceConfig;
import com.banking.customer.dto.CustomerDetailsResponseDto;
import com.banking.customer.model.Account;
import com.banking.customer.model.Loans;
import com.banking.customer.model.Properties;
import com.banking.customer.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer-service")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerServiceConfig customerServiceConfig;

    @GetMapping("/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(
                customerServiceConfig.getMsg(),
                customerServiceConfig.getBuildVersion(),
                customerServiceConfig.getMailDetails(),
                customerServiceConfig.getActiveBranches());
        String jsonProperties = objectWriter.writeValueAsString(properties);
        return jsonProperties;
    }

    @GetMapping("/{customerId}")
    public CustomerDetailsResponseDto findCustomerDetailsByCustomerID(@PathVariable Long customerId) {
        return customerService.findCustomerDetailsByCustomerID(customerId);
    }

    @GetMapping("/myCustomer/{customerId}")
    public CustomerDetailsResponseDto findMyCustomerDetailsByCustomerId(@RequestHeader("correlation-id") String correlationId, @PathVariable Long customerId) {
        return customerService.findMyCustomerDetailsByCustomerID(correlationId, customerId);
    }

}