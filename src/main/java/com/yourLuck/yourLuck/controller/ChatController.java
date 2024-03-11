package com.yourLuck.yourLuck.controller;

import com.yourLuck.yourLuck.controller.request.ChatRoomJoinRequest;
import com.yourLuck.yourLuck.controller.request.CreateChatRoomRequest;
import com.yourLuck.yourLuck.controller.request.SendMessageRequest;
import com.yourLuck.yourLuck.controller.response.ChatRoomJoinResponse;
import com.yourLuck.yourLuck.controller.response.CreateChatRoomResponse;
import com.yourLuck.yourLuck.controller.response.SendMessageResponse;
import com.yourLuck.yourLuck.model.ChatRoom;
import com.yourLuck.yourLuck.model.Message;
import com.yourLuck.yourLuck.model.entity.UserEntity;
import com.yourLuck.yourLuck.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/rooms") // 엔드포인트를 복수형으로 변경
    public ResponseEntity<CreateChatRoomResponse> createChatRoom(@RequestBody CreateChatRoomRequest request) {
        ChatRoom chatRoom = chatService.createChatRoom(request.getChatRoomName(), request.getUserName());
        CreateChatRoomResponse response = chatRoom.createChatRoomResponse();
        return new ResponseEntity<>(response, HttpStatus.CREATED); // 201 Created 상태 코드 설정
    }


//    @PutMapping("/chat-rooms/{roomId}/join") //PUT 메서드는 자원의 전체 교체에 주로 사용됩니다
@PatchMapping("/chat-rooms/{roomId}/join")
public ResponseEntity<ChatRoomJoinResponse> joinChatRoom(
        @PathVariable Integer roomId,
        @RequestBody ChatRoomJoinRequest joinRequest) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userName = authentication.getName(); // 현재 인증된 사용자 이름 가져오기

    ChatRoom chatRoom = chatService.joinChatRoom(roomId, userName, joinRequest.getChatRoomName());
    ChatRoomJoinResponse response = new ChatRoomJoinResponse(roomId, chatRoom.getChatRoomName(), userName);

    return ResponseEntity.ok(response);
}
    @PostMapping("/room/{roomId}/message")
    public ResponseEntity<SendMessageResponse> sendMessage(
            @PathVariable Integer roomId,
            @RequestBody SendMessageRequest sendMessageRequest) {

        return ResponseEntity.ok(chatService.sendMessage(roomId, sendMessageRequest.getMessageContent(),sendMessageRequest));

    }

    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<List<Message>> getMessagesFromChatRoom(@PathVariable Integer roomId) {
        return ResponseEntity.ok(chatService.getMessagesFromChatRoom(roomId));
    }
}