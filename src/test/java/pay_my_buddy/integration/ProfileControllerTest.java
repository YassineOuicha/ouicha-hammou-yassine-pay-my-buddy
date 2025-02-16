package pay_my_buddy.integration;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pay_my_buddy.PayMyBuddyApplication;
import pay_my_buddy.model.User;
import pay_my_buddy.service.UserService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = PayMyBuddyApplication.class)
public class ProfileControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "yassine@example.com")
    public void testProfilePage() throws Exception {
        User connectedUser = new User();
        connectedUser.setUsername("Yassine");
        connectedUser.setEmail("yassine@example.com");

        when(userService.getConnectedUser()).thenReturn(connectedUser);

        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("user", "username"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "yassine@example.com")
    public void testUpdateProfile() throws Exception {
        User user = new User();
        user.setUsername("Yassine");
        user.setEmail("yassine@example.com");

        when(userService.getConnectedUser()).thenReturn(user);
        when(passwordEncoder.encode("newPassword")).thenReturn("hashedNewPassword");

        mockMvc.perform(post("/profile/update")
                .param("username", "NewUsername")
                .param("email", "newEmail@example.com")
                .param("password", "newPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("user", "success"));
    }
}
