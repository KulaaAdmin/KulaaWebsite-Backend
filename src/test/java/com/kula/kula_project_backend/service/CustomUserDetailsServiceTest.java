//package com.kula.kula_project_backend.service;
//
//import com.kula.kula_project_backend.dao.UsersRepository;
//import com.kula.kula_project_backend.entity.Users;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class CustomUserDetailsServiceTest {
//
//    @Autowired
//    private CustomUserDetailsService userDetailsService;
//
//    @MockBean
//    private UsersRepository usersRepository;
//
//    @Test
//    public void loadUserByUsername_UserExists_ReturnsUser() {
//        // Arrange
//        String username = "testuser";
//        Users mockUser = new Users();
//        mockUser.setUsername(username);
//        mockUser.setPasswordHash("hashedpassword");
//        when(usersRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
//
//        // Act
//        UserDetails result = userDetailsService.loadUserByUsername(username);
//
//        // Assert
//        assertEquals(username, result.getUsername());
//    }
//
//    @Test
//    public void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
//        // Arrange
//        String username = "nonexistent";
//        when(usersRepository.findByUsername(username)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(UsernameNotFoundException.class, () -> {
//            userDetailsService.loadUserByUsername(username);
//        });
//    }
//}
