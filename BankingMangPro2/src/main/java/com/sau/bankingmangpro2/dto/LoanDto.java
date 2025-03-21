package com.sau.bankingmangpro2.dto;

import com.sau.bankingmangpro2.enums.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanDto {
    private long loanId;
    private BigDecimal loanAmount;
    private LocalDateTime loanDate;
    private int repayment_months ;
    private BigDecimal interest_rate  ;
    private LoanStatus status;
    private CustomerDto customerDto;
    private AccountDto accountDto;

}
