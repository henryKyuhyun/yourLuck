package com.yourLuck.yourLuck.controller.response;

import com.yourLuck.yourLuck.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {

    private String token;
    private UserLoginDTO user;

    public UserLoginResponse(String token , User user){
        this.token = token;
        this.user = UserLoginDTO.fromUser(user);
    }

}
