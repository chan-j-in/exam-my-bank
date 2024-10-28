package exam.myBank.domain.repository;

import exam.myBank.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNum(String accountNum);
    Optional<Account> findByAccountName(String accountName);
    Optional<Account> findFirstByOrderByIdDesc();

    List<Account> findByMemberId(Long memberId);
}
