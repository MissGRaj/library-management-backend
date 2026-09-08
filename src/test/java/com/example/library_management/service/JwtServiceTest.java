package com.example.library_management.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    @Test
    void shouldGenerateAndExtractUsernameFromToken(){

//        Arrange
        JwtService jwtService = new JwtService(
                "test-secret-key-for-library-management-system-123456789",
                3600000);

//        Act
        String token = jwtService.generateToken("john");
        String username = jwtService.extractUsername(token);

//        Assert
        assertEquals("john", username);
    }
}
