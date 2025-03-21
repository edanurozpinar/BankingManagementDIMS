package com.sau.bankingmangpro2.entity;

import com.sau.bankingmangpro2.dto.AccountDto;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name="account",schema="dims_db")
public class Account {
    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(name = "branch")
    private String branch;

    @Column(name = "balance")
    private BigDecimal balance; //limit

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Loan> loans;

    public AccountDto viewAsAccountDto() {
        return new AccountDto(accountId, branch, balance);
    }

}
