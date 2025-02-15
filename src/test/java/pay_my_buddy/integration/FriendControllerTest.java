package pay_my_buddy.integration;


import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import pay_my_buddy.PayMyBuddyApplication;
import pay_my_buddy.model.User;
import pay_my_buddy.service.UserService;

import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = PayMyBuddyApplication.class)
public class FriendControllerTest {


    @Mock
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(username = "yassine@example.com")
    public void testShowAddFriendPage() throws Exception {
        User user = new User();
        user.setUsername("Yassine");
        user.setEmail("yassine@example.com");
        when(userService.getConnectedUser()).thenReturn(user);

        mockMvc.perform(get("/friends/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("friend"))
                .andExpect(model().attributeExists("username"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "yassine@example.com")
    public void testAddFriendSuccess() throws Exception {
        User currentUser = new User();
        currentUser.setUsername("Yassine");
        currentUser.setEmail("yassine@example.com");

        User friend = new User();
        friend.setEmail("bob@example.com");

        when(userService.getConnectedUser()).thenReturn(currentUser);
        when(userService.findByEmail("bob@example.com")).thenReturn(Optional.of(friend));

        mockMvc.perform(post("/friends/add")
                .param("email", "bob@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "yassine@example.com")
    public void testAddFriendFailure() throws Exception {
        User currentUser = new User();
        currentUser.setUsername("Yassine");

        when(userService.getConnectedUser()).thenReturn(currentUser);
        when(userService.findByEmail("nonExistent@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/friends/add")
                .param("email", "nonExistent@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("friend"))
                .andExpect(model().attributeExists("error"));
    }
}
