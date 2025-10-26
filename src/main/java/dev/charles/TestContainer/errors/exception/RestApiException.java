package dev.charles.TestContainer.errors.exception;

import dev.charles.TestContainer.errors.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class RestApiException extends RuntimeException {
    protected ErrorCode errorCode;
    public RestApiException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}