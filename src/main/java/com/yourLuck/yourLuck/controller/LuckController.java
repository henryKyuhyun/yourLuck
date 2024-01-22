package com.yourLuck.yourLuck.controller;

import com.yourLuck.yourLuck.controller.request.UserFortuneRequestDTO;
import com.yourLuck.yourLuck.controller.request.UserLoginRequest;
import com.yourLuck.yourLuck.controller.response.FortuneResponseDTO;
import com.yourLuck.yourLuck.controller.response.Response;
import com.yourLuck.yourLuck.controller.response.UserLoginResponse;
import com.yourLuck.yourLuck.model.User;
import com.yourLuck.yourLuck.model.entity.UserEntity;
import com.yourLuck.yourLuck.service.LuckService;
import com.yourLuck.yourLuck.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/luck")
@RequiredArgsConstructor
public class LuckController {

    private final UserService userService;
    private final LuckService luckService;


    //    @GetMapping("/todayLuck")
//    public Response<FortuneResponseDTO> getFortune(@RequestBody UserFortuneRequestDTO request){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userName = authentication.getName();
//        User user = userService.loadUserByUserName(userName);
//        String message = luckService.calculateFortune(user);
//        FortuneResponseDTO responseDTO = new FortuneResponseDTO();
//        responseDTO.setMessage(message);
//        return Response.success(responseDTO);
//    }}
    @GetMapping("/todayLuck")
    public Response<FortuneResponseDTO> getFortune() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.loadUserByUserName(userName);
        String message = luckService.calculateFortune(user);
        FortuneResponseDTO responseDTO = new FortuneResponseDTO();
        responseDTO.setMessage(message);
        return Response.success(responseDTO);
    };
}