package com.yourLuck.yourLuck.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourLuck.yourLuck.service.LuckService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LuckController.class)
public class LuckControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LuckService luckService;

}
