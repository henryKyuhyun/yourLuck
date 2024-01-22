package com.yourLuck.yourLuck.controller;

import com.yourLuck.yourLuck.controller.response.FortuneResponseDTO;
import com.yourLuck.yourLuck.controller.response.Response;
import com.yourLuck.yourLuck.model.User;
import com.yourLuck.yourLuck.service.LuckService;
import com.yourLuck.yourLuck.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/luck")
@RequiredArgsConstructor
public class LuckController {

    private final UserService userService;
    private final LuckService luckService;


    @GetMapping("/todayLuck")
    public Response<FortuneResponseDTO> getFortune(@AuthenticationPrincipal User user) {
        String message = luckService.calculateFortune(user);
        FortuneResponseDTO responseDTO = new FortuneResponseDTO();
        responseDTO.setMessage(message);
        return Response.success(responseDTO);
    }
}