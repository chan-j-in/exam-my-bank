package exam.myBank.domain.repository;

import exam.myBank.domain.entity.Account;
import exam.myBank.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccount(Account account);
}
