package site.viosmash.english.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@Data
@NoArgsConstructor
public class BaseResponse<T>{

    private String message;
    private Integer statusCode;
    private T data;

    public BaseResponse(String message, Integer statusCode, T data) {
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
    }

    public static <T> BaseResponse<T> success(String message, Integer code, T data) {
        return new BaseResponse<>(message, code, data);
    }

    public static <T, U> BaseResponse<T> success(U data, Function<U, T> func) {
        return success(func.apply(data));
    }

    public static <T> BaseResponse<T> success(String message, T data) {
        return success(message, 200, data);
    }

    public static <T> BaseResponse<T> success(T data) {
        return success("", 200, data);
    }

    public static <T> BaseResponse<T> success(Integer code, T data) {
        return success("", code, data);
    }

    public static <T> BaseResponse<T> error(String message, Integer code) {
        return new BaseResponse<>(message, code, null);
    }
}