package com.sau.bankingmangpro2.controller;

import com.sau.bankingmangpro2.dto.LoanDto;
import com.sau.bankingmangpro2.dto.LoanPaymentDto;
import com.sau.bankingmangpro2.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/loan")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @GetMapping("/all")
    public ResponseEntity<List<LoanDto>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<LoanDto> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getLoanById(id));
    }

    @GetMapping("/getByCustomer/{customerId}")
    public ResponseEntity<List<LoanDto>> getLoansByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(loanService.getLoansByCustomerId(customerId));
    }

    @GetMapping("/getByAccount/{accountId}")
    public ResponseEntity<List<LoanDto>> getLoanByAccountId(@PathVariable Long accountId) {
        return ResponseEntity.ok(loanService.getLoanByAccountId(accountId));
    }

    @PostMapping("/create")
    public ResponseEntity<LoanDto> createLoan(@RequestBody LoanDto loanDto) {
        LoanDto createdLoan = loanService.createLoan(loanDto);
        return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/payment/{id}")
    public ResponseEntity<LoanPaymentDto> makePayment(@PathVariable Long id, @RequestParam BigDecimal amount) {
        LoanPaymentDto paymentLoan = loanService.makePayment(id, amount);
        return ResponseEntity.ok(paymentLoan);
    }

}
