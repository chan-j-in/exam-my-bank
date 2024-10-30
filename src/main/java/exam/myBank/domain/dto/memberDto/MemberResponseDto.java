package exam.myBank.domain.dto.memberDto;

import exam.myBank.domain.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {

    private String username;
    private List<Account> accounts;
}
