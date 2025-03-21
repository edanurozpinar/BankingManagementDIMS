package com.sau.bankingmangpro2.entity;

import com.sau.bankingmangpro2.dto.AccountDto;
import com.sau.bankingmangpro2.dto.CustomerDto;
import com.sau.bankingmangpro2.enums.LoanStatus;
import com.sau.bankingmangpro2.dto.LoanDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="loan",schema="dims_db")
public class Loan {

    @Id
    @Column(name="loan_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long loanId;

    @Column(name="loan_date")
    private LocalDateTime loanDate;

    @Column(name="loan_amount")
    private BigDecimal loanAmount;

    @Column(name="repayment_months")
    private int repayment_months ;

    @Column(name="interest_rate")
    private BigDecimal interest_rate  ;

    @Column(name = "total_debt")
    private BigDecimal totalDebt;

    @Column(name = "paid_total_debt")
    private BigDecimal paidTotalDebt;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;


    public LoanDto viewAsLoanDto() {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanId(this.loanId);
        loanDto.setLoanAmount(this.loanAmount);
        loanDto.setLoanDate(this.loanDate);
        loanDto.setRepayment_months(this.repayment_months);
        loanDto.setInterest_rate(this.interest_rate);
        loanDto.setStatus(this.status);
        if (this.account != null) {
            AccountDto accountDto = new AccountDto();
            accountDto.setAccountId(this.account.getAccountId());
            accountDto.setBranch(this.account.getBranch());
            accountDto.setBalance(this.account.getBalance());
            loanDto.setAccountDto(accountDto);
        }
        if (this.customer != null) {
            CustomerDto customerDto = new CustomerDto();
            customerDto.setCustomerId(this.customer.getCustomerId());
            customerDto.setCustomerName(this.customer.getCustomerName());
            customerDto.setAddress(this.customer.getAddress());
            customerDto.setCity(this.customer.getCity());
            loanDto.setCustomerDto(customerDto);
        }
        return loanDto;
    }

}
