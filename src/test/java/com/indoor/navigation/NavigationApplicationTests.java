package com.indoor.navigation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indoor.navigation.controller.IndoorDataController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@RunWith(SpringRunner.class)
class NavigationApplicationTests {
    private MockMvc mockMvc;
    @BeforeEach
    public void setMockMvc() throws Exception{
        mockMvc = MockMvcBuilders.standaloneSetup(new IndoorDataController()).build();
    }
    @Test
    void testHello() throws Exception{
        Object randomObj = new Object() {
            public final Double x = 12722082.54;
            public final Double y = 3578787.21;
        };
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(randomObj);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/data/test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
