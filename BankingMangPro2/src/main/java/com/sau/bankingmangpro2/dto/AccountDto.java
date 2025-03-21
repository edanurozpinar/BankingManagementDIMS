package com.sau.bankingmangpro2.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {
    private Long accountId;
    private String branch;
    private BigDecimal balance;
}
