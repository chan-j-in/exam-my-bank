package exam.myBank.dto.accountDto;

import exam.myBank.type.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDto {

    private TransactionType transactionType;
    private Long amount;
    private Long balanceAfterTransaction;
    private LocalDateTime transactionDate;
    private String targetAccountNum;
}
