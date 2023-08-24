package com.banking.customer.service;

import com.banking.customer.dto.CustomerDetailsResponseDto;

public interface CustomerService {
    CustomerDetailsResponseDto findCustomerDetailsByCustomerID(Long customerID);

    CustomerDetailsResponseDto findMyCustomerDetailsByCustomerID(Long customerId);
}
