package pay_my_buddy.integration;


import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import pay_my_buddy.PayMyBuddyApplication;
import pay_my_buddy.model.User;
import pay_my_buddy.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = PayMyBuddyApplication.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }


    // Attention before every test we need to change the email
    // because the user will be saved after each test
    @Test
    @DirtiesContext
    public void testHandleRegisterSuccess() throws Exception {
        doNothing().when(userService).saveUser(any(User.class));
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");

        mockMvc.perform(post("/register")
                        .param("username", "YassineTest3")
                        .param("email", "yassinetest3@example.com")
                        .param("password", "password"))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/login"));
    }

    @Test
    @DirtiesContext
    public void testHandleRegisterFailure() throws Exception {

        doThrow(new RuntimeException("Une erreur s'est produite !")).when(userService).saveUser(any(User.class));

        mockMvc.perform(post("/register")
                        .param("username", "YassineTest")
                        .param("email", "yassinetest@example.com")
                        .param("password", "password"))
                        .andExpect(status().isOk())
                        .andExpect(view().name("register"))
                        .andExpect(model().attributeExists("error"));
    }
}



