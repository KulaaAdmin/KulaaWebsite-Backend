//package com.kula.kula_project_backend.service.impl;
//import static org.mockito.Mockito.*;
//
//import com.kula.kula_project_backend.entity.Users;
//import com.kula.kula_project_backend.dao.UsersRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//
//@SpringBootTest
//public class UsersServiceImplTest {
//
//    @Autowired
//    private UsersServiceImpl usersService;
//
//    @MockBean
//    private UsersRepository usersRepository;
//
//    @Test
//    public void testSaveUser_WithNewEmail_ShouldCreateUser() {
//        UsersDTO newUser = new UsersDTO()
//                .setEmail("test@example.com")
//                .setPhoneNumber("1234567890")
//                .setUsername("testuser")
//                .setFirstName("Test")
//                .setLastName("User")
//                .setPassword("password")
//                .setRegistrationMethod("email");
//
//        when(usersRepository.existsByEmail(anyString())).thenReturn(false);
//        when(usersRepository.existsByPhoneNumber(anyString())).thenReturn(false);
//        when(usersRepository.save(any(Users.class))).thenReturn(new Users());
//
//        // Execute
//        Result result = usersService.save(newUser);
//
//        // Verify
//        assertEquals(200, result.getCode());
//        verify(usersRepository, times(1)).save(any(Users.class));
//    }
//
//}
