package dev.charles.TestContainer.errors.exception;

import dev.charles.TestContainer.errors.errorcode.CustomErrorCode;

public class DuplicateResourceException extends RestApiException {
    public DuplicateResourceException(String message) {
        super(CustomErrorCode.DUPLICATED_RESOURCE, message);
    }
}
