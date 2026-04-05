package site.viosmash.english.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;
import java.util.Objects;

import site.viosmash.english.dto.response.BaseResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<BaseResponse<?>> handleServiceException(ServiceException ex) {
        HttpStatus status = ex.getCode() != null ? ex.getCode() : HttpStatus.BAD_REQUEST;
        String message = ex.getMessage() != null ? ex.getMessage() : "";
        log.warn("ServiceException: {}", message);
        HttpStatus safeStatus = Objects.requireNonNull(status);
        return ResponseEntity.status(safeStatus).body(BaseResponse.error(message, safeStatus.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Validation failed: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.error(message, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<BaseResponse<?>> handleBindException(BindException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Bind exception: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.error(message, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<?>> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + " " + cv.getMessage())
                .collect(Collectors.joining(", "));
        log.warn("Constraint violations: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.error(message, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<?>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "Malformed JSON request";
        log.warn("HttpMessageNotReadableException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.error(message, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<?>> handleMissingParams(MissingServletRequestParameterException ex) {
        String message = ex.getParameterName() + " parameter is missing";
        log.warn("Missing param: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.error(message, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse<?>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String message = ex.getMessage();
        log.warn("Method not supported: {}", message);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(BaseResponse.error(message, HttpStatus.METHOD_NOT_ALLOWED.value()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse<?>> handleAccessDenied(AccessDeniedException ex) {
        String message = "Access is denied";
        log.warn("AccessDenied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BaseResponse.error(message, HttpStatus.FORBIDDEN.value()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseResponse<?>> handleAuthenticationException(AuthenticationException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Authentication failed";
        log.warn("AuthenticationException: {}", message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponse.error(message, HttpStatus.UNAUTHORIZED.value()));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<BaseResponse<?>> handleMultipartException(MultipartException ex) {
        // Usually means Swagger/curl called voice endpoint without multipart/form-data.
        String message = "Voice endpoint requires multipart/form-data with field name 'audio' (file).";
        log.warn("MultipartException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.error(message, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(WebClientResponseException.TooManyRequests.class)
    public ResponseEntity<BaseResponse<?>> handleOpenAiRateLimit(WebClientResponseException.TooManyRequests ex) {
        String message = "OpenAI rate limit exceeded. Please wait a moment and retry.";
        log.warn("OpenAI 429 Too Many Requests: {}", ex.getResponseBodyAsString());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(BaseResponse.error(message, HttpStatus.TOO_MANY_REQUESTS.value()));
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<BaseResponse<?>> handleWebClientResponse(WebClientResponseException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        HttpStatus resolvedStatus = status != null ? status : HttpStatus.BAD_GATEWAY;
        String message = "Upstream API error: " + ex.getStatusCode().value();
        log.warn("WebClientResponseException status={} body={}", ex.getStatusCode().value(), ex.getResponseBodyAsString());
        return ResponseEntity.status(resolvedStatus)
                .body(BaseResponse.error(message, resolvedStatus.value()));
    }

    /**
     * Spring 6 maps unknown paths to static resources first; missing files become this exception.
     * For API clients it usually means wrong URL or the running JAR/classpath does not include the controller (rebuild/restart).
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleNoResourceFound(NoResourceFoundException ex) {
        String path = ex.getResourcePath();
        log.warn("No resource or API mapping for path: {}", path);
        String message = "Not found: " + path;
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponse.error(message, HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<?>> handleAll(Exception ex) {
        log.error("Unhandled exception", ex);
        String message = "Internal server error";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.error(message, HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
