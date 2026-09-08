package com.example.library_management.security;

import com.example.library_management.entity.User;
import com.example.library_management.repository.UserRepository;
import com.example.library_management.service.JwtService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest()
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    void setUp() {

        userRepository.save(
                new User(
                        "john",
                        passwordEncoder.encode("password"),
                        "USER"
                )
        );

        userRepository.save(
                new User(
                        "admin",
                        passwordEncoder.encode("password"),
                        "ADMIN"
                )
        );
    }

    @Test
    void shouldReturn401WithoutJwt() throws Exception {

        mockMvc.perform(
                        get("/books")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowAuthenticatedUserWithJwt() throws Exception {

        String token = jwtService.generateToken("john");

        mockMvc.perform(
                        get("/books")
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn403WhenUserTriesToCreateBook() throws Exception {

        String token = jwtService.generateToken("john");

        mockMvc.perform(
                        post("/books")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "title": "Clean Code",
                                "author": "Robert Martin"
                            }
                            """)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAdminToCreateBook() throws Exception {

        String token = jwtService.generateToken("admin");

        mockMvc.perform(
                        post("/books")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "title": "Clean Code",
                                "author": "Robert Martin"
                            }
                            """)
                )
                .andExpect(status().isCreated());
    }



}
