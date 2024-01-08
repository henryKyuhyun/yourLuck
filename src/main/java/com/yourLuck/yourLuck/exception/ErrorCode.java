package com.yourLuck.yourLuck.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    DUPLICATED_USER_NAME(HttpStatus.CONFLICT,"User Name is duplicated"),
    USER_NOT_FOUNDED(HttpStatus.NOT_FOUND,"User not founded"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"token is invalid"),

    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"User Password wrong")
    ;
    private HttpStatus status;
    private String message;

//  'DUPLICATED_USER_NAME'은 'ErrorCode' enum의 항목으로 정의되어 있습니다.
//  이 enum 항목은 HttpStatus와 메시지를 인자로 받아 초기화됩니다.
//  Enum은 Java에서 열거형을 정의할 때 사용하는 특수한 클래스 타입입니다.
//  Enum 내부에서 멤버 변수를 정의할 때는 Enum 항목보다 뒤에 위치해야 합니다. 이는 Java 언어의 문법적인 규칙입니다.
//  따라서 'DUPLICATED_USER_NAME' 항목을 'status'와 'message' 멤버 변수보다 아래에 위치시키면,
//  컴파일러는 해당 변수들을 아직 정의하지 않았다고 판단하고 에러를 발생시킵니다.
//  Enum에서 멤버 변수와 메서드는 항상 Enum 항목보다 아래에 위치해야 합니다. 이를 지키면 문제가 발생하지 않을 것입니다.

}
