package pay_my_buddy.unit;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pay_my_buddy.model.User;
import pay_my_buddy.repository.UserRepository;
import pay_my_buddy.service.CustomUserDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsernameSuccess(){
        // Arrange
        User user = new User();
        user.setEmail("yassine@example.com");
        user.setPassword("hashedPassword");
        when(userRepository.findByEmail("yassine@example.com")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

        // Assert
        assertEquals("yassine@example.com", userDetails.getUsername());
        assertEquals("hashedPassword", userDetails.getPassword());
    }

    @Test
    public void testLoadUserByUsernameFailure(){
        // Arrange
        String email = "yassine@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Assert
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(email));
    }
}
