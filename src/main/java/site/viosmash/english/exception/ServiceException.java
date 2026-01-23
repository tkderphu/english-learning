package site.viosmash.english.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ServiceException extends RuntimeException {
    private HttpStatus code;
    private String message;

    public ServiceException(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }
}