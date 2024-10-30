package exam.myBank.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USERNAME_DUPLICATED(HttpStatus.CONFLICT, ""),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, ""),
    USER_NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, ""),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST, ""),
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, ""),
    SAME_ACCOUNT_TRANSFER(HttpStatus.BAD_REQUEST, "")
    ;

    private HttpStatus httpStatus;
    private String message;
}
