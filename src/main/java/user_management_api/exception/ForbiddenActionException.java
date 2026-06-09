package user_management_api.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class ForbiddenActionException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public ForbiddenActionException(HttpStatus forbidden, String message) {
        super(message);
    }
}
