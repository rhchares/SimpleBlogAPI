package dev.charles.TestContainer.errors.exception;

import dev.charles.TestContainer.errors.errorcode.CustomErrorCode;


public class NotFoundResourceException extends RestApiException{
    public NotFoundResourceException(String message) {
        super(CustomErrorCode.RESOURCE_NOT_FOUND, message);
    }
}
