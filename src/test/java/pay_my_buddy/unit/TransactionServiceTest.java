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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

}
