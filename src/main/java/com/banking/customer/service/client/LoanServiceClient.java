package com.banking.customer.service.client;

import com.banking.customer.model.Loans;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("loan-service")
public interface LoanServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "loan-service/{customerId}", consumes = "application/json")
    List<Loans> getLoanDetails(@RequestHeader("correlation-id") String correlationId, @PathVariable Long customerId);
}
