package com.banking.customer.service.client;

import com.banking.customer.model.Card;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("card-service")
public interface CardServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "card-service/{customerId}", consumes = "application/json")
    List<Card> getCardDetails(@PathVariable Long customerId);
}
