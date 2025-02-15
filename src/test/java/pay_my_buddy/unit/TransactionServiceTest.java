package pay_my_buddy.unit;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pay_my_buddy.model.Transaction;
import pay_my_buddy.model.User;
import pay_my_buddy.repository.TransactionRepository;
import pay_my_buddy.repository.UserRepository;
import pay_my_buddy.service.TransactionService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCreateTransactionWithSufficientBalance(){

        // Arrange
        User sender =  new User();
        sender.setId(5515L);
        sender.setBalance(500.0);

        User receiver = new User();
        receiver.setId(5120L);
        receiver.setBalance(150.0);

        when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Transaction transaction = transactionService.createTransaction(sender.getId(), receiver.getId(), "Test transaction", 50.0);

        // Assert
        assertNotNull(transaction);
        assertEquals(450, sender.getBalance());
        assertEquals(200, receiver.getBalance());
    }
    @Test
    public void testCreateTransactionWithInsufficientBalance(){

        // Arrange
        User sender =  new User();
        sender.setId(5515L);
        sender.setBalance(20.0);

        User receiver = new User();
        receiver.setId(5120L);
        receiver.setBalance(150.0);

        when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));

        // Act
        Transaction transaction = transactionService.createTransaction(sender.getId(), receiver.getId(), "Test transaction", 50.0);

        // Assert
        assertNull(transaction);
    }

    @Test
    public void testGetSentTransactions(){
        // Arrange
        String email ="Yassine@example.com";

        Transaction tr1 = new Transaction();
        tr1.setAmount(50.0);
        tr1.setDescription("Remboursement");

        Transaction tr2 = new Transaction();
        tr2.setAmount(100.0);
        tr2.setDescription("Cadeau");

        when(transactionRepository.findTransactionsSentByUser(email))
                .thenReturn(List.of(tr1, tr2));
        // Act
        List<Transaction> sentTransactions = transactionService.getSentTransactions(email);

        // Assert
        assertNotNull(sentTransactions);
        assertEquals(2, sentTransactions.size());
        assertEquals("Remboursement", sentTransactions.get(0).getDescription());
        assertEquals(50, sentTransactions.get(0).getAmount());
        assertEquals("Cadeau", sentTransactions.get(1).getDescription());
        assertEquals(100, sentTransactions.get(1).getAmount());
        verify(transactionRepository, times(1)).findTransactionsSentByUser(email);
    }

    @Test
    public void testGetReceivedTransactions(){
        // Arrange
        String email ="Georges@example.com";

        Transaction tr1 = new Transaction();
        tr1.setAmount(150.0);
        tr1.setDescription("Virement");

        Transaction tr2 = new Transaction();
        tr2.setAmount(600.0);
        tr2.setDescription("Loyer");

        when(transactionRepository.findTransactionsReceivedByUser(email))
                .thenReturn(List.of(tr1, tr2));
        // Act
        List<Transaction> receivedTransactions = transactionService.getReceivedTransactions(email);

        // Assert
        assertNotNull(receivedTransactions);
        assertEquals(2, receivedTransactions.size());
        assertEquals("Virement", receivedTransactions.get(0).getDescription());
        assertEquals(150, receivedTransactions.get(0).getAmount());
        assertEquals("Loyer", receivedTransactions.get(1).getDescription());
        assertEquals(600.0, receivedTransactions.get(1).getAmount());
        verify(transactionRepository, times(1)).findTransactionsReceivedByUser(email);
    }

}
