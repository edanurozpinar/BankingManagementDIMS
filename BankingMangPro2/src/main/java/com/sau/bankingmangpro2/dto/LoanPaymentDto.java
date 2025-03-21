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
public class LoanPaymentDto {

    private BigDecimal paymentMade ;
    private BigDecimal totalPaid;
    private BigDecimal remainingDebt ;
    private LocalDateTime paymentDate;

}