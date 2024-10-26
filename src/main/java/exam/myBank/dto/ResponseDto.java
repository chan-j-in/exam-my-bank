package exam.myBank.dto;

import lombok.Data;

@Data
public class ResponseDto<T> {
    private boolean success;
    private String message;
    private T data;

    public ResponseDto(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    private ResponseDto() {
    }
}
