package com.banking.customer.dto;

import com.banking.customer.model.Account;
import com.banking.customer.model.Card;
import com.banking.customer.model.Loans;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailsResponseDto {
    private Long customerId;
    private String name;
    private String email;
    private String mobileNumber;
    private LocalDate createdDate;
    private List<Account> accounts;
    private List<Loans> loans;
    private List<Card> cards;
}
