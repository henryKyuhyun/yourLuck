//package com.yourLuck.yourLuck.service;
//
//import com.yourLuck.yourLuck.configuration.ChatGptConfig;
//import com.yourLuck.yourLuck.controller.request.ChatGptRequestDto;
//import com.yourLuck.yourLuck.controller.response.ChatGptResponseDto;
//import com.yourLuck.yourLuck.controller.QuestionRequestDto;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
////Service
//@Service
//public class ChatGptService {
//
//    private static RestTemplate restTemplate = new RestTemplate();
//
//    public HttpEntity<ChatGptRequestDto> buildHttpEntity(ChatGptRequestDto requestDto) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
//        headers.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + ChatGptConfig.API_KEY);
//        return new HttpEntity<>(requestDto, headers);
//    }
//
//    public ChatGptResponseDto getResponse(HttpEntity<ChatGptRequestDto> chatGptRequestDtoHttpEntity) {
//        ResponseEntity<ChatGptResponseDto> responseEntity = restTemplate.postForEntity(
//                ChatGptConfig.URL,
//                chatGptRequestDtoHttpEntity,
//                ChatGptResponseDto.class);
//
//        return responseEntity.getBody();
//    }
//
//    public ChatGptResponseDto askQuestion(QuestionRequestDto requestDto) {
//        return this.getResponse(
//                this.buildHttpEntity(
//                        new ChatGptRequestDto(
//                                ChatGptConfig.MODEL,
//                                requestDto.getQuestion(),
//                                ChatGptConfig.MAX_TOKEN,
//                                ChatGptConfig.TEMPERATURE,
//                                ChatGptConfig.TOP_P
//                        )
//                )
//        );
//    }
//}