package exam.myBank.domain.entity;

import exam.myBank.type.TransactionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private Long amount;

    private Long balanceAfterTransaction;

    private LocalDateTime  transactionDate;

    @Enumerated(EnumType.STRING)
    TransactionType transactionType;

    private String targetAccountNum;

    @Builder
    public Transaction(Account account, Long amount, Long balanceAfterTransaction, TransactionType transactionType, String targetAccountNum) {
        this.account = account;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.transactionDate = LocalDateTime.now();
        this.transactionType = transactionType;
        this.targetAccountNum = targetAccountNum;
    }
}
