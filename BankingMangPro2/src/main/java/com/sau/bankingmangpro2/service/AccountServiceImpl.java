package com.sau.bankingmangpro2.service;

import com.sau.bankingmangpro2.dto.AccountDto;
import com.sau.bankingmangpro2.entity.Account;
import com.sau.bankingmangpro2.entity.Customer;
import com.sau.bankingmangpro2.exception.ErrorMessages;
import com.sau.bankingmangpro2.exception.ResourceNotFoundException;
import com.sau.bankingmangpro2.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    @Override
    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(Account::viewAsAccountDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_ACCOUNT_NOT_FOUND + "with ID: " + id));
        return account.viewAsAccountDto();
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = new Account();
        account.setBranch(accountDto.getBranch());
        account.setBalance(accountDto.getBalance());

        Account savedAccount = accountRepository.save(account);
        return savedAccount.viewAsAccountDto();
    }

    @Override
    public AccountDto updateAccount(Long id, AccountDto accountDto) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_ACCOUNT_NOT_FOUND + "with ID: " + id));

        if(accountDto.getBranch() != null) {
            existingAccount.setBranch(accountDto.getBranch());
        }
        if(accountDto.getBalance() != null) {
            existingAccount.setBalance(accountDto.getBalance());
        }

        Account updatedAccount = accountRepository.save(existingAccount);
        return updatedAccount.viewAsAccountDto();
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_ACCOUNT_NOT_FOUND + " ID: " + id));
        accountRepository.delete(account);
    }
}
