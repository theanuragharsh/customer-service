package com.banking.customer.service.client;

import com.banking.customer.model.Loan;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("loan-service")
public interface LoanServiceClient {
/*    @RequestMapping(method = RequestMethod.GET, value = "loan-service/{customerId}", consumes = "application/json")
    List<Loan> getLoanDetails(@RequestHeader("correlation-id") String correlationId, @PathVariable Long customerId);*/

    @GetMapping("/loan-service/{customerId}")
    List<Loan> getLoanDetails(@RequestHeader("correlation-id") String correlationId, @PathVariable Long customerId);
}
