package obssolution.task1.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BusinessException extends Exception {
    private final HttpStatus httpStatus;
    private final String errorMessage;

    public BusinessException(String errorMessage) {
        httpStatus = HttpStatus.BAD_REQUEST;
        this.errorMessage = errorMessage;
    }

    public BusinessException(HttpStatus httpStatus, String message) {
        this.errorMessage = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return String.format(" %s : %s", getHttpStatus(), errorMessage);
    }
}
