package exam.myBank.domain.entity;

import exam.myBank.type.Bank;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountName;

    private String accountNum;

    @Enumerated(EnumType.STRING)
    private Bank bank;

    private Long amount = 0L;

    private LocalDateTime createdAt;

    private LocalDateTime lastUsedAt;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    @Builder
    public Account(String accountName, String accountNum, Member member, Bank bank) {
        this.accountName = accountName;
        this.accountNum = accountNum;
        this.member = member;
        this.bank = bank;
        this.createdAt = LocalDateTime.now();
        this.lastUsedAt = LocalDateTime.now();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction); // 거래를 리스트에 추가
    }

    public Long deposit(Long amount) {
        this.amount += amount;
        this.lastUsedAt = LocalDateTime.now();

        return this.amount;
    }

    public Long withdraw(Long amount) {

        this.amount -= amount;
        this.lastUsedAt = LocalDateTime.now();

        return this.amount;
    }

    public void updateAccountName(String accountName) {

        this.accountName = accountName;
    }
}
