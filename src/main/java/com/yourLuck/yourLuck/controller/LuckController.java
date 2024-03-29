package com.yourLuck.yourLuck.controller;

import com.yourLuck.yourLuck.controller.response.FortuneResponseDTO;
import com.yourLuck.yourLuck.controller.response.Response;
import com.yourLuck.yourLuck.model.Luck;
import com.yourLuck.yourLuck.model.User;
import com.yourLuck.yourLuck.service.LuckService;
import com.yourLuck.yourLuck.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/users/luck")
@RequiredArgsConstructor
public class LuckController {

    private final LuckService luckService;

    @GetMapping("/todayLuck")
    public Response<FortuneResponseDTO> getFortune(@AuthenticationPrincipal User user) {
        String message = luckService.calculateFortune(user);
        FortuneResponseDTO responseDTO = new FortuneResponseDTO();
        responseDTO.setMessage(message);
        return Response.success(responseDTO);
    }

    @GetMapping("/lotto")
    public Response<int[]> getLottoNumbers(@AuthenticationPrincipal User user) {
        int[] lottoNumbers = luckService.generateLottoNumber();
        return Response.success(lottoNumbers);
    }

    @GetMapping("/history")
    public Response<List<Luck>> getLuckHistory(@AuthenticationPrincipal User user) {
        List<Luck> luckHistory = luckService.getLuckHistory(user.getId());
        return Response.success(luckHistory);
    }

}