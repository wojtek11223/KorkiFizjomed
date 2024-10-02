package com.example.KorkiMedic;

import com.example.KorkiMedic.controllers.AuthenticationController;
import com.example.KorkiMedic.dto.LoginResponse;
import com.example.KorkiMedic.dto.LoginUserDto;
import com.example.KorkiMedic.dto.RegisterUserDto;
import com.example.KorkiMedic.entity.User;
import com.example.KorkiMedic.enums.Role;
import com.example.KorkiMedic.service.AuthenticationService;
import com.example.KorkiMedic.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    void testRegister() throws Exception {
        RegisterUserDto registerUserDto = new RegisterUserDto(
                "email@example.com",
                "password123",
                "John",
                "Doe",
                "123456789",
                LocalDate.of(1990, 1, 1)
        );

        User user = new User();
        user.setEmail(registerUserDto.getEmail());
        user.setPassword("encodedPassword");
        user.setFirstName(registerUserDto.getFirstName());
        user.setLastName(registerUserDto.getLastName());
        user.setPhoneNumber(registerUserDto.getPhoneNumber());
        user.setDateOfBirth(registerUserDto.getDateOfBirth());
        user.setRoles(Set.of(Role.USER, Role.ADMIN));

        when(authenticationService.signup(registerUserDto)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"email@example.com\",\"password\":\"password123\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"phoneNumber\":\"123456789\",\"dateOfBirth\":\"1990-01-01\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is("email@example.com")))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));
    }

    @Test
    void testAuthenticate() throws Exception {
        LoginUserDto loginUserDto = new LoginUserDto("email@example.com", "password123");
        User authenticatedUser = new User();
        authenticatedUser.setEmail(loginUserDto.getEmail());
        authenticatedUser.setPassword("encodedPassword");
        authenticatedUser.setRoles(Set.of(Role.USER, Role.ADMIN));

        String jwtToken = "jwtToken";
        when(authenticationService.authenticate(loginUserDto)).thenReturn(authenticatedUser);
        when(jwtService.generateToken(authenticatedUser)).thenReturn(jwtToken);
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"email@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.jwtToken", is(jwtToken)))
                .andExpect(jsonPath("$.expirationTime", is(3600)));
    }
}
