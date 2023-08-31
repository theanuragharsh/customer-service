package com.banking.customer.service.client;

import com.banking.customer.model.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "account-service")
public interface AccountServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "account-service/{customerId}", consumes = "application/json")
    List<Account> getAccountDetails(@RequestHeader("correlation-id") String correlationId, @PathVariable("customerId") Long customerId);

/*
    @GetMapping("/{customerId}")
    List<Account> getAccountDetails(@PathVariable("customerId") Long customerId);
*/
/*@RequestMapping(method = RequestMethod.POST, value = "card-service/myCards", consumes = "application/json")
List<Card> getCardDetails(@RequestBody Customer customer);*/

}
