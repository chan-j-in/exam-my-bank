package exam.myBank.dto.accountDto;

import exam.myBank.type.Bank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {

    private String accountName;
    private Bank bank;
    private String accountNum;
    private Long amount;
    private LocalDateTime createdAt;
    private LocalDateTime lastUsedAt;

}
