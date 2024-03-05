package com.yourLuck.yourLuck.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor  // 빈 생성자를 통해 요청 객체(ChatRoomJoinRequest)를 생성한 다음, Spring 에서 요청 본문과 바인딩하는 과정에서 객체의 필드를 채울 수 있게 됩니다.
public class ChatRoomJoinRequest {

    private String chatRoomName;

}
