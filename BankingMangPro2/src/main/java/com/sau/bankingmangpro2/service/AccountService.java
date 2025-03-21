package com.sau.bankingmangpro2.service;

import com.sau.bankingmangpro2.dto.AccountDto;

import java.util.List;

public interface AccountService {
     List<AccountDto> getAllAccounts();
     AccountDto getAccountById(Long id);
     AccountDto createAccount(AccountDto accountDto);
     AccountDto updateAccount(Long id, AccountDto accountDto);
     void deleteAccount(Long id);

}
