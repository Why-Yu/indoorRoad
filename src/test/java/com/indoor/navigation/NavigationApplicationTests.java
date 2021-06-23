package com.indoor.navigation;

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
        mockMvc.perform(MockMvcRequestBuilders
                .get("/test")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
