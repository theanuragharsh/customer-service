package com.banking.customer.service.impl;

import com.banking.customer.dto.CustomerDetailsResponseDto;
import com.banking.customer.mapper.CustomerMapper;
import com.banking.customer.model.Account;
import com.banking.customer.model.Card;
import com.banking.customer.model.Loan;
import com.banking.customer.repo.CustomerRepo;
import com.banking.customer.service.CustomerService;
import com.banking.customer.service.client.AccountServiceClient;
import com.banking.customer.service.client.CardServiceClient;
import com.banking.customer.service.client.LoanServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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
    private final AccountServiceClient accountServiceClient;
    private final LoanServiceClient loanServiceClient;
    private final CardServiceClient cardServiceClient;

    @Override
    public CustomerDetailsResponseDto findCustomerDetailsByCustomerID(String correlationId, Long customerID) {
        Span span = tracer.nextSpan().name("findCustomerDetailsByCustomerID").start();
        try {
            log.debug("Fetching Customer Details for customerID: {}..", customerID);
            return customerRepo
                    .findById(customerID)
                    .map(customerMapper::toDto)
                    .map(customerDetailsResponseDto -> {
                        customerDetailsResponseDto.setAccounts(fetchAccounts(correlationId, customerID));
                        customerDetailsResponseDto.setLoans(fetchLoans(correlationId, customerID));
                        customerDetailsResponseDto.setCards(fetchCards(correlationId, customerID));
                        log.info("Account Details Fetched Successfully.");
                        return customerDetailsResponseDto;
                    })
                    .orElseThrow(() -> new NoSuchElementException(String.format("No Customer Found with this customerID: %d", customerID)));
        } finally {
            span.end();
        }
    }

    @Override
    @CircuitBreaker(name = "myCustomerDetailsCB", fallbackMethod = "myCustomerDetailsCBFallbackMethod")
//    @TimeLimiter(name = "myCustomerDetailsCB", fallbackMethod = "myCustomerDetailsCBFallbackMethod")
    @Retry(name = "myCustomerDetailsCB", fallbackMethod = "myCustomerDetailsCBFallbackMethod")
    public CustomerDetailsResponseDto findMyCustomerDetailsByCustomerID(String correlationId, Long customerId) {
        log.info("Fetching Customer details for customerId : {}", customerId);
        return customerRepo.findById(customerId)
                .map(customerMapper::toDto)
                .map(customerDetailsResponseDto -> {
                    log.debug("correlationId : {}", correlationId);
                    customerDetailsResponseDto.setAccounts(accountServiceClient.getAccountDetails(correlationId, customerId));
                    customerDetailsResponseDto.setLoans(loanServiceClient.getLoanDetails(correlationId, customerId));
                    customerDetailsResponseDto.setCards(cardServiceClient.getCardDetails(correlationId, customerId));
                    return customerDetailsResponseDto;
                }).orElseThrow(() -> new NoSuchElementException(String.format("No Customer Found with this customerID: %d", customerId)));
    }


    public CustomerDetailsResponseDto myCustomerDetailsCBFallbackMethod(String correlationId, Long customerId, RuntimeException exception) {
        log.info("FALLBACK : Fetching Customer details for customerId : {}", customerId);
        return customerRepo.findById(customerId)
                .map(customerMapper::toDto)
                .map(customerDetailsResponseDto -> {
                    log.debug("correlationId : {}", correlationId);
                    customerDetailsResponseDto.setAccounts(accountServiceClient.getAccountDetails(correlationId, customerId));
                    customerDetailsResponseDto.setLoans(loanServiceClient.getLoanDetails(correlationId, customerId));
//                    customerDetailsResponseDto.setCards(cardServiceClient.getCardDetails(correlationId, customerId));
                    return customerDetailsResponseDto;
                }).orElseThrow(() -> new NoSuchElementException(String.format("No Customer Found with this customerID: %d", customerId)));
    }

    private List<Account> fetchAccounts(String correlationId, Long customerID) {
        log.info("Customer Account Details fetched successfully with customerID: {}", customerID);
        return webClient.build()
                .get()
                .uri("http://localhost:8081/account-service/csp/" + customerID)
                .header("correlation-id", correlationId)
                .retrieve()
                .bodyToFlux(Account.class)
                .collectList()
                .block();

    }

    private List<Loan> fetchLoans(String correlationId, Long customerID) {
        log.debug("Fetching loan details from loan-service using WebClient..");
        return webClient.build()
                .get()
                .uri("http://localhost:8082/loan-service/" + customerID)
                .header("correlation-id", correlationId)
                .retrieve()
                .bodyToFlux(Loan.class)
                .collectList()
                .block();
    }

    private List<Card> fetchCards(String correlationId, Long customerID) {
        log.debug("Fetching card details from card-service using WebClient..");
        return webClient.build()
                .get()
                .uri("http://localhost:8083/card-service/" + customerID)
                .header("correlation-id", correlationId)
                .retrieve()
                .bodyToFlux(Card.class)
                .collectList()
                .block();
    }
}
