package com.sau.bankingmangpro2.repository;

import com.sau.bankingmangpro2.entity.Customer;
import com.sau.bankingmangpro2.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan,Long> {
}
