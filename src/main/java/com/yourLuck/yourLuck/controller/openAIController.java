//package com.yourLuck.yourLuck.controller;
//
//import com.yourLuck.yourLuck.controller.response.ChatGptResponseDto;
//import com.yourLuck.yourLuck.service.ChatGptService;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/chat-gpt")
//public class openAIController {
//
//    private final ChatGptService chatGptService;
//
//    public openAIController(ChatGptService chatGptService) {
//        this.chatGptService = chatGptService;
//    }
//
//    @PostMapping("/question")
//    public ChatGptResponseDto sendQuestion(@RequestBody QuestionRequestDto requestDto) {
//        return chatGptService.askQuestion(requestDto);
//    }
//}