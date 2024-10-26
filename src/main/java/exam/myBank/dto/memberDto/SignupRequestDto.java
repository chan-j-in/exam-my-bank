package exam.myBank.dto.memberDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequestDto {

    private String username;
    private String email;
    private String password;
    private String password2;
}
