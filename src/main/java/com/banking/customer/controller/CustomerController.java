package com.banking.customer.controller;

import com.banking.customer.config.CustomerServiceConfig;
import com.banking.customer.dto.CustomerDetailsResponseDto;
import com.banking.customer.model.Properties;
import com.banking.customer.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}