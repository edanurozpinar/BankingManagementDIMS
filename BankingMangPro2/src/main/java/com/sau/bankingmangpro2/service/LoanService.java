package com.sau.bankingmangpro2.service;

import com.sau.bankingmangpro2.dto.LoanDto;
import com.sau.bankingmangpro2.dto.LoanPaymentDto;

import java.math.BigDecimal;
import java.util.List;

public interface LoanService {
     List<LoanDto> getAllLoans();
     LoanDto getLoanById(Long id);
      List<LoanDto> getLoansByCustomerId(Long customerId);
      List<LoanDto> getLoanByAccountId(Long accountId);
     LoanDto createLoan(LoanDto loanDto);
     void deleteLoan(Long id);
     LoanPaymentDto makePayment(Long id, BigDecimal paymentAmount);
     void checkDuePayments();
}
