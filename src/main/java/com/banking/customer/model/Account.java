package com.banking.customer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Account {

    @Id
    @Column(name = "customer_id")
    private long customerId;
    @Column(name = "account_number")
    private long accountNumber;
    @Column(name = "account_type")
    private String accountType;
    @Column(name = "branch_address")
    private String branchAddress;
    @Column(name = "created_date")
    private LocalDate createdDate;

}