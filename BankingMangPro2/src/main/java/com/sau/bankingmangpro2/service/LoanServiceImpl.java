package com.sau.bankingmangpro2.service;

import com.sau.bankingmangpro2.dto.AccountDto;
import com.sau.bankingmangpro2.dto.CustomerDto;
import com.sau.bankingmangpro2.dto.LoanDto;
import com.sau.bankingmangpro2.dto.LoanPaymentDto;
import com.sau.bankingmangpro2.entity.Account;
import com.sau.bankingmangpro2.entity.Customer;
import com.sau.bankingmangpro2.entity.Loan;
import com.sau.bankingmangpro2.enums.LoanStatus;
import com.sau.bankingmangpro2.exception.ErrorMessages;
import com.sau.bankingmangpro2.exception.LoanAmountExceedsBalanceException;
import com.sau.bankingmangpro2.exception.PaymentAmountMismatchException;
import com.sau.bankingmangpro2.exception.ResourceNotFoundException;
import com.sau.bankingmangpro2.repository.AccountRepository;
import com.sau.bankingmangpro2.repository.CustomerRepository;
import com.sau.bankingmangpro2.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    @Override
    public List<LoanDto> getAllLoans() {
        return loanRepository.findAll().stream().map(Loan::viewAsLoanDto).collect(Collectors.toList());
    }

    @Override
    public LoanDto getLoanById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_LOAN_NOT_FOUND + ": " + id));
        return LoanDto.builder()
                .loanId(loan.getLoanId())
                .loanAmount(loan.getLoanAmount())
                .loanDate(loan.getLoanDate())
                .repayment_months(loan.getRepayment_months())
                .interest_rate(loan.getInterest_rate())
                .status(loan.getStatus())
                .accountDto(AccountDto.builder()
                        .accountId(loan.getAccount().getAccountId())
                        .branch(loan.getAccount().getBranch())
                        .balance(loan.getAccount().getBalance())
                        .build())
                .customerDto(CustomerDto.builder()
                        .customerId(loan.getCustomer().getCustomerId())
                        .customerName(loan.getCustomer().getCustomerName())
                        .address(loan.getCustomer().getAddress())
                        .city(loan.getCustomer().getCity())
                        .build())
                .build();
    }

    @Override
    public List<LoanDto> getLoansByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_CUSTOMER_NOT_FOUND
                        + "with ID : " + customerId));

        return customer.getLoans().stream()
                .map(Loan::viewAsLoanDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LoanDto> getLoanByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_ACCOUNT_NOT_FOUND
                        + "with ID : " + accountId));

        return account.getLoans().stream()
                .map(Loan::viewAsLoanDto)
                .collect(Collectors.toList());

    }

    @Override
    public LoanDto createLoan(LoanDto loanDto) {
        Account account = accountRepository.findById(loanDto.getAccountDto().getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_ACCOUNT_NOT_FOUND
                        + "with ID : " + loanDto.getAccountDto().getAccountId()));

        Customer customer = customerRepository.findById(loanDto.getCustomerDto().getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_CUSTOMER_NOT_FOUND
                        + "with ID : " + loanDto.getCustomerDto().getCustomerId()));

        //control loan not above a limit
        BigDecimal balance =account.getBalance();
        if(loanDto.getLoanAmount().compareTo(balance) > 0) {
            throw new LoanAmountExceedsBalanceException(ErrorMessages.ERROR_LOAN_AMOUNT_EXCEEDS_BALANCE + " : " + account.getBalance());
        }

        BigDecimal totalDebt = loanDto.getLoanAmount().add(loanDto.getLoanAmount().multiply(loanDto.getInterest_rate()).divide(
                new BigDecimal(100), 2, RoundingMode.HALF_UP ));

        BigDecimal monthlyPayment = totalDebt.divide(
                BigDecimal.valueOf(loanDto.getRepayment_months()),
                2, // Ondalık basamak sayısı (örneğin, 2)
                RoundingMode.HALF_UP // Yuvarlama modu
        );


        Loan loan = Loan.builder()
                .account(account)
                .customer(customer)
               .loanDate(LocalDateTime.now())
                .loanAmount(loanDto.getLoanAmount())
                .interest_rate(loanDto.getInterest_rate())
                .repayment_months(loanDto.getRepayment_months())
                .totalDebt(totalDebt)
                .paidTotalDebt(BigDecimal.ZERO)
                .monthlyPayment(monthlyPayment)
                .status(LoanStatus.ACTIVE_CLEAR)
                .build();


        Loan savedLoan = loanRepository.save(loan);
        return savedLoan.viewAsLoanDto();
    }


    @Override
    public void deleteLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_LOAN_NOT_FOUND + " with ID : " + id));

        loanRepository.delete(loan);
    }

    @Override
    public LoanPaymentDto makePayment(Long id, BigDecimal paymentAmount) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_LOAN_NOT_FOUND + "with ID: " + id));


        if (loan.getMonthlyPayment().compareTo(paymentAmount) != 0) {
            throw new PaymentAmountMismatchException(
                    ErrorMessages.ERROR_PAYMENT_AMOUNT_MISMATCH  + loan.getMonthlyPayment()
            );
        }

        BigDecimal newPaidDebt = loan.getPaidTotalDebt().add(paymentAmount);
        loan.setPaidTotalDebt(newPaidDebt);
        if (newPaidDebt.compareTo(loan.getTotalDebt()) >= 0) {
            loan.setStatus(LoanStatus.PAID);
        } else {
            loan.setStatus(LoanStatus.ACTIVE_CLEAR);
        }

        Loan updatedLoan = loanRepository.save(loan);

        LoanPaymentDto loanPaymentDto = new LoanPaymentDto();
        loanPaymentDto.setPaymentMade(paymentAmount);
        loanPaymentDto.setPaymentDate(LocalDateTime.now());
        loanPaymentDto.setRemainingDebt(updatedLoan.getTotalDebt().subtract(updatedLoan.getPaidTotalDebt()));
        loanPaymentDto.setTotalPaid(updatedLoan.getTotalDebt());

        return loanPaymentDto;
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // Her gün
    public void checkDuePayments() {
        List<Loan> activeLoans = loanRepository.findAll()
                .stream()
                .filter(loan -> loan.getStatus() != LoanStatus.PAID)
                .toList();


        for(Loan loan : activeLoans) {
            LocalDate today = LocalDate.now();
            LocalDate loanStartDate = loan.getLoanDate().toLocalDate();
            long fullMonthsPassed = ChronoUnit.MONTHS.between(loanStartDate, today);

            int loanStartDay = loanStartDate.getDayOfMonth();

            int lowerBound = loanStartDay - 10;

            if (today.getDayOfMonth() >= lowerBound && today.getDayOfMonth() <= loanStartDay) {

                if (loan.getPaidTotalDebt().compareTo(
                        BigDecimal.valueOf(fullMonthsPassed).add(BigDecimal.ONE).multiply(loan.getMonthlyPayment())
                ) == 0) {
                    loan.setStatus(LoanStatus.ACTIVE_PAID);

                }else{
                    loan.setStatus(LoanStatus.ACTIVE_PENDING_PAYMENT);
                }
            } else {

                if (loan.getPaidTotalDebt().compareTo(
                        BigDecimal.valueOf(fullMonthsPassed).add(BigDecimal.ONE).multiply(loan.getMonthlyPayment())
                ) == 0) {
                    loan.setStatus(LoanStatus.ACTIVE_CLEAR);

                }else{
                    loan.setStatus(LoanStatus.ACTIVE_DELAYED_PAYMENT);
                }

            }

        }

    }
}
