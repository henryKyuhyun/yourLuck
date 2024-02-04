package com.yourLuck.yourLuck.controller;

import com.yourLuck.yourLuck.controller.request.UserJoinRequest;
import com.yourLuck.yourLuck.controller.request.UserLoginRequest;
import com.yourLuck.yourLuck.controller.response.Response;
import com.yourLuck.yourLuck.controller.response.UserJoinResponse;
import com.yourLuck.yourLuck.controller.response.UserLoginResponse;
import com.yourLuck.yourLuck.model.User;
import com.yourLuck.yourLuck.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


   @PostMapping("/join")
   public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request){
       User user = userService.join(request.getName(), request.getPassword(), request.getNation(),request.getBirthOfDayAndTime(),request.getBloodType(),request.getGender());
       return Response.success(UserJoinResponse.fromUser(user));
   }

   @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request){
       String token = userService.login(request.getName(), request.getPassword());
       return Response.success(new UserLoginResponse(token));
   }
}