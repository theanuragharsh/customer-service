package com.banking.customer.service.client;

import com.banking.customer.model.Card;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("card-service")
public interface CardServiceClient {
/*    @RequestMapping(method = RequestMethod.GET, value = "card-service/{customerId}", consumes = "application/json")
    List<Card> getCardDetails(@RequestHeader("correlation-id") String correlationId, @PathVariable Long customerId);*/

    @GetMapping("/card-service/{customerId}")
    List<Card> getCardDetails(@RequestHeader("correlation-id") String correlationId, @PathVariable Long customerId);
}
