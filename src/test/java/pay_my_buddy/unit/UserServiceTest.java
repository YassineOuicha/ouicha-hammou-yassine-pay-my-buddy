package pay_my_buddy.unit;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import pay_my_buddy.model.User;
import pay_my_buddy.repository.UserRepository;
import pay_my_buddy.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser(){

        // Arrange
        User user = new User();
        user.setUsername("Yassine");
        user.setEmail("yassine@example.com");

        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.registerUser("Yassine", "yassine@example.com", "password");

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testFindByEmail(){

        // Arrange
        User user = new User();
        user.setEmail("yassine@example.com");
        when(userRepository.findByEmail("yassine@example.com")).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = userService.findByEmail("yassine@example.com");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("yassine@example.com", foundUser.get().getEmail());
    }

}
