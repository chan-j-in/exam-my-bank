package exam.myBank.dto.accountDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequestDto {

    private String myAccountNum;
    private String targetAccountNum;
    private Long amount;

}
