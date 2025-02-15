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
import pay_my_buddy.model.Transaction;
import pay_my_buddy.model.User;
import pay_my_buddy.service.TransactionService;
import pay_my_buddy.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = PayMyBuddyApplication.class)
public class DashboardControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "charlie@example.com")
    public void testDashboardPage() throws Exception {
        User user = new User();
        user.setEmail("charlie@example.com");
        user.setBalance(1500.0);

        User friend = new User();
        friend.setEmail("hugo@example.com");
        friend.setBalance(500.0);

        Transaction tr1 = new Transaction();
        tr1.setDescription("Test paiement");
        tr1.setAmount(100);
        tr1.setSender(user);
        tr1.setReceiver(friend);

        Transaction tr2 = new Transaction();
        tr2.setDescription("Test remboursement");
        tr2.setAmount(100);
        tr2.setSender(friend);
        tr2.setReceiver(user);

        List<Transaction> sentTransactions = List.of(tr1);
        List<Transaction> receivedTransactions = List.of(tr2);

        when(userService.getConnectedUser()).thenReturn(user);
        when(transactionService.getSentTransactions(user.getEmail())).thenReturn(sentTransactions);
        when(transactionService.getReceivedTransactions(user.getEmail())).thenReturn(receivedTransactions);

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("user", "SentTransactions", "ReceivedTransactions"));
    }


    @Test
    @DirtiesContext
    @WithMockUser(username = "charlie@example.com")
    public void testHandlePayment() throws Exception {

        User sender = new User();
        sender.setEmail("charlie@example.com");
        sender.setBalance(1000.0);

        User receiver = new User();
        receiver.setEmail("hugo@example.com");
        receiver.setBalance(500.0);

        when(userService.getConnectedUser()).thenReturn(sender);
        when(userService.findByEmail("hugo@example.com")).thenReturn(Optional.of(receiver));

        mockMvc.perform(post("/dashboard/payment")
                .param("receiverEmail", "hugo@example.com")
                .param("description", "Test transaction of 50$ from charlie to hugo")
                .param("amount", "50.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }
}
