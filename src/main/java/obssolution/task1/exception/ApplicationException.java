package obssolution.task1.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class ApplicationException extends Exception {

    private HttpStatus httpStatus;

    public ApplicationException() {}

    public ApplicationException(String message) {
        super(message);
    }
}
