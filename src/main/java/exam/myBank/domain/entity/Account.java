package exam.myBank.domain.entity;

import exam.myBank.type.Bank;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountName;

    private String accountNum;

    private Bank bank;

    private Long amount = 0L;

    private LocalDateTime createdAt;

    private LocalDateTime lastUsedAt;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Account(String accountName, String accountNum, Member member, Bank bank) {
        this.accountName = accountName;
        this.accountNum = accountNum;
        this.member = member;
        this.bank = bank;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.lastUsedAt = LocalDateTime.now();
    }

    public Long deposit(Long amount) {
        this.amount += amount;
        this.lastUsedAt = LocalDateTime.now();

        return this.amount;
    }

    public Long withdraw(Long amount) {
        if (this.amount>=amount) {
            this.amount -= amount;
            this.lastUsedAt = LocalDateTime.now();
        } else {
            throw new IllegalArgumentException("출금할 수 있는 잔액이 부족합니다.");
        }

        return this.amount;
    }
}
