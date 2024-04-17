package br.challege.user.management.system.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static br.challege.user.management.system.common.USER;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTeste {

    @Autowired
    private MockMvc mockMvc; // Utilizado para realizar requisições HTTP simuladas

    @Autowired
    private PasswordEncoder passwordEncoder; // Encoder de senha real

    @Autowired
    private ObjectMapper objectMapper; // Objeto para serializar e deserializar objetos Java em JSON

    @Autowired
    private UserService userService;


    @Test
    public void createUser_WithValidData_ReturnsCreated() throws Exception {
        when (userService.create(USER)).thenReturn(USER);

        mockMvc
                .perform(
                        post("/api/users").content(objectMapper.writeValueAsString(USER))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(USER));

    }
}
