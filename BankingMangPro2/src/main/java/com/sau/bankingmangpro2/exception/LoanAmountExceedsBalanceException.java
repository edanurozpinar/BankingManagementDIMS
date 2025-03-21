package com.sau.bankingmangpro2.exception;

public class LoanAmountExceedsBalanceException extends RuntimeException{
    public LoanAmountExceedsBalanceException(String message) {
        super(message);
    }

}



