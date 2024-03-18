//package com.yourLuck.yourLuck.controller.response;
//
//import com.yourLuck.yourLuck.controller.Choice;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//import java.time.LocalDate;
//import java.util.List;
//
////ChatGptResponseDto
//@Getter
//@NoArgsConstructor
//public class ChatGptResponseDto implements Serializable {
//
//    private String id;
//    private String object;
//    private LocalDate created;
//    private String model;
//    private List<Choice> choices;
//
//    @Builder
//    public ChatGptResponseDto(String id, String object,
//                              LocalDate created, String model,
//                              java.util.List<Choice> choices) {
//        this.id = id;
//        this.object = object;
//        this.created = created;
//        this.model = model;
//        this.choices = choices;
//    }
//}