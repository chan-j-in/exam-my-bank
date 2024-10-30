package exam.myBank.domain.dto.accountDto;

import exam.myBank.type.Bank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreateRequestDto {

    private String bank;
    private String accountName;

    public Bank getBankEnum() {
        return Bank.valueOf(this.bank);
    }
}
