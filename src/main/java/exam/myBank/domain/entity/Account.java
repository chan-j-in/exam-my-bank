package exam.myBank.domain.entity;

import exam.myBank.type.Bank;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountName;

    private String accountNum;

    private Bank bank;

    private Long amount;

    private LocalDateTime createdAt;

    private LocalDateTime lastUsedAt;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Account(String accountName, String accountNum, Member member, Bank bank, LocalDateTime createdAt, LocalDateTime lastUsedAt) {
        this.accountName = accountName;
        this.accountNum = accountNum;
        this.member = member;
        this.bank = bank;
        this.amount = 0L;
        this.createdAt = createdAt;
        this.lastUsedAt = lastUsedAt;
    }

    public void deposit(Long amount) {
        this.amount += amount;
        this.lastUsedAt = LocalDateTime.now();
    }

    public void withdraw(Long amount) {
        if (this.amount>=amount) {
            this.amount -= amount;
            this.lastUsedAt = LocalDateTime.now();
        } else {
            throw new IllegalArgumentException("출금할 수 있는 잔액이 부족합니다.");
        }

    }
}
