package com.banking.customer.service;

import com.banking.customer.dto.CustomerDetailsResponseDto;

public interface CustomerService {
    CustomerDetailsResponseDto findCustomerDetailsByCustomerID(String correlationId, Long customerID);

    CustomerDetailsResponseDto findMyCustomerDetailsByCustomerID(String correlationId, Long customerId);
}
