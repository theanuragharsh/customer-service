package com.banking.customer.service.impl;

import com.banking.customer.dto.CustomerDetailsResponseDto;
import com.banking.customer.mapper.CustomerMapper;
import com.banking.customer.model.Account;
import com.banking.customer.model.Card;
import com.banking.customer.model.Loans;
import com.banking.customer.repo.CustomerRepo;
import com.banking.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final WebClient.Builder webClient;
    private final Tracer tracer;
    private final CustomerRepo customerRepo;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDetailsResponseDto findCustomerDetailsByCustomerID(Long customerID) {
        Span span = tracer.nextSpan().name("findCustomerDetailsByCustomerID").start();
        try {
            log.debug("Fetching Customer Details for customerID: {}..", customerID);
            return customerRepo
                    .findById(customerID)
                    .map(customerMapper::toDto)
                    .map(customerDetailsResponseDto -> {
                        customerDetailsResponseDto.setAccounts(fetchAccounts(customerID));
                        customerDetailsResponseDto.setLoans(fetchLoans(customerID));
                        customerDetailsResponseDto.setCards(fetchCards(customerID));
                        log.info("Account Details Fetched Successfully.");
                        return customerDetailsResponseDto;
                    })
                    .orElseThrow(() -> new NoSuchElementException(String.format("No Customer Found with this customerID: %d", customerID)));
        } finally {
            span.end();
        }
    }

    private List<Account> fetchAccounts(Long customerID) {
        log.info("Customer Account Details fetched successfully with customerID: {}", customerID);
        return webClient.build()
                .get()
                .uri("http://localhost:8081/account-service/csp/" + customerID)
                .retrieve()
                .bodyToFlux(Account.class)
                .collectList()
                .block();

    }

    private List<Loans> fetchLoans(Long customerID) {
        log.debug("Fetching loan details from loan-service using WebClient..");
        return webClient.build()
                .get()
                .uri("http://localhost:8082/loan-service/" + customerID)
                .retrieve()
                .bodyToFlux(Loans.class)
                .collectList()
                .block();
    }

    private List<Card> fetchCards(Long customerID) {
        log.debug("Fetching card details from card-service using WebClient..");
        return webClient.build()
                .get()
                .uri("http://localhost:8083/card-service/" + customerID)
                .retrieve()
                .bodyToFlux(Card.class)
                .collectList()
                .block();
    }
}
