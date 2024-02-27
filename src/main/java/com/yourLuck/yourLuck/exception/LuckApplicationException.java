package com.yourLuck.yourLuck.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@AllArgsConstructor
@Getter
public class LuckApplicationException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public LuckApplicationException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.message = null;
    }

    @Override
    public String getMessage(){
        if(message == null){
            return errorCode.getMessage();
        }
        return String.format("%s. %s", errorCode.getMessage(),message);
    }

}
