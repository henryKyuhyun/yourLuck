package com.yourLuck.yourLuck.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yourLuck.yourLuck.controller.request.UserJoinRequest;
import com.yourLuck.yourLuck.exception.ErrorCode;
import com.yourLuck.yourLuck.exception.LuckApplicationException;
import com.yourLuck.yourLuck.model.BloodType;
import com.yourLuck.yourLuck.model.Gender;
import com.yourLuck.yourLuck.model.User;
import com.yourLuck.yourLuck.repository.UserEntityRepository;
import com.yourLuck.yourLuck.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void joinTest() throws Exception {

        //        Request Setting
        String userName = "test";
        String password = "password";
        String nation = "nation";
        LocalDateTime birthOfDayAndTime = LocalDateTime.now();
        BloodType bloodType = BloodType.A형;
        Gender gender = Gender.MALE;

        UserJoinRequest request = new UserJoinRequest(userName, password, nation, birthOfDayAndTime, bloodType, gender);

        // Mock setting
        User user = new User(null, userName, password, nation, birthOfDayAndTime, bloodType, gender, null, null, null);

        Mockito.when(userService.join(eq(userName), eq(password), eq(nation), eq(birthOfDayAndTime), eq(bloodType), eq(gender))).thenReturn(user);

        // Test
        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.userName").value(userName))
                .andExpect(jsonPath("$.result.nation").value(nation));
    }

    @Test
    void Duplicated_UserName() throws Exception {
        //        Request Setting
        String userName = "test";
        String password = "password";
        String nation = "nation";
        LocalDateTime birthOfDayAndTime = LocalDateTime.now();
        BloodType bloodType = BloodType.A형;
        Gender gender = Gender.MALE;


        when(userService.join(userName,password,nation,birthOfDayAndTime,bloodType,gender))
                .thenThrow(new LuckApplicationException(ErrorCode.DUPLICATED_USER_NAME, userName + " is duplicated"));


        // Make a POST request to /api/v1/users/join
        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName,password,nation,birthOfDayAndTime,bloodType,gender)))
                )
                .andExpect(status().isConflict());
    }
}
