package com.pio.security.jwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pio.security.jwt.constant.UserRole;
import com.pio.security.jwt.dto.UserDTO;
import com.pio.security.jwt.security.JwtAuthenticationFilter;
import com.pio.security.jwt.security.JwtHelper;
import com.pio.security.jwt.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private JwtHelper jwtHelper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO(1, "abc", "1234", UserRole.USER);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    void TestAddUserSuccessful() throws Exception {
        doNothing().when(userService).addUser(any(UserDTO.class));
        this.mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated()).andExpect(content().string("User created Successfully!"));
    }

    /**
     *
     * @throws Exception
     */
    @Test
    void testGetAllUsers() throws Exception {
        List<UserDTO> userDTOList = Arrays.asList(userDTO);
        when(userService.getAllUser()).thenReturn(userDTOList);
        MvcResult mvcResult = mockMvc.perform(get("/user")).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String json = mvcResult.getResponse().getContentAsString();
        UserDTO[] responseUsers = objectMapper.readValue(json, UserDTO[].class);
        assertEquals(200, status);
        assertEquals(1, responseUsers.length);
        assertEquals(userDTO.getId(), responseUsers[0].getId());
        assertEquals(userDTO.getUsername(), responseUsers[0].getUsername());
    }
}
